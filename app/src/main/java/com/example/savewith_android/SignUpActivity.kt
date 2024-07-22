package com.example.savewith_android
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup) // SignUpActivity 레이아웃 파일 경로

        // <---------- 여기는 내용 입력 받는 코드 추가 필요 --------->//

        val SignupButton: ImageButton = findViewById(R.id.signup_btn)
        SignupButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java) // 수정 필요
            startActivity(intent)
        }
    }
}