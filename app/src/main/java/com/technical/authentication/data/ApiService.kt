package com.technical.authentication.data

import com.technical.authentication.model.LoginBody
import com.technical.authentication.model.LoginResult
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun login(@Body body: LoginBody): Observable<LoginResult>
}