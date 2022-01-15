package com.isma3il.photoweatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.isma3il.core.base.BaseActivity
import com.isma3il.core.utils.replaceFragment
import com.isma3il.photoweatherapp.R
import com.isma3il.photoweatherapp.databinding.ActivityMainBinding
import com.isma3il.photoweatherapp.ui.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {



    override fun initLayout() {
       supportFragmentManager.replaceFragment(MainFragment.newInstance(),binding.mainContainer.id)
    }
}