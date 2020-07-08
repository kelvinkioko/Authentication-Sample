package com.technical.authentication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.technical.authentication.R
import com.technical.authentication.model.LoginBody
import com.technical.authentication.ui.viewmodels.LoginViewModel
import com.technical.authentication.ui.viewmodels.ViewModelFactory
import com.technical.authentication.util.Utils
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class Login : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var loginViewModel: LoginViewModel
    var isLoginClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //inject the activity
        AndroidInjection.inject(this)
        setupValidationViews()
        loginViewModel = ViewModelProvider(this,viewModelFactory)[LoginViewModel::class.java]
        hideDialog()
    }

    fun setupValidationViews() {
        // Username Views
        val usernameInputValidation = findViewById<View>(R.id.username_input_validation) as TextView
        usernameInputValidation.setVisibility(View.GONE)
        val editTextUsername = findViewById<View>(R.id.editTextUsername) as EditText

        // Password Views
        val passwordInputValidation = findViewById<View>(R.id.password_input_validation) as TextView
        passwordInputValidation.setVisibility(View.GONE)
        val editTextPassword = findViewById<View>(R.id.editTextPassword) as EditText

        // Check when button has just been clicked username
        if (isLoginClicked && editTextUsername.text.isEmpty()) {
            usernameInputValidation.setVisibility(View.VISIBLE)
            usernameInputValidation.setText("Enter Username")
        } else {
            usernameInputValidation.setVisibility(View.GONE)
        }

        // Check when button has just been clicked password
        if (isLoginClicked && editTextPassword.text.isEmpty()) {
            passwordInputValidation.setVisibility(View.VISIBLE)
            passwordInputValidation.setText("Enter Password")
        } else {
            passwordInputValidation.setVisibility(View.GONE)
        }

        // addTextChangedListener for the username
        editTextUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun afterTextChanged(editable: Editable) {
                if (inputValidation(editable)) {
                    usernameInputValidation.setVisibility(View.VISIBLE)
                    usernameInputValidation.setText("Enter Username")
                } else {
                    usernameInputValidation.setVisibility(View.GONE)
                }
            }
        })

        // addTextChangedListener for the password
        editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun afterTextChanged(editable: Editable) {
                if (inputValidation(editable)) {
                    passwordInputValidation.setVisibility(View.VISIBLE)
                    passwordInputValidation.setText("Enter Password")
                } else {
                    passwordInputValidation.setVisibility(View.GONE)
                }
            }
        })
    }

    fun inputValidation(inputText: Editable): Boolean {
        return inputText.toString().isEmpty()
    }

    fun loginAction(view: View) {
        val isConnected = Utils(this).isConnectedToInternet()
        if (isConnected){
            isLoginClicked = true
            setupValidationViews()
            showDialog()
            if(editTextUsername.text.isEmpty() || editTextPassword.text.isEmpty() ){
                longToast("All Fields Are Required!!")
                return
            }else{
                val loginBody = LoginBody(editTextUsername.text.toString(),
                    editTextPassword.text.toString())
                loginUser(loginBody)
                loginViewModel.loginResult().observe(this, Observer{
                    if(it.success){
                        startActivity<MainActivity>()
                        finish()
                    }
                })
                loginViewModel.loginError().observe(this, Observer {
                    Log.e("Login","Error: $it")
                })
            }
        }else{
            longToast("You are not connected to the internet")
        }


    }
    fun loginUser(loginBody: LoginBody){
        loginViewModel.loadUser(loginBody)
    }
    private fun showDialog() {
        loginProgressBar!!.setVisibility(View.VISIBLE);
    }

    private fun hideDialog() {
        if (loginProgressBar!!.getVisibility() == View.VISIBLE) {
            loginProgressBar!!.setVisibility(View.INVISIBLE);
        }
    }
}