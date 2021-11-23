package com.example.reto1dm

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import com.example.reto1dm.databinding.FragmentEditProfileBinding
import com.example.reto1dm.model.Business
import com.google.gson.Gson
import org.json.JSONObject
import java.io.File


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private var file: File? = null
    private val options = arrayOf<CharSequence>("Tomar foto", "Escoger de la galería", "Cancelar")
    private val business : Business = Business()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        // Check if current business is set
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val json = sharedPref?.getString("currentBusiness", "NO_DATA")
        if (json != "NO_DATA") {
            val business = Gson().fromJson(json, Business::class.java)
            val file2 = File(business.businessPhotoPath)
            setImageThumbnail(file2)
            binding.editTextBusinessDesc.setText(business.businessDescription)
            binding.editTextBusinessName.setText(business.businessName)
        }

        val cameraLauncher = registerForActivityResult(StartActivityForResult(), ::onCameraResult)
        val galleryLauncher = registerForActivityResult(StartActivityForResult(), ::onGalleryResult)

        binding.editBusinessPhotoIB.setOnClickListener {

            // Ask the user for either gallery or camera
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Escoge la foto de tu negocio")
            builder.setItems(options, DialogInterface.OnClickListener { dialogInterface, i ->
                when (options[i]){
                    "Tomar foto" -> {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        file = File("${activity?.getExternalFilesDir(null)}/photo.png")
                        val uri = FileProvider.getUriForFile(activity?.applicationContext!!, activity?.packageName!!, file!!)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        Log.e(">>>>", file?.path.toString())
                        cameraLauncher.launch(intent)
                    }
                    "Escoger de la galería" -> {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        galleryLauncher.launch(intent)
                    }
                    else -> {
                        dialogInterface.dismiss()
                    }
                }
            })
            builder.show()

        }

        // Create button interaction
        binding.editButton.setOnClickListener {

            // Save on sharedPreferences the current business via Gson serialization
            business.businessName = binding.editTextBusinessName.text.toString()
            business.businessDescription = binding.editTextBusinessDesc.text.toString()
            val json = Gson().toJson(business)
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            sharedPref?.edit()?.putString("currentBusiness", json)?.apply()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, ProfileFragment.newInstance())
            transaction.commit()

        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditProfileFragment()
    }

    private fun onCameraResult (result: ActivityResult) {
        when (result.resultCode) {
            RESULT_OK -> {
                setImageThumbnail(file)
                business.businessPhotoPath = file?.path.toString()
            }
            RESULT_CANCELED -> {
                Toast.makeText(activity?.applicationContext!!, "No tomó una foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onGalleryResult (result: ActivityResult) {
        when (result.resultCode) {
            RESULT_OK -> {
                val uriImage = result.data?.data
                uriImage?.let {
                    binding.editBusinessPhotoIB.setImageURI(uriImage)
                    val bitmap = (binding.editBusinessPhotoIB.drawable as BitmapDrawable).bitmap
                    val thumbnail = Bitmap.createScaledBitmap(bitmap, bitmap.width / 4, bitmap.height / 4, true)
                    val params: ConstraintLayout.LayoutParams = binding.editBusinessPhotoIB.layoutParams as ConstraintLayout.LayoutParams
                    params.setMargins(0,0,0,0)
                    params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                    params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
                    binding.editBusinessPhotoIB.layoutParams = params
                    binding.editBusinessPhotoIB.setImageBitmap(thumbnail)
                }
            }
            RESULT_CANCELED -> {
                Toast.makeText(activity?.applicationContext!!, "No eligió una foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setImageThumbnail (file : File?) {
        val bitmap = BitmapFactory.decodeFile(file?.path)
        val thumbnail = Bitmap.createScaledBitmap(bitmap, bitmap.width / 4, bitmap.height / 4, true)
        val params: ConstraintLayout.LayoutParams = binding.editBusinessPhotoIB.layoutParams as ConstraintLayout.LayoutParams
        params.setMargins(0,0,0,0)
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        binding.editBusinessPhotoIB.layoutParams = params
        binding.editBusinessPhotoIB.setImageBitmap(thumbnail)
    }

}