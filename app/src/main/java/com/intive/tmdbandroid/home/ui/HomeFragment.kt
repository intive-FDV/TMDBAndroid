package com.intive.tmdbandroid.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.intive.tmdbandroid.common.state.Resource
import com.intive.tmdbandroid.databinding.FragmentHomeBinding
import com.intive.tmdbandroid.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        subscribeUi(binding)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sample()
    }


    private fun subscribeUi(binding: FragmentHomeBinding) {
        lifecycleScope.launchWhenStarted {
            viewModel.sampleFlow.collectLatest { sampleResource ->
                when (sampleResource.status) {
                    Resource.Status.SUCCESS -> {
                        binding.dummy.text = sampleResource.data?.dummy
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(context, sampleResource.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Status.LOADING -> {

                    }
                    Resource.Status.FAILURE -> {
                        Toast.makeText(context, sampleResource.message, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }

}
