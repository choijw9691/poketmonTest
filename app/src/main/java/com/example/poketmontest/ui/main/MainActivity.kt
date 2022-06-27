package com.example.poketmontest.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.poketmontest.R
import com.example.poketmontest.databinding.ActivityMainBinding
import com.example.poketmontest.network.PokedexClient
import com.example.poketmontest.network.PokedexService
import com.example.poketmontest.repository.MainRepository
import com.example.poketmontest.ui.adapter.PokemonAdapter
import kotlinx.coroutines.CoroutineDispatcher


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        Log.d("JIWOUNG","start")

        viewModelFactory = MainViewModelFactory(MainRepository(application))
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]



        binding.apply {
            lifecycleOwner = this@MainActivity
            vm = viewModel
            adapter = PokemonAdapter()
        }
    }
}