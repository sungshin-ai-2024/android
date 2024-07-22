package com.example.savewith_android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.content.Intent
import android.widget.ImageButton

class Login2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login2)

        val signupButton: ImageButton = findViewById(R.id.signup)

        signupButton.setOnClickListener {
            val intent = Intent(this, CkAccessActivity::class.java)
            startActivity(intent)
        }
    }
}