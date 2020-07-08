package com.technical.authentication.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.technical.authentication.data.Repository
import com.technical.authentication.model.LoginBody
import com.technical.authentication.model.LoginResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: Repository):ViewModel() {
    var loginResult: MutableLiveData<LoginResult> = MutableLiveData()
    var loginError: MutableLiveData<String> = MutableLiveData()
    var loginLoader: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var disposableObserver: DisposableObserver<LoginResult>

    fun loginResult(): LiveData<LoginResult> {
        return loginResult
    }

    fun loginError(): LiveData<String> {
        return loginError
    }

    fun loginLoader(): LiveData<Boolean> {
        return loginLoader
    }

    fun loadUser(loginBody: LoginBody) {

        disposableObserver = object : DisposableObserver<LoginResult>() {
            override fun onComplete() {

            }
            override fun onError(e: Throwable) {
                loginError.postValue(e.message)
                loginLoader.postValue(false)
            }

            override fun onNext(t: LoginResult) {
                loginResult.postValue(t)
                loginLoader.postValue(false)
            }
        }

        repository.loginUser(loginBody)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    fun disposeElements(){
        if(null != disposableObserver && !disposableObserver.isDisposed) disposableObserver.dispose()
    }
}