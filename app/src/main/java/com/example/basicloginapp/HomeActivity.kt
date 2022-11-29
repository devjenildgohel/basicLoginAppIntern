package com.example.basicloginapp

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import android.widget.TextView
import android.os.Bundle
import com.example.basicloginapp.R
import com.example.basicloginapp.SharedPreference.SaveSharedPreference
import android.widget.RelativeLayout
import com.google.android.material.snackbar.Snackbar
import android.widget.Toast
import android.content.Intent
import android.view.View
import com.example.basicloginapp.LoginActivity

class HomeActivity : AppCompatActivity() {
    var logoutButton: AppCompatButton? = null
    var userName: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userEmail = SaveSharedPreference.getPrefUserEmail(this)
        val relativeLayout = findViewById<RelativeLayout>(R.id.homeActivityRelativeLayout)

        userName = findViewById(R.id.userName)
        userName!!.setText(String.format("Current User : %s", userEmail))

        val snackbar =
            Snackbar.make(relativeLayout, "Welcome back :$userEmail", Snackbar.LENGTH_SHORT)
        snackbar.show()

        logoutButton = findViewById(R.id.logoutButton)

        logoutButton!!.setOnClickListener(View.OnClickListener { view: View? ->
            SaveSharedPreference.clearUserEmail(this@HomeActivity)
            Toast.makeText(this@HomeActivity, "See You Again !", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        })
    }
}