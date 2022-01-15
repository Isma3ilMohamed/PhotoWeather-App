package com.isma3il.photoweatherapp.ui.main

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.isma3il.core.base.BaseFragment
import com.isma3il.core.utils.*
import com.isma3il.photoweatherapp.BuildConfig
import com.isma3il.photoweatherapp.R
import com.isma3il.photoweatherapp.databinding.FragmentMainBinding
import com.isma3il.photoweatherapp.ui.main.adapter.WeatherPhotoAdapter
import com.isma3il.photoweatherapp.ui.weather_photo.WeatherPhotoFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {


    private val viewModel by viewModels<MainViewModel>()

    private val photoAdapter by lazy {
        WeatherPhotoAdapter()
    }

    private var imageUri: Uri? = null

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Timber.e("${it.key} = ${it.value}")
            }

        }

    private val takePictureResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->

            if (isSuccess)
                parentFragmentManager.addFragment(
                    WeatherPhotoFragment.newInstance(imageUri),
                    R.id.main_container
                )

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
        setupObservables()
        //fetch history
        viewModel.getHistory()
    }

    private fun setupObservables() = with(binding) {
        //loading
        viewModel.loadingLiveData.observe(this@MainFragment, Observer {
            if (it) progressBar.showB() else progressBar.secretB()
        })

        //error message
        viewModel.errorMessageLiveData.observe(this@MainFragment, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

        viewModel.historyLiveData.observe(this@MainFragment, Observer { data ->
            if (data.isEmpty()) {
                rvHistory.secretB()
                emptyView.showB()
            } else {
                rvHistory.showB()
                emptyView.secretB()

                photoAdapter.addData(data)
            }

        })

    }

    private fun initComponents() = with(binding) {
        rvHistory.adapter = photoAdapter

        fabCapture.setOnClickListener {
            if (requireContext().hasPermissions(getPermissions())) {
                takePicture()
            } else {
                requestPermissions.launch(
                    getPermissions()
                )
            }

        }


    }

    private fun takePicture() {
        getImageUri().let {
            imageUri = it
            takePictureResult.launch(imageUri)
        }
    }

    private fun getImageUri(): Uri {
        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider", requireContext().createImageFile()
        )
    }


    private fun getPermissions(): Array<String> {
        val permissions = mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (!hasSdkHigherThan(Build.VERSION_CODES.Q)) {
            permissions += Manifest.permission.WRITE_EXTERNAL_STORAGE
        }

        return permissions.toTypedArray()
    }


    companion object {
        fun newInstance() = MainFragment()
    }

}