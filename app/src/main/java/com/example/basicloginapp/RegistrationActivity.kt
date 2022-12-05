package com.example.basicloginapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.example.basicloginapp.Room.AppDatabase
import com.example.basicloginapp.Room.UserDataClass
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class RegistrationActivity : AppCompatActivity() {
    private var nameLayout: TextInputLayout? = null
    private var emailLayout: TextInputLayout? = null
    private var passwordLayout: TextInputLayout? = null
    private var retypepasswordLayout: TextInputLayout? = null


    private var name: TextInputEditText? = null
    private var email: TextInputEditText? = null
    private var password: TextInputEditText? = null
    private var retypepassword: TextInputEditText? = null


    private var registerButton: AppCompatButton? = null
    private var userName: String? = null
    private var userEmail: String? = null
    private var userPassword: String? = null
    private var userReTypePassword: String? = null
    private var db: AppDatabase? = null


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


        editTextWatcher(name, nameLayout)
        emailEditTextWatcher(email, emailLayout)
        passwordEditTextWatcher(password, retypepassword, passwordLayout, retypepasswordLayout)

        db = AppDatabase.getDatabase(baseContext)

        registerButton!!.setOnClickListener {
            userName = name!!.text.toString()
            userEmail = email!!.text.toString()
            userPassword = password!!.text.toString()
            userReTypePassword = retypepassword!!.text.toString()

            if(userName!!.isEmpty())
            {
                nameLayout!!.error = "Name Required !"
            }
            if(userEmail!!.isEmpty())
            {
                emailLayout!!.error = "Email Required !"
            }
            if(userPassword!!.isEmpty())
            {
                passwordLayout!!.error = "Password Required !"
            }
            if(userReTypePassword!!.isEmpty())
            {
                retypepasswordLayout!!.error = "Required !"
            }

            val newUser = UserDataClass(null, userName!!, userEmail!!, userPassword!!)

            if (emailVerification(userEmail!!) && passwordVerification(userPassword!!)) {

                if (db!!.userDao().isUserExists(userEmail.toString())) {
                    emailLayout!!.error = "User Already Exists"
                }
                else {
                    try
                    {
                        db!!.userDao().insert(newUser)
                        val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        Toast.makeText(this, "Registration Successful !", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    }
                    catch (e : Exception)
                    {
                        Toast.makeText(this, "Ooops ! Something Went Wrong ! $e", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun emailEditTextWatcher(
        emailEditText: TextInputEditText?, emailLayout: TextInputLayout?
    ) {

        emailEditText?.doOnTextChanged { text, _, _, _ ->
            run {
                if (!emailVerification(text.toString())) emailLayout!!.error =
                    "Please Enter Valid Email"
                if (text!!.isEmpty()) emailLayout!!.error = "Email Required !"
            }
        }

        emailEditText?.doAfterTextChanged { editable: Editable? ->
            run {
                if (editable!!.isNotEmpty() && emailVerification(editable.toString())) {
                    emailLayout!!.error = null
                    emailLayout.clearFocus()
                }
            }
        }
    }

    private fun editTextWatcher(nameEditText: TextInputEditText?, nameLayout: TextInputLayout?) {
        nameEditText!!.doOnTextChanged { text, _, _, _ ->
            run {
                if (text!!.isEmpty()) nameLayout!!.error = "Name cannot be empty"
            }
        }

        nameEditText.doAfterTextChanged { editable: Editable? ->
            run {
                if (editable!!.isNotEmpty()) {
                    nameLayout!!.error = null
                    nameLayout.clearFocus()
                }
            }
        }
    }

    private fun passwordEditTextWatcher(
        passwordEditText: TextInputEditText?,
        retypePasswordEditText: TextInputEditText?,
        passwordLayout: TextInputLayout?,
        retypepasswordLayout: TextInputLayout?
    ) {
        passwordEditText!!.doOnTextChanged { text: CharSequence?, _: Int, _: Int, _: Int ->
            run {
                //password 8 char validation
                if (text!!.length < 8) passwordLayout!!.error =
                    "Password length must be greater than 8"
                else {
                    passwordLayout!!.error = null
                    passwordLayout.clearFocus()
                }

                //password RegEx validation
                if (text.length >= 8 && !passwordVerification(text.toString())) passwordLayout.error =
                    "Password must contains !@#$%^&*()?><:"

            }
        }

        retypePasswordEditText!!.doOnTextChanged { text: CharSequence?, _: Int, _: Int, _: Int ->
            run {
                //retypepassword == password validation
                if (text!!.trim() != passwordEditText.text!!.trim()) retypepasswordLayout!!.error =
                    "Password doesnot matched !"

                //remove error after validation checked

            }
        }

        passwordEditText.doAfterTextChanged { editable: Editable? ->
            run {

                //empty edittext validation
                if (editable!!.isEmpty()) passwordLayout!!.error = "Required"
                //remove error after validation checked
                if (editable.isNotEmpty() && editable.length > 8 && passwordVerification(editable.toString())) {
                    passwordLayout!!.error = null
                    passwordLayout.clearFocus()
                }


            }
        }

        retypePasswordEditText.doAfterTextChanged { editable: Editable? ->
            run {
                //empty edittext validation
                if (editable!!.isEmpty()) retypepasswordLayout!!.error = "Required"

                if (editable.isNotEmpty() && editable.length > 8 && passwordVerification(editable.toString()) && retypePasswordEditText.text!!.trim()
                        .toString() == passwordEditText.text!!.trim().toString()
                ) {
                    retypepasswordLayout!!.error = null
                    retypepasswordLayout.clearFocus()
                }
            }
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