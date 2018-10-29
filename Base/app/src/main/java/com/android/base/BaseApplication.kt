package com.android.base

import android.app.Activity
import android.app.Application
import com.android.base.view.login.BaseAuthActivity
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.android.autoAndroidModule
import com.github.salomonbrys.kodein.lazy

open class BaseApplication : Application(), KodeinAware {

    open fun getLoginActivityClass(): Class<out Activity> = BaseAuthActivity::class.java

    override val kodein by Kodein.lazy {
        import(autoAndroidModule(this@BaseApplication))
    }
}
