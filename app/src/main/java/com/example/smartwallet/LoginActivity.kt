package com.example.smartwallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var loginET: EditText
    private lateinit var passwordET: EditText
    private lateinit var loginBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginET = findViewById(R.id.emailET)
        passwordET = findViewById(R.id.passwordET)
        loginBtn = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(
                loginET.text.toString().trim(),
                passwordET.text.toString().trim()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this@LoginActivity, ListActivity::class.java))
                } else {
                    auth.createUserWithEmailAndPassword(
                        loginET.text.toString().trim(),
                        passwordET.text.toString().trim()
                    )
                        .addOnFailureListener { e ->
                            e.printStackTrace()
                        }

                }
            }
        }
    }
}