package com.technical.authentication.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.technical.authentication.R
import com.technical.authentication.ui.viewmodels.LoginViewModel
import com.technical.authentication.ui.viewmodels.MainActivityViewModel
import com.technical.authentication.ui.viewmodels.ViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this)
        viewModel = ViewModelProvider(this,viewModelFactory)[MainActivityViewModel::class.java]

        viewModel.loadUser()

        viewModel.userResult().observe(this, Observer{
            tv_username.text = it.name
            tv_email.text = it.email
        })
        viewModel.userError().observe(this, Observer {
            Log.e("Login","Error: $it")
        })

    }
}
