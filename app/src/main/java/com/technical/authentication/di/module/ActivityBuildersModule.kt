package com.technical.authentication.di.module

import com.technical.authentication.ui.Login
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule{
    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): Login
}