package com.technical.authentication.di.module

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.technical.authentication.App
import com.technical.authentication.BuildConfig
import com.technical.authentication.data.ApiService
import com.technical.authentication.data.db.AppDatabase
import com.technical.authentication.data.db.UserDao
import com.technical.authentication.ui.viewmodels.ViewModelFactory
import com.technical.authentication.util.Utils
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * AppModule will provide app-wide dependencies for a part of the application.
 * It should initialize objects used across our application, such as Room database, Retrofit, Shared Preference, etc.
 */
@Module
class AppModule() {
    @Provides // Annotation informs Dagger compiler that this method is the constructor for the Context return type.
    @Singleton // Annotation informs Dagger compiler that the instance should be created only once in the entire lifecycle of the application.
    fun provideContext(app: App): Context = app // Using provide as a prefix is a common convention but not a requirement.

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        // We need to prepare a custom OkHttp client because need to use our custom call interceptor.
        // to be able to authenticate our requests.
        val builder = OkHttpClient.Builder()
        /*.connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)*/

        // Configure this client not to retry when a connectivity problem is encountered.
        builder.retryOnConnectionFailure(false)

        // Log requests and responses.
        // Add logging as the last interceptor, because this will also log the information which
        // you added or manipulated with previous interceptors to your request.
        builder.interceptors().add(HttpLoggingInterceptor())/*.apply {
            // For production environment to enhance apps performance we will be skipping any
            // logging operation. We will show logs just for debug builds.
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })*/
        return builder.build()
    }
    @Provides
    @Singleton
    fun provideApiService(httpClient: OkHttpClient): ApiService {
        return Retrofit.Builder() // Create retrofit builder.
            .baseUrl(BuildConfig.URL) // Base url for the api has to end with a slash.
            .addConverterFactory(GsonConverterFactory.create()) // Use GSON converter for JSON to POJO object mapping.
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient) // Here we set the custom OkHttp client we just created.
            .build()
            .create(ApiService::class.java) // We create an API using the interface we defined.
    }
    /*
   * provide room dependencies
   * */
    @Provides
    @Singleton
    fun provideDb(app: App): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, "user.db")
            // At current moment we don't want to provide migrations and specifically want database to be cleared when upgrade the version.
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao =
        db.userDao()

    @Provides
    @Singleton
    fun provideViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory = factory
}