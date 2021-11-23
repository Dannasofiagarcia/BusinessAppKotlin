package com.example.reto1dm

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.Fragment
import com.example.reto1dm.databinding.ActivityBaseBinding

    private lateinit var profileTaskFragment: ProfileFragment
    private lateinit var publicationsFragment: PublicationsFragment
    private lateinit var mapsFragment: MapsFragment
    private lateinit var binding: ActivityBaseBinding

class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getPreferences(Context.MODE_PRIVATE).edit().clear().commit()

        // Fragment initialization
        profileTaskFragment = ProfileFragment.newInstance()
        publicationsFragment = PublicationsFragment.newInstance()
        mapsFragment = MapsFragment.newInstance(1)



        showFragment(profileTaskFragment)

        binding.navigator.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    showFragment(profileTaskFragment)
                }
                R.id.publications -> {
                    showFragment(publicationsFragment)
                }
                R.id.mapTab -> {
                    showFragment(mapsFragment)
                }
            }
            true
        }
    }

    private fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.commit()
    }

}