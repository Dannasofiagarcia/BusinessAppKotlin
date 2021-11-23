package com.example.reto1dm

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.fragment.app.setFragmentResultListener
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.reto1dm.databinding.FragmentAddPublicationsBinding
import com.example.reto1dm.model.Business
import com.example.reto1dm.model.Publication
import com.google.gson.Gson
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "1"

class AddPublicationsFragment : Fragment() {

    private var flag: Boolean = false
    private var currentUsage: Int? = null

    private var months = arrayOf("ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic")
    private var _binding:FragmentAddPublicationsBinding? = null
    private val binding get() = _binding!!
    private val publication : Publication = Publication()
    private var index: Int = 0

    //Listener
    var listener: OnNewPublicationListener? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentUsage = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPublicationsBinding.inflate(inflater, container, false)
        val view  = binding.root

        // Get new location
        binding.imageView5.setOnClickListener {
            setFragmentResultListener("newLoc") { requestKey, bundle ->
                val result = bundle.get("bundleKey")
                binding.ubicationSelectedTV.text = result.toString()
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView,MapsFragment.newInstance(0) )
            transaction.addToBackStack("locationAdder")
            transaction.commit()
        }

        binding.startBtn.setOnClickListener{
            showDateTimePicker(true)
        }

        binding.endBtn.setOnClickListener {
            showDateTimePicker()
        }

        // Create a new publication
        binding.createBtn.setOnClickListener{

            // Toast.makeText(activity, text, Toast.LENGTH_LONG).show()

            publication.eventName = binding.eventNameET.text.toString()
            publication.ubication = binding.ubicationSelectedTV.text.toString()
            publication.startTime = binding.startBtn.text.toString()
            publication.finishTime = binding.endBtn.text.toString()

            val sharedPref2 = activity?.getPreferences(Context.MODE_PRIVATE)
            val json2 = sharedPref2?.getString("currentBusiness", "NO_DATA")
            var profilePhotoPath : String = ""
            var profileName : String = ""
            if (json2 != "NO_DATA") {
                val business = Gson().fromJson(json2, Business::class.java)

                publication.profilePhotoPath = business.businessPhotoPath
                publication.profileName = business.businessName
            }

            val json = Gson().toJson(publication)
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            sharedPref?.edit()?.putString("publication" + index, json)?.apply()
            index = index + 1


            listener?.let{
                //it es listener pero no null
               // it.onNewPublication(eventName, profileName, ubication, startTime, endTime, profilePhotoPath)
            }


            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, PublicationsFragment.newInstance())
            transaction.addToBackStack("publication")
            transaction.commit()



            //childFragmentManager.popBackStack()

        }
        return view
    }

    private fun showDateTimePicker(start: Boolean = false){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val tpd = TimePickerDialog(requireContext(), { _, i, i2 ->
            val isPM: Boolean = i >= 12
            when {
                start -> {
                    binding.startBtn.text = binding.startBtn.text.toString() +
                            java.lang.String.format( "%02d:%02d %s", if (i=== 12 || i=== 0) 12 else i % 12, i2, if (isPM) "PM" else "AM" )
                }
                else -> binding.endBtn.text = binding.endBtn.text.toString() +
                        java.lang.String.format( "%02d:%02d %s", if (i=== 12 || i=== 0) 12 else i % 12, i2, if (isPM) "PM" else "AM" )
            }
        }, hour, minute, true)
        tpd.show()
        val dpd = DatePickerDialog(requireContext(), { _, i, i2, i3 ->
            when {
                start -> {
                    val monthName = months[i2]
                    binding.startBtn.text = "${i} $monthName ${i3} "
                }
                else -> {
                    val monthName = months[i2]
                    binding.endBtn.text = "${i} $monthName ${i3} "
                }
            }
        }, year, month, day)
        dpd.show()
    }

    override fun onDestroyView() {
        Log.e(">>>", "onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    //Publicación del dato
    interface OnNewPublicationListener{
        fun onNewPublication(eventName: String, profileName: String, ubication: String, startTime: String, finishTime: String, profilePhotoPath: String)
    }

    companion object {

        @JvmStatic
        fun newInstance() = AddPublicationsFragment()
    }



}