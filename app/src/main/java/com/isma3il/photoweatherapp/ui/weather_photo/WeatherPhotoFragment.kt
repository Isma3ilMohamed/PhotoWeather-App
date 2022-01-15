package com.isma3il.photoweatherapp.ui.weather_photo

import android.annotation.SuppressLint
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.isma3il.core.base.BaseFragment
import com.isma3il.core.utils.loadImage
import com.isma3il.core.utils.secretB
import com.isma3il.core.utils.showB
import com.isma3il.photoweatherapp.R
import com.isma3il.photoweatherapp.databinding.FragmentWeatherPhotoBinding
import com.isma3il.photoweatherapp.ui.weather_photo.ext.bindWeather
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherPhotoFragment : BaseFragment<FragmentWeatherPhotoBinding>(
    FragmentWeatherPhotoBinding::inflate
){
    private val viewModel by viewModels<WeatherPhotoViewModel>()

    //Rec vars
    private var uri:Uri? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
        initComponents()
        setupObservables()


    }

    @SuppressLint("MissingPermission")
    private fun setupObservables()= with(binding) {
        //loading
        viewModel.loadingLiveData.observe(this@WeatherPhotoFragment, Observer {
            if (it) progressBar.showB() else progressBar.secretB()
        })

        //error message
        viewModel.errorMessageLiveData.observe(this@WeatherPhotoFragment, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                viewModel.getWeatherInfo(location?.latitude?:0.0,location?.longitude?:0.0)
            }
        viewModel.weatherInfoLiveData.observe(this@WeatherPhotoFragment, Observer {
            layoutWeather.root.showB()
            this@WeatherPhotoFragment.bindWeather(it)
        })
    }


    private fun initComponents() = with(binding){
        ivCaptured.loadImage(uri)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        photoLayout.setOnClickListener {  }
    }

    private fun getArgs() {
        uri=arguments?.getParcelable(URI_PATH)
    }


    companion object{
        private const val URI_PATH="URI_PATH"
        fun newInstance(uri:Uri?)=WeatherPhotoFragment().apply {
            arguments=Bundle().apply {
                putParcelable(URI_PATH,uri)
            }
        }
    }

}