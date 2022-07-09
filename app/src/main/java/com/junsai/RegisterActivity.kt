package com.junsai

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val email:EditText = findViewById(R.id.register_edittext_email)
        val password:EditText = findViewById(R.id.register_edittext_password)
        val register:Button = findViewById(R.id.register_btn_register)
        val alredyText:TextView = findViewById(R.id.register_tv_already)
        val btnPhoto:Button = findViewById(R.id.register_btn_photo)



        register.setOnClickListener {
            performRegister()

        }

        btnPhoto.setOnClickListener {
            Log.d("RegisterActivity", "photo")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        alredyText.setOnClickListener {
            Log.d("RegisterActivity", "Try to show login activity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val btnPhoto:Button = findViewById(R.id.register_btn_photo)
        val photoView:CircleImageView = findViewById(R.id.register_photo_imageview)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RegisterActivity", "photo was selected")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            photoView.setImageBitmap(bitmap)
            btnPhoto.alpha = 0f


//            val bitmapDrawable = BitmapDrawable(bitmap)
//            btnPhoto.setBackgroundDrawable(bitmapDrawable)

        }
    }

    private fun performRegister(){
        val email:EditText = findViewById(R.id.register_edittext_email)
        val password:EditText = findViewById(R.id.register_edittext_password)

        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email is :" + email.text.toString())
        Log.d("RegisterActivity", "Password:" + password.text.toString())
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("RegisterActivity", "Successfully created user with uid: ${it.result.user?.uid}")
                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener{
                Log.d("RegisterActivity", "Failed to  create user ${it.message}" )
                Toast.makeText(this, "Failed to create: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/${filename}")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Register", "path: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Register", "File Location : ${it}")

                    saveUserToFirebaseDB(it.toString())
                }
            }
            .addOnFailureListener{

            }
    }

    private fun saveUserToFirebaseDB(profileImageUrl:String) {
        val username:EditText = findViewById(R.id.register_edittext_username)
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")

        val user = User(uid, username.text.toString(), profileImageUrl )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register", "Finally we saved the user to Firebase Database")

            }
    }
}
class User(val uid:String, val username:String, val profileImageUrl: String)