package com.junsai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email:EditText = findViewById(R.id.login_et_email)
        val password:EditText = findViewById(R.id.login_et_password)
        val btnLogin:Button = findViewById(R.id.login_btn_login)
        val backToRegistration:TextView = findViewById(R.id.login_tv_backtoregistration)

        btnLogin.setOnClickListener {
            Log.d("Login", "email: ${email.text.toString()}, password:${password.text.toString()}")
        }

        backToRegistration.setOnClickListener {
            finish()
        }

    }
}