package com.example.protonapp.di

import androidx.lifecycle.ViewModelProvider
import com.example.protonapp.viewmodel.ViewModelFactory
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton

val viewModelModule = Kodein.Module {
    bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(kodein) }
}
