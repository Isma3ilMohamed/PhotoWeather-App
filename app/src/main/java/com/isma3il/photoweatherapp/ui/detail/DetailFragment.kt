package com.isma3il.photoweatherapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.isma3il.core.base.BaseFragment
import com.isma3il.core.utils.loadImage
import com.isma3il.photoweatherapp.R
import com.isma3il.photoweatherapp.databinding.FragmentDetailBinding
import java.io.File


class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate){


    private var photoPath=""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()

    }



    private fun getArgs() {
        photoPath=arguments?.getString(PHOTO_PATH)?:""

        binding.ivWeatherPhoto.loadImage(photoPath)
    }


    companion object{
        private const val PHOTO_PATH="PHOTO_PATH"

        fun newInstance(photoPath:String)=DetailFragment().apply {
            arguments=Bundle().apply {
                putString(PHOTO_PATH,photoPath)
            }
        }
    }
}