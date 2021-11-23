package com.example.reto1dm

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val splashTimeout:Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions(arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ), 1)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                var allGranted = true
                for (result in grantResults) {
                    Log.e(">>>", "$result")
                    if (result == -1) allGranted = false
                }
                when (allGranted) {
                    true -> {
                        Handler(mainLooper).postDelayed({
                            startActivity(Intent(this, BaseActivity::class.java))
                            finish()
                        }, splashTimeout)
                    }
                    false -> {
                        Toast.makeText(this, "Tiene que aceptar todos los permisos.", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }
}