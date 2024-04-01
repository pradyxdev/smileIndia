/*
 * *
 *  * Created by Prady on 3/21/23, 6:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/21/23, 6:42 PM
 *
 */
package com.app.prady.appbase.helpers

import android.app.Application
import com.app.prady.appbase.data.remote.MyApi
import com.app.prady.appbase.data.remote.NetworkConnectionInterceptors
import com.app.prady.appbase.factories.AuthVMF
import com.app.prady.appbase.factories.SharedVMF
import com.app.prady.appbase.helpers.PreferenceManager.Companion.init
import com.app.prady.appbase.repositories.AuthRepo
import com.app.prady.appbase.repositories.SharedRepo
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AppController : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@AppController))
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { NetworkConnectionInterceptors(instance()) }

        bind() from singleton { SharedRepo(instance()) }
        bind() from provider { SharedVMF(instance()) }

        bind() from singleton { AuthRepo(instance()) }
        bind() from provider { AuthVMF(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        init(this)
    }

    companion object {
        var context: AppController? = null
            private set
    }
}