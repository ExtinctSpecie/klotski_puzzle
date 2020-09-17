package com.skarra.klotski.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.skarra.klotski.presentation.viewmodel.CalculateSolutions
import com.skarra.klotski.presentation.viewmodel.MainActivityViewModel

private class Provider<T : ViewModel>(
    private val createViewModel: () -> T
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return createViewModel() as T
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainActivityViewModel =

            ViewModelProviders.of(this, Provider {
                MainActivityViewModel(CalculateSolutions())
            }).get(MainActivityViewModel::class.java)

        setContentView(
            MainActivityView()
                .inflate(layoutInflater, null, viewModel, this)
        )
    }
}