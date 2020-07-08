package com.technical.authentication

import android.app.Activity
import android.app.Application
import com.technical.authentication.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App: Application(), HasActivityInjector {
    @Inject // It implements Dagger machinery of finding appropriate injector factory for a type.
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        // Here we initialize Dagger. DaggerAppComponent is auto-generated from AppComponent.
        DaggerAppComponent.builder().application(this).build().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector
}