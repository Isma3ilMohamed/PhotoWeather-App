package com.isma3il.photoweatherapp.ui.weather_photo

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.PixelCopy
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.isma3il.core.base.BaseFragment
import com.isma3il.core.utils.*
import com.isma3il.photoweatherapp.databinding.FragmentWeatherPhotoBinding
import com.isma3il.photoweatherapp.ui.weather_photo.ext.bindWeather
import com.nmd.screenshot.Screenshot

import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers


@AndroidEntryPoint
class WeatherPhotoFragment : BaseFragment<FragmentWeatherPhotoBinding>(
    FragmentWeatherPhotoBinding::inflate
) {
    private val viewModel by viewModels<WeatherPhotoViewModel>()

    //Rec vars
    private var uri: Uri? = null
    private var photoPath: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
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

            //start saving
            replaceOldPicWithGeneratedOne()

        })

        viewModel.savedImageData.observe(this@WeatherPhotoFragment, Observer {
            if (it)
                fabProgress.fabLoading.secretB()
            fabProgress.fabShare.isEnabled = it
        })
    }


    private fun initComponents() = with(binding) {
        ivCaptured.loadImage(uri)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        photoLayout.setOnClickListener { }

        fabProgress.fabShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/png"
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(shareIntent, "Share with..."))
        }
    }

    private fun getArgs() {
        uri = arguments?.getParcelable(URI_PATH)
        photoPath = arguments?.getString(IMAGE_PATH)
    }


    private fun replaceOldPicWithGeneratedOne() {


        val screenshot = Screenshot(requireActivity()).also {
            it.showNotification(false)
            it.showPreview(false)
            it.setCallback { success, filePath, bitmap ->

                binding.ivCaptured.setImageBitmap(bitmap)
                binding.grpInfo.secretB()
                requireContext().saveImage(uri ?: Uri.EMPTY, bitmap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        //show share loading
                        binding.fabProgress.fabLoading.showB()
                        binding.fabProgress.fabShare.isEnabled = false
                    }
                    .subscribeBy(
                        onNext = {
                            viewModel.saveImage(filePath)
                        },
                        onError = {
                            errorMessage(it.localizedMessage ?: "")
                        },
                        onComplete = {
                            //hide share loading
                            binding.fabProgress.fabLoading.secretB()
                            binding.fabProgress.fabShare.isEnabled = true
                        }
                    )


            }
        }

        screenshot.takeScreenshotFromView(binding.photoLayout)

/*       requireActivity().getScreenShotBitmap(binding.photoLayout)
           .flatMap {bitmap->
               //show capture screen
               binding.ivCaptured.setImageBitmap(bitmap)
               binding.grpInfo.secretB()
               requireContext().saveImage(uri?: Uri.EMPTY,bitmap)
           }
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .doOnSubscribe {
               //show share loading
               binding.fabProgress.fabLoading.showB()
               binding.fabProgress.fabShare.isEnabled = false
           }
           .subscribeBy(
               onNext = {
                   viewModel.saveImage(photoPath?:"")
               },
               onError = {
                   errorMessage(it.localizedMessage?:"")
               },
               onComplete = {
                   //hide share loading
                   binding.fabProgress.fabLoading.secretB()
                   binding.fabProgress.fabShare.isEnabled = true
               }
           )*/
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun screenShot(view: View){
        val bitmap=Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        PixelCopy.request(requireActivity().window,bitmap,
            { TODO("Not yet implemented") }, Handler()
        )
    }

    private fun errorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val URI_PATH = "URI_PATH"
        private const val IMAGE_PATH = "IMAGE_PATH"
        private const val REQUEST_CODE = 1254
        fun newInstance(uri: Uri?, path: String?) = WeatherPhotoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(URI_PATH, uri)
                putString(IMAGE_PATH, path)
            }
        }
    }

}