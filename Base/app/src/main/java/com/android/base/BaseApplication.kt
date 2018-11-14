package com.android.base

import android.app.Activity
import android.app.Application
import com.android.base.view.login.BaseAuthActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule

open class BaseApplication : Application(), KodeinAware {

    open fun getLoginActivityClass(): Class<out Activity> = BaseAuthActivity::class.java

    override val kodein = Kodein.lazy {
        import(androidModule(this@BaseApplication))
    }
}
