package com.example.stoppested

import com.example.stoppested.ui.screens.StoppestedViewModel
import com.example.stoppested.data.StoppestedRepository
import com.example.stoppested.location.LocationProvider
import com.example.stoppested.network.StoppestedApiService
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { LocationProvider(context = get()) }
    single { StoppestedApiService(stoppestedApolloClient = get())}
    single { StoppestedRepository(stoppestedApiService = get()) }
    viewModel { StoppestedViewModel(stoppRepository = get(), locationProvider = get()) }
}