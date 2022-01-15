package com.isma3il.photoweatherapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.isma3il.core.base.BaseFragment
import com.isma3il.core.utils.secretB
import com.isma3il.core.utils.showB
import com.isma3il.photoweatherapp.R
import com.isma3il.photoweatherapp.databinding.FragmentMainBinding
import com.isma3il.photoweatherapp.ui.main.adapter.WeatherPhotoAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {


    private val viewModel by viewModels<MainViewModel>()

    private val photoAdapter by lazy {
        WeatherPhotoAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
        setupObservables()
        //fetch history
        viewModel.getHistory()
    }

    private fun setupObservables()= with(binding) {
        //loading
        viewModel.loadingLiveData.observe(this@MainFragment, Observer {
            if (it) progressBar.showB() else progressBar.secretB()
        })

        //error message
        viewModel.errorMessageLiveData.observe(this@MainFragment, Observer {
            Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
        })

        viewModel.historyLiveData.observe(this@MainFragment, Observer { data->
            if (data.isEmpty()){
                rvHistory.secretB()
                emptyView.showB()
            }else{
                rvHistory.showB()
                emptyView.secretB()

                photoAdapter.addData(data)
            }

        })

    }

    private fun initComponents() {
        binding.rvHistory.adapter = photoAdapter
    }


    companion object {
        fun newInstance() = MainFragment()
    }

}