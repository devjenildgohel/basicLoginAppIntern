package com.example.basicloginapp;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.basicloginapp.databaseHandler.databaseHandler;

import java.util.Objects;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    EditText name,email,password,re_typepassword;
    AppCompatButton registerButton;
    CheckBox passwordCheckBox;
    TransformationMethod checkbox;

    private String userName,userEmail,userPassword,userRe_TypePassword;
    databaseHandler dbhandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.nameEditext);
        email = findViewById(R.id.emailEditext);
        password = findViewById(R.id.passwordEdittext);
        re_typepassword = findViewById(R.id.reType_passwordEdittext);
        registerButton = findViewById(R.id.registerButton);
        passwordCheckBox = findViewById(R.id.showPasswordCheckbox);


        dbhandler =new databaseHandler(getApplicationContext());

        passwordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(!isChecked)
                {
                    checkbox = HideReturnsTransformationMethod.getInstance();
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    re_typepassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.getText().length());
                    re_typepassword.setSelection(re_typepassword.getText().length());
                }
                else {
                    checkbox = PasswordTransformationMethod.getInstance();
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    re_typepassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password.setSelection(password.getText().length());
                    re_typepassword.setSelection(re_typepassword.getText().length());
                }
            }

        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = name.getText().toString();
                userEmail = email.getText().toString();

                try {
                    if(editextisEmpty(name))
                    {
                        name.setError("Name Required !");
                    }
                    else if(editextisEmpty(email))
                    {
                        email.setError("email Required !");
                    }
                    else if(editextisEmpty(password))
                    {
                        password.setError("password Required !");
                    }
                    else if(editextisEmpty(re_typepassword))
                    {
                        re_typepassword.setError("retype password Required !");
                    }

                    else
                    {

                        if(emailVerification(userEmail) && passwordVerification(userPassword,userRe_TypePassword))
                        {
                            userRegisteration(userName,userEmail,userPassword);
                        }


                    }
                }

                catch (Exception e)
                {
                    Toast.makeText(RegistrationActivity.this, "Failed :"+e, Toast.LENGTH_SHORT).show();
                }

            }

            private boolean editextisEmpty(EditText etText) {
                return etText.getText().toString().trim().length() <= 0;
            }
        });


    }

    private void userRegisteration(String userName, String userEmail, String userPassword) {
            try {
                if (Objects.equals(dbhandler.userCheck(userName, userEmail, userPassword), "not found")) {
                    dbhandler.addNewUser(this.userName, this.userEmail, this.userPassword);
                    Toast.makeText(this, "Registration Successfully !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "user exists", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(this, "Error : " + e, Toast.LENGTH_LONG).show();
                Log.e("TAG", e.toString());
            }
        }



    private boolean emailVerification(String userEmail) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(userEmail.matches(emailPattern))
        {
            return true;
        }
        else
        {
            email.setError("Email Not Valid !");
        }

        return false;
    }

    private boolean passwordVerification(String userPassword, String userRe_TypePassword) {
        String passwordPattern = String.valueOf(Pattern.compile("^" + "(?=.*[@#$%^&+=])" +"(?=\\S+$)" + ".{8,}" + "$"));

        userPassword = password.getText().toString();
        userRe_TypePassword = re_typepassword.getText().toString();

        if(userPassword.matches(passwordPattern))
        {
            if(Objects.equals(userPassword, userRe_TypePassword))
            {
                return true;
            }
            else
            {
                re_typepassword.setError("password does not match !");
                return false;
            }
        }
        else
        {
            password.setError("password invalid !");
        }

       return false;
    }
}