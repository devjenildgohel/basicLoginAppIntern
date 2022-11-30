package com.example.basicloginapp

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.widget.AppCompatButton
import android.widget.TextView
import com.example.basicloginapp.databaseHandler.databaseHandler
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.basicloginapp.SharedPreference.SaveSharedPreference
import android.content.Intent
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private var email: TextInputEditText? = null
    private var password: TextInputEditText? = null
    private var emailLayout: TextInputLayout? = null
    private var passwordLayout: TextInputLayout? = null
    private var loginButton: AppCompatButton? = null
    private var goToRegistration: TextView? = null
    private var db: databaseHandler? = null
    private var userEmail: String? = null
    private var userPassword: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        email = findViewById(R.id.emailEditext)
        emailLayout = findViewById(R.id.emailTextInputLayout)
        password = findViewById(R.id.passwordEdittext)
        passwordLayout = findViewById(R.id.passwordTextInputLayout)
        loginButton = findViewById(R.id.loginButton)
        goToRegistration = findViewById(R.id.goToRegistration)

        emailEditTextWatcher(email,emailLayout)
        passwordEditTextWatcher(password,passwordLayout)

        if (SaveSharedPreference.getPrefUserEmail(this@LoginActivity)?.length != 0) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        loginButton!!.setOnClickListener {
            userEmail = email!!.text.toString()
            userPassword = password!!.text.toString()

            db = databaseHandler(applicationContext)

            var isExists = false
            try {
                isExists = db!!.isUserExist(userEmail!!, userPassword!!)
            } catch (e: Exception) {
                Log.d("TAG", e.toString())
            }
            if (isExists) {
                if (emailVerification(userEmail!!) && passwordVerification(userPassword!!)) {
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    Toast.makeText(
                        this@LoginActivity,
                        "you can login now !",
                        Toast.LENGTH_SHORT
                    ).show()
                    SaveSharedPreference.setPrefUserEmail(baseContext, userEmail)
                    startActivity(intent)
                }
            } else {
            emailLayout!!.error = "User Not Found !!"
            }
        }
        goToRegistration!!.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun passwordEditTextWatcher(passwordEditText: TextInputEditText?, passwordLayout: TextInputLayout?) {
        passwordEditText!!.doOnTextChanged { text: CharSequence?, _: Int, _: Int, _: Int ->
            run {
                //password 8 char validation
                if (text!!.length < 8 && text.isNotEmpty())
                    passwordLayout!!.error = "Password length must be greater than 8"
                else
                {
                    passwordLayout!!.error = null
                    passwordLayout.clearFocus()
                }

                //password RegEx validation
                if (text.length >= 8 && !passwordVerification(text.toString()))
                    passwordLayout.error = "Password must contains !@#$%^&*()?><:"

            }
        }
        
        passwordEditText.doAfterTextChanged { editable: Editable? ->
            run{
                if(editable!!.isNotEmpty() && emailVerification(editable.toString()))
                {
                    passwordLayout!!.error = null
                    passwordLayout.clearFocus()
                }
            }
        }
    }

    private fun emailEditTextWatcher(emailEditText: TextInputEditText?, emailLayout: TextInputLayout?) {
        emailEditText!!.doOnTextChanged { text, _, _, _ ->
            run{
                if(text!!.isNotEmpty() && !emailVerification(text.toString()))
                    emailLayout!!.error = "Please Enter Valid Email"
                else
                {
                    emailLayout!!.error = null
                    emailLayout.clearFocus()
                }

            }
        }

        emailEditText.doAfterTextChanged { editable: Editable? ->
            run{
                if(editable!!.isNotEmpty() && emailVerification(editable.toString()))
                {
                    emailLayout!!.error = null
                    emailLayout.clearFocus()
                }
            }
        }
    }

    private fun emailVerification(userEmail: String): Boolean {
        return (userEmail.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
    }

    private fun passwordVerification(userPassword: String): Boolean {
        val passwordPattern =
            Pattern.compile("^" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{8,}" + "$")
                .toString()
        if (userPassword.matches(passwordPattern.toRegex())) {
            return true
        }
        password!!.error = "Password Not Valid !"
        return false
    }
}