package com.example.basicloginapp

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.widget.AppCompatButton
import android.widget.TextView
import com.example.basicloginapp.databaseHandler.databaseHandler
import android.os.Bundle
import com.example.basicloginapp.R
import androidx.appcompat.app.AppCompatDelegate
import com.example.basicloginapp.SharedPreference.SaveSharedPreference
import android.content.Intent
import android.util.Log
import android.view.View
import com.example.basicloginapp.HomeActivity
import android.widget.Toast
import com.example.basicloginapp.RegistrationActivity
import java.lang.Exception
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    var email: TextInputEditText? = null
    var password: TextInputEditText? = null
    var loginButton: AppCompatButton? = null
    var goToRegistration: TextView? = null
    var db: databaseHandler? = null
    private var userEmail: String? = null
    private var userPassword: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        email = findViewById(R.id.emailEditext)
        password = findViewById(R.id.passwordEdittext)
        loginButton = findViewById(R.id.loginButton)
        goToRegistration = findViewById(R.id.goToRegistration)
        if (SaveSharedPreference.getPrefUserEmail(this@LoginActivity)?.length != 0) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        loginButton!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                userEmail = email!!.getText().toString()
                userPassword = password!!.getText().toString()

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
                    Toast.makeText(
                        this@LoginActivity,
                        "please enter valid credentials !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            private fun emailVerification(userEmail: String): Boolean {
                return (!userEmail.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
            }

            private fun passwordVerification(userPassword: String): Boolean {
                val passwordPattern =
                    Pattern.compile("^" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{8,}" + "$")
                        .toString()
                if (userPassword.matches(passwordPattern.toRegex())) {
                    return true
                }
                password!!.setError("Password Not Valid !")
                return false
            }
        })
        goToRegistration!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        })
    }
}