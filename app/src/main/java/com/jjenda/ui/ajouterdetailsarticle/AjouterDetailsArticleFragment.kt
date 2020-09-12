package com.jjenda.ui.ajouterdetailsarticle

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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.jjenda.R
import com.jjenda.databinding.FragmentAjouterDetailsArticleBinding
import com.jjenda.reseau.Repository
import com.jjenda.ui.SharedArticleViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar


class AjouterDetailsArticleFragment : Fragment() {

    lateinit var binding : FragmentAjouterDetailsArticleBinding
    lateinit var viewModel: AjouterDetailsArticleViewModel
    lateinit var sharedArticleViewModel : SharedArticleViewModel
    private var mImageName: String? = null
    private var mImageBitmap: Bitmap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest : LocationRequest

    companion object {
        const val LOCATION_SETTING_REQUEST = 999
        const val LOCATION_PERMISSION_REQUEST = 200
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_ajouter_details_article, container, false)

        viewModel = ViewModelProviders.of(this).get(AjouterDetailsArticleViewModel::class.java)
        sharedArticleViewModel  = ViewModelProvider(requireActivity()).get(SharedArticleViewModel::class.java)

        setupArticleImageAndImagName()
        getLocation()

        binding.viewModel = viewModel
        binding.btnPosterDetailsArticle.setOnClickListener{
            viewModel.publicKey = getString(R.string.imagekit_io_public_key)
            viewModel.postArticle()
            showLoading()
        }

        viewModel.btnEnabled.observe(viewLifecycleOwner, Observer {
            binding.btnPosterDetailsArticle.isEnabled = it
        })

        viewModel.eventArticlePosted.observe(viewLifecycleOwner, Observer {
            if(it) {
                sharedArticleViewModel.startEventArticlePosted()
                navigateToarticlesFragment()
                viewModel.onArticlePostedFinished()
            }
        })

        viewModel.eventErrorWhenPostingArticle.observe(viewLifecycleOwner, Observer {
            if(it) {
                showForm()
                Snackbar.make(binding.root, R.string.connection_problem_message, Snackbar.LENGTH_LONG).show()
                viewModel.onErrorWhenPostingArticleFinished()
            }
        })

        showForm()

        return binding.root
    }

    private fun navigateToarticlesFragment() {
        Navigation.findNavController(binding.root)
                .navigate(AjouterDetailsArticleFragmentDirections
                        .actionAjouterDetailsArticleFragmentToArticlesFragment())
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        formEnabled(false)
    }

    private fun showForm() {
        binding.progressBar.visibility = View.GONE
        formEnabled(true)
    }

    private fun formEnabled(enabled : Boolean) {
        binding.ilCategorieDetailsArticle.isEnabled = enabled
        binding.ilDescriptionDetailsArticle.isEnabled = enabled
        binding.ilPrixDetailsArticle.isEnabled = enabled
        binding.btnPosterDetailsArticle.isEnabled = enabled
    }

    private fun setupArticleImageAndImagName() {

        arguments?.let {
            val args = AjouterDetailsArticleFragmentArgs.fromBundle(it)
            mImageName = args.photoName
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

        val task = LocationServices.getSettingsClient(requireContext())
                .checkLocationSettings(builder.build())

        setupListenersForLocationSettings(task)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setupListenersForLocationSettings(task : Task<LocationSettingsResponse>) {
        task.addOnSuccessListener {
            startLocationUpdates()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().

                    exception.startResolutionForResult(activity, LOCATION_SETTING_REQUEST)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback(),
                    Looper.getMainLooper()
            )
        } else {
            // You can directly ask for the permission.
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
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