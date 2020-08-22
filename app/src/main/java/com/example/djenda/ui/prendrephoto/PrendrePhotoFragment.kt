package com.example.djenda.ui.prendrephoto

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.camerakit.CameraKitView
import com.example.djenda.R
import com.example.djenda.databinding.FragmentPrendrePhotoBinding
import com.example.djenda.reseau.Repository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class PrendrePhotoFragment : Fragment() {

    lateinit var binding : FragmentPrendrePhotoBinding
    private var cameraKitView: CameraKitView? = null
    private var buttonPrendrePhoto: FloatingActionButton? = null
    private var mPhotoName = "default.jpg"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_prendre_photo, container, false)
        // Request camera permissions
        cameraKitView =  binding.camera
        binding.imgCapture.setOnClickListener { prendrePhoto() }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun prendrePhoto() {
        cameraKitView?.captureImage { cameraKitView, capturedImage ->
            Log.d("prendre photo", "debut")
            if (hasPermissionToWriteInExternalStorage()) {
                Log.d("prendre photo", "debut")
                saveImage(capturedImage)
            } else {
                requestPermissionToWriteInExternalStorage()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun hasPermissionToWriteInExternalStorage(): Boolean {
        return  checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }


    private fun requestPermissionToWriteInExternalStorage() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 42)
    }

    private fun saveImage(capturedImage: ByteArray) {
        Repository.getInstance().photArticle = capturedImage
        val savedPhoto = createFileToSave()
        try {
            Log.d("prendre photo", "debut")
            val outputStream = FileOutputStream(savedPhoto.path)
            outputStream.write(capturedImage)
            outputStream.close()
            Log.d("prendre photo", "fin")
            startAjouterDetailsActivity(savedPhoto.path)
        } catch (e: IOException) {
            Log.d("prendre photo", "erreur")
            e.printStackTrace()
        }
    }

    private fun startAjouterDetailsActivity(pathPhoto: String) {
        Navigation.findNavController(binding.root)
                .navigate(PrendrePhotoFragmentDirections
                        .actionPrendrePhotoFragmentToAjouterDetailsArticleFragment(pathPhoto, mPhotoName))
    }

    private fun createFileToSave(): File {
        val dossier = File(Environment.getExternalStorageDirectory().toString() + "/djenda")
        if (!dossier.exists()) {
            dossier.mkdirs()
        }
        mPhotoName = generatePhotoName()
        return File(dossier, mPhotoName)
    }

    private fun generatePhotoName(): String {
        val d = Date()
        val s = DateFormat.format("MM-dd-yy_hh-mm-ss", d.time)
        return "$s.jpg"
    }

    override fun onStart() {
        super.onStart()
        cameraKitView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
        cameraKitView?.onResume()
    }

    override fun onPause() {
        cameraKitView?.onPause()
        super.onPause()
    }

    override fun onStop() {
        cameraKitView?.onStop()
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraKitView?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}