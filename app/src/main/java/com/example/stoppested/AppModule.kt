package com.example.stoppested

import com.example.stoppested.ui.screens.StoppestedViewModel
import com.example.stoppested.data.StoppestedRepository
import com.example.stoppested.location.LocationProvider
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { LocationProvider(context = get()) }
    single { StoppestedRepository() }
    viewModel { StoppestedViewModel(stoppRepository = get(), locationProvider = get()) }
}