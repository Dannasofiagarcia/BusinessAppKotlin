package com.example.reto1dm

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginStart
import androidx.fragment.app.FragmentManager
import com.example.reto1dm.databinding.FragmentProfileBinding
import com.example.reto1dm.model.Business
import com.google.gson.Gson
import org.json.JSONObject

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        checkState()
        binding.editBtn1.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, EditProfileFragment.newInstance())
            transaction.addToBackStack("profile")
            transaction.commit()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        checkState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkState() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val json = sharedPref?.getString("currentBusiness", "NO_DATA")
        if (json != "NO_DATA") {
            val business = Gson().fromJson(json, Business::class.java)
            val params: ConstraintLayout.LayoutParams = binding.imageView2.layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0,0,0,0)
            params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
            binding.imageView2.layoutParams = params
            binding.imageView2.setImageBitmap(BitmapFactory.decodeFile(business.businessPhotoPath))
            binding.textView.visibility = View.INVISIBLE
            binding.textView2.text = business.businessName
            binding.textView3.text = business.businessDescription
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}