package com.example.basicloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import com.example.basicloginapp.databaseHandler.databaseHandler;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    AppCompatButton loginButton;
    CheckBox passwordCheckBox;
    TextView goToRegistration;

    databaseHandler db;

    private String userEmail,userPassword;
    TransformationMethod checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        email = findViewById(R.id.emailEditext);
        password = findViewById(R.id.passwordEdittext);
        loginButton = findViewById(R.id.loginButton);
        passwordCheckBox = findViewById(R.id.showPasswordCheckbox);
        goToRegistration = findViewById(R.id.goToRegistration);

        passwordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(!isChecked)
                {
                    checkbox = HideReturnsTransformationMethod.getInstance();
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.getText().length());
                }
                else {
                    checkbox = PasswordTransformationMethod.getInstance();
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password.setSelection(password.getText().length());
                }
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userEmail = email.getText().toString();
                userPassword = password.getText().toString();
                boolean isExists = false;

                try
                {
                    isExists = db.checkUserExist(userEmail,userPassword);

                }
                catch (Exception e)
                {
                    Log.d("TAG" ,e.toString());
                }

                if(isExists)
                {
                    if(emailVerification(userEmail) && passwordVerification(userPassword))
                    {
                        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Toast.makeText(LoginActivity.this, "you can login now !", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "please enter valid credentials !", Toast.LENGTH_SHORT).show();
                    }

                }


            }




            private boolean emailVerification(String userEmail) {

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(userEmail.matches(emailPattern))
                {
                    return true;
                }
                email.setError("Email Not Valid !");
                return false;
            }

            private boolean passwordVerification(String userPassword) {
                String passwordPattern = String.valueOf(Pattern.compile("^" + "(?=.*[@#$%^&+=])" +"(?=\\S+$)" + ".{8,}" + "$"));

                if(userPassword.matches(passwordPattern))
                {
                    return true;
                }

                password.setError("Password Not Valid !");
                return false;
            }
        });

        goToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }
}