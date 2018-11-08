package com.example.protonapp

import com.android.base.BaseApplication
import com.example.protonapp.di.repositoryModule
import com.example.protonapp.di.viewModelModule
import com.example.protonapp.utils.ReleaseTree
import com.facebook.stetho.Stetho
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.lazy
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber
import timber.log.Timber.DebugTree


class ProtonApplication : BaseApplication(), KodeinAware {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(DebugTree()) else Timber.plant(ReleaseTree())
        AndroidThreeTen.init(this)
        Stetho.initializeWithDefaults(this)
    }

    override val kodein by Kodein.lazy {
        extend(super.kodein)
        import(viewModelModule)
        import(repositoryModule)
    }
}
