package com.example.basicloginapp

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.widget.AppCompatButton
import com.example.basicloginapp.databaseHandler.databaseHandler
import android.os.Bundle
import android.content.Intent
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception
import java.util.regex.Pattern

class RegistrationActivity : AppCompatActivity() {
    var nameLayout: TextInputLayout? = null
    var emailLayout: TextInputLayout? = null
    var passwordLayout: TextInputLayout? = null
    var retypepasswordLayout: TextInputLayout? = null


    var name: TextInputEditText? = null
    var email: TextInputEditText? = null
    var password: TextInputEditText? = null
    var retypepassword: TextInputEditText? = null


    private var registerButton: AppCompatButton? = null
    private var userName: String? = null
    private var userEmail: String? = null
    private var userPassword: String? = null
    private var userReTypePassword: String? = null
    private var dbhandler: databaseHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        name = findViewById(R.id.nameEditext)
        email = findViewById(R.id.emailEditext)
        password = findViewById(R.id.passwordEdittext)
        retypepassword = findViewById(R.id.reType_passwordEdittext)
        registerButton = findViewById(R.id.registerButton)

        nameLayout = findViewById(R.id.nameTextInputLayout)
        emailLayout = findViewById(R.id.emailTextInputLayout)
        passwordLayout = findViewById(R.id.passwordTextInputLayout)
        retypepasswordLayout = findViewById(R.id.reType_passwordTextInputLayout)

        dbhandler = databaseHandler(applicationContext)

//        passwordEditTextWatcher(password, retypepassword, passwordLayout!!, retypepasswordLayout!!)

        registerButton!!.setOnClickListener {
            userName = name!!.text.toString()
            userEmail = email!!.text.toString()
            userPassword = password!!.text.toString()
            userReTypePassword = retypepassword!!.text.toString()



            if (emailVerification(userEmail!!) && passwordVerification(
                    userPassword!!
                )
            ) {
                userRegisteration(userName!!, userEmail!!, userPassword!!)
                val intent =
                    Intent(this@RegistrationActivity, LoginActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

//    private fun passwordEditTextWatcher(
//        ediText: TextInputEditText?,
//        editText2: TextInputEditText?,
//        passwordLayout: TextInputLayout?,
//        retypepasswordLayout: TextInputLayout?,
//    ) {
//        editTextLayout!!.error = null
//
//        ediText!!.doOnTextChanged { text: CharSequence?, i: Int, i1: Int, i2: Int ->
//            run {
//                editTextLayout.error = null
//                editTextLayout.clearFocus()
//                if (text!!.length < 8 && !passwordVerification(text.toString())) {
//                    editTextLayout.error =
//                        "Password must contain !@#$%^&*()?><: and 8 characters long"
//                }
//                if(this@RegistrationActivity.retypepassword)
//            }
//        }
//
//        ediText.doAfterTextChanged { editable: Editable? ->
//            run {
//                if (editable!!.isEmpty()) {
//                    editTextLayout.error = "Required"
//                }
//            }
//        }
//    }

    private fun userRegisteration(userName: String, userEmail: String, userPassword: String) {
        try {
            if (dbhandler!!.userCheck(userEmail) == "not found") {
                dbhandler!!.addNewUser(userName, userEmail, userPassword)
                Toast.makeText(this, "Registration Successfully !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "user exists", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error : $e", Toast.LENGTH_LONG).show()
            Log.e("TAG", e.toString())
        }
    }

    private fun emailVerification(userEmail: String): Boolean {
        return (userEmail.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail)
            .matches())
    }

    private fun passwordVerification(userPassword: String): Boolean {
        val passwordPattern =
            Pattern.compile("^" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{8,}" + "$").toString()
        if (userPassword.matches(passwordPattern.toRegex())) {
            return true
        }
        return false
    }

}