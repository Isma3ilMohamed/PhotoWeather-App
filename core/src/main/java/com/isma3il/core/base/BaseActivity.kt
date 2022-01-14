package com.isma3il.core.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) :AppCompatActivity() {


    private var _binding: VB? = null
    val binding: VB
        get() = _binding as VB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater(layoutInflater)

        if (_binding==null){
            throw IllegalArgumentException("Binding cannot be null")
        }

        setContentView(binding.root)



        initLayout()
    }

    abstract fun initLayout()


    override fun onDestroy() {
        super.onDestroy()
        _binding=null

    }


}