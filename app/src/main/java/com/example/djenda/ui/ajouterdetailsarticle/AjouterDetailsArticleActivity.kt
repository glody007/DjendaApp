package com.example.djenda.ui.ajouterdetailsarticle

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.djenda.R
import com.example.djenda.databinding.ActivityAjouterDetailsArticleBinding
import com.example.djenda.reseau.Repository
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class AjouterDetailsArticleActivity : AppCompatActivity() {
    lateinit var binding : ActivityAjouterDetailsArticleBinding
    lateinit var viewModel: AjouterDetailsArticleViewModel
    private var mImageName: String? = null
    private var mImageBitmap: Bitmap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient;
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest : LocationRequest

    companion object {
        const val LOCATION_SETTING_REQUEST = 999
        const val LOCATION_PERMISSION_REQUEST = 200
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ajouter_details_article)

        viewModel = ViewModelProviders.of(this).get(AjouterDetailsArticleViewModel::class.java)

        setupArticleImageAndImagName()
        getLocation()

        binding.btnPosterDetailsArticle.setOnClickListener{
            viewModel.article.description = binding.tiDescriptionDetailsArticle.text.toString()
            viewModel.article.nom = binding.tiNomDetailsArticle.text.toString()
            viewModel.article.categorie = binding.tiCategorieDetailsArticle.text.toString()
            viewModel.article.prix = binding.tiPrixDetailsArticle.text.toString().toInt()
            viewModel.publicKey = getString(R.string.imagekit_io_public_key)

            viewModel.postArticle()
        }

        viewModel.eventArticlePosted.observe(this, Observer {
            if(it) {
                Toast.makeText(this, "Article posted", Toast.LENGTH_LONG).show()
            }
        })

        viewModel.eventErrorWhenPostingArticle.observe(this, Observer {
            if(it) {
                Toast.makeText(this, "Error when posting article", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupArticleImageAndImagName() {
        val extras = intent.extras
        if (extras != null) {
            //val image_path = extras.getString("photo_uri")
            mImageName = extras.getString("photo_name")
            mImageBitmap = BitmapFactory.decodeByteArray(Repository.getInstance().photArticle, 0, Repository.getInstance().photArticle.size)
            binding.ivImageArticle.setImageBitmap(mImageBitmap)
            viewModel.fileName = mImageName.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getLocation() {
        locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

        val task = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build())

        setupListenersForLocationSettings(task)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setupListenersForLocationSettings(task : Task<LocationSettingsResponse>) {
        task.addOnSuccessListener { locationSettingsResponse ->
            startLocationUpdates()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().

                    exception.startResolutionForResult(this@AjouterDetailsArticleActivity, LOCATION_SETTING_REQUEST)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback(),
                    Looper.getMainLooper()
            )
        } else {
            // You can directly ask for the permission.
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST);
        }
    }

    private fun locationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                viewModel.setLocation(locationResult.lastLocation.longitude.toString(),
                        locationResult.lastLocation.latitude.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LOCATION_SETTING_REQUEST -> {
                if(resultCode == Activity.RESULT_OK) {
                    startLocationUpdates()
                }
                else {
                    getLocation()
                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                                grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("Not Empty", "Granted")
                } else {
                    Log.d("Not Empty", "Not Granted")
                }
                startLocationUpdates()
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}