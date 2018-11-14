package com.example.protonapp

import com.android.base.BaseApplication
import com.example.protonapp.di.dropboxModule
import com.example.protonapp.di.repositoryModule
import com.example.protonapp.di.viewModelModule
import com.example.protonapp.utils.ReleaseTree
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import timber.log.Timber
import timber.log.Timber.DebugTree

class ProtonApplication : BaseApplication(), KodeinAware {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(DebugTree()) else Timber.plant(ReleaseTree())
        AndroidThreeTen.init(this)
        Stetho.initializeWithDefaults(this)
    }

    override val kodein = Kodein.lazy {
        extend(super.kodein)
        import(viewModelModule)
        import(repositoryModule)
        import(dropboxModule)
    }
}
