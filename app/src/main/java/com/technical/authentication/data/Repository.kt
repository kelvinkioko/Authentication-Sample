package com.technical.authentication.data

import android.util.Log
import com.technical.authentication.data.db.UserDao
import com.technical.authentication.model.LoginBody
import com.technical.authentication.model.LoginResult
import com.technical.authentication.model.User
import com.technical.authentication.util.Utils
import io.reactivex.Observable
import javax.inject.Inject

class Repository @Inject constructor(
    val apiService: ApiService,
    val userDao: UserDao
) {
    /*fun getUser(userBody: LoginBody): Observable<out Any> {
        val utils = Utils()
        val hasConnection = utils.isConnectedToInternet()
        var observableFromApi: Observable<LoginResult>? = null
        if (hasConnection){
            observableFromApi = getUserFromApi(userBody)
        }
        val observableFromDb = getUserFromDb()

        return if (hasConnection) Observable.concatArrayEager(observableFromApi, observableFromDb)
        else observableFromDb
    }*/
    fun loginUser(loginBody: LoginBody): Observable<LoginResult> {
        return apiService.login(loginBody)
            .doOnNext {
                Log.e("REPOSITORY API * ", it.toString())
                userDao.insertUser(it.data.user)
            }
    }

    fun getUserFromDb(): Observable<User> {
        return userDao.getUser()
            .toObservable()
            .doOnNext {
                //Print log it.size :)
                Log.e("REPOSITORY DB *** ", it.toString())
            }
    }
}