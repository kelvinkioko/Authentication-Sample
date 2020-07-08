package com.technical.authentication.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.technical.authentication.data.Repository
import com.technical.authentication.model.LoginBody
import com.technical.authentication.model.LoginResult
import com.technical.authentication.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val repository: Repository
):ViewModel() {
    var userResult: MutableLiveData<User> = MutableLiveData()
    var userError: MutableLiveData<String> = MutableLiveData()
    var userLoader: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var disposableObserver: DisposableObserver<User>

    fun userResult(): LiveData<User> {
        return userResult
    }

    fun userError(): LiveData<String> {
        return userError
    }

    fun userLoader(): LiveData<Boolean> {
        return userLoader
    }

    fun loadUser() {

        disposableObserver = object : DisposableObserver<User>() {
            override fun onComplete() {

            }
            override fun onError(e: Throwable) {
                userError.postValue(e.message)
                userLoader.postValue(false)
            }

            override fun onNext(t: User) {
                userResult.postValue(t)
                userLoader.postValue(false)
            }
        }

        repository.getUserFromDb()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    fun disposeElements(){
        if(null != disposableObserver && !disposableObserver.isDisposed) disposableObserver.dispose()
    }
}