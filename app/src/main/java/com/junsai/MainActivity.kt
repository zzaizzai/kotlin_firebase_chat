package com.junsai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email:EditText = findViewById(R.id.register_edittext_email)
        val password:EditText = findViewById(R.id.register_edittext_password)
        val register:Button = findViewById(R.id.register_btn_register)
        var alredy_text:TextView = findViewById(R.id.register_tv_already)


        register.setOnClickListener {
            performRegister()



        }

        alredy_text.setOnClickListener {
            Log.d("MainActivity", "Try to show login activity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }



    }

    private fun performRegister(){
        val email:EditText = findViewById(R.id.register_edittext_email)
        val password:EditText = findViewById(R.id.register_edittext_password)

        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email is :" + email.text.toString())
        Log.d("MainActivity", "Password:" + password.text.toString())
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Main", "Successfully created user with uid: ${it.result.user?.uid}")
            }
            .addOnFailureListener{
                Log.d("Main", "Failed to  create user ${it.message}" )
                Toast.makeText(this, "Failed to create: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}