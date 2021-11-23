package com.example.reto1dm

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reto1dm.databinding.FragmentPublicationsBinding
import com.example.reto1dm.model.Business
import com.example.reto1dm.model.Publication
import com.google.gson.Gson

class PublicationsFragment : Fragment(), AddPublicationsFragment.OnNewPublicationListener{

    private var _binding: FragmentPublicationsBinding? = null
    private val binding get() = _binding!!
    private var index: Int = 0
    private var flag: Boolean = false

    private val adapter = PublicationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPublicationsBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        //Recrear el estado

        if(!flag) {
            flag = true
            val publicationRecycler = binding.publicationRecycler
            publicationRecycler.setHasFixedSize(true)
            publicationRecycler.layoutManager = LinearLayoutManager(activity)
            publicationRecycler.adapter = adapter
        }

        binding.addBtn2.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, AddPublicationsFragment.newInstance())
            transaction.addToBackStack("publication")
            transaction.commit()
        }

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val json = sharedPref?.getString("publication"+index, "NO_DATA")
        if (json != "NO_DATA") {
            index = index + 1
            val publication = Gson().fromJson(json, Publication::class.java)
            adapter.addPublication(publication)
            Log.e(">>>Size", adapter.itemCount.toString())
            binding.addBtn.visibility = View.INVISIBLE
            binding.noPublicationsCL.visibility = View.INVISIBLE
            binding.publicationRecycler.visibility = View.VISIBLE
            binding.addBtn2.visibility = View.VISIBLE
        }

        /*
        if(flag == true) {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            val json = sharedPref?.getString("publication"+index, "NO_DATA")
            if (json != "NO_DATA") {
                index = index + 1
                val publication = Gson().fromJson(json, Publication::class.java)
                Log.e(">>>", "onCheckState")
                binding.addBtn2.visibility = View.VISIBLE
                binding.noPublicationsCL.visibility = View.INVISIBLE
                binding.addBtn.visibility = View.INVISIBLE
                binding.publicationRecycler.visibility = View.VISIBLE
                adapter.addPublication(publication)
            }
        }

         */

        binding.addBtn.setOnClickListener {

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, AddPublicationsFragment.newInstance())
            transaction.addToBackStack("publication")
            transaction.commit()
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        addPublications()

    }

    fun addPublications(){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val json = sharedPref?.getString("publication"+index, "NO_DATA")
        if (json != "NO_DATA") {
            index = index + 1
            val publication = Gson().fromJson(json, Publication::class.java)
            adapter.addPublication(publication)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PublicationsFragment()
    }

    override fun onNewPublication(
        eventName: String,
        profileName: String,
        ubication: String,
        startTime: String,
        finishTime: String,
        profilePhotoPath: String
    ) {

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val json = sharedPref?.getString("publication"+index, "NO_DATA")
        if (json != "NO_DATA") {
            index = index + 1
            val publication = Gson().fromJson(json, Publication::class.java)
            adapter.addPublication(publication)
            Log.e(">>>Size", adapter.itemCount.toString())
        }

    }



}