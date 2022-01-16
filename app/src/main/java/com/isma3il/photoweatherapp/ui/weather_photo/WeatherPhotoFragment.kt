package com.isma3il.photoweatherapp.ui.weather_photo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.isma3il.core.base.BaseFragment
import com.isma3il.core.utils.*
import com.isma3il.photoweatherapp.databinding.FragmentWeatherPhotoBinding
import com.isma3il.photoweatherapp.ui.weather_photo.ext.bindWeather

import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class WeatherPhotoFragment : BaseFragment<FragmentWeatherPhotoBinding>(
    FragmentWeatherPhotoBinding::inflate
) {
    private val viewModel by viewModels<WeatherPhotoViewModel>()

    private val compositeDisposable = CompositeDisposable()

    //Rec vars
    private var uri: Uri? = null
    private var photoPath: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var isSavedInDatabase = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
        initComponents()
        setupObservables()

    }

    @SuppressLint("MissingPermission")
    private fun setupObservables() = with(binding) {
        //loading
        viewModel.loadingLiveData.observe(this@WeatherPhotoFragment, Observer {
            if (it) progressBar.showB() else progressBar.secretB()
        })

        //error message
        viewModel.errorMessageLiveData.observe(this@WeatherPhotoFragment, Observer {
            errorMessage(it)
        })

        //fetch last location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                viewModel.getWeatherInfo(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
            }

        // fetch weather info
        viewModel.weatherInfoLiveData.observe(this@WeatherPhotoFragment, Observer {
            grpInfo.showB()
            this@WeatherPhotoFragment.bindWeather(it)
            fabProgress.root.showB()
            hideShareLoading(true)

        })

        //save image in db
        viewModel.savedImageData.observe(this@WeatherPhotoFragment, Observer {
            isSavedInDatabase = true

            startShareIntent()
        })
    }


    private fun initComponents() = with(binding) {
        ivCaptured.loadImage(uri)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fabProgress.fabShare.setOnClickListener {
            if (isSavedInDatabase) {
                startShareIntent()
            } else {
                generateBitmap()
            }
        }
    }

    private fun startShareIntent() {
        binding.fabProgress.root.showB()
        hideShareLoading(true)

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/png"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(shareIntent, "Share with..."))
    }

    private fun getArgs() {
        uri = arguments?.getParcelable(URI_PATH)
        photoPath = arguments?.getString(IMAGE_PATH)
    }


    private fun generateBitmap() {
        binding.fabProgress.root.secretB()
        hideShareLoading(false)

        compositeDisposable.addAll(
            ScreenShotUtils.screenShotSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { bitmap ->
                        binding.ivCaptured.setImageBitmap(bitmap)
                        binding.grpInfo.secretB()
                        saveImageAsFile(bitmap)
                    },
                    onError = {
                        errorMessage(it.localizedMessage ?: "")
                    }
                ),

            //delay 2 seconds to prevent take fab progress in screenshot
            Observable.timer(2, TimeUnit.SECONDS)
                .subscribe {
                    ScreenShotUtils.takeScreenShot(binding.root, requireActivity())

                }
        )
    }

    private fun saveImageAsFile(bitmap: Bitmap) {
        compositeDisposable.add(
            requireContext().saveImage(uri ?: Uri.EMPTY, bitmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        viewModel.saveImage(photoPath ?: "")
                    },
                    onError = {
                        errorMessage(it.localizedMessage ?: "")
                    }
                )
        )
    }


    private fun errorMessage(message: String) {
        Timber.e(message)
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }


    private fun showShareLoading(shouldEnableShare: Boolean = false) = with(binding) {
        fabProgress.fabLoading.showB()
        fabProgress.fabShare.isEnabled = shouldEnableShare
    }

    private fun hideShareLoading(shouldEnableShare: Boolean = false) = with(binding) {
        fabProgress.fabLoading.secretB()
        fabProgress.fabShare.isEnabled = shouldEnableShare
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }

    companion object {
        private const val URI_PATH = "URI_PATH"
        private const val IMAGE_PATH = "IMAGE_PATH"
        fun newInstance(uri: Uri?, path: String?) = WeatherPhotoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(URI_PATH, uri)
                putString(IMAGE_PATH, path)
            }
        }
    }

}