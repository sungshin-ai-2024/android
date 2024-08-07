package com.example.savewith_android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.savewith_android.databinding.ActivityLogin1Binding

class Login1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLogin1Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login1)

        binding = ActivityLogin1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 클릭 리스너 설정
        binding.txtSavewithLogin1.setOnClickListener { // 세이브위드 로그인 클릭 시
            val intent = Intent(this, Login2Activity::class.java)
            startActivity(intent)
        }

        binding.kakaoLogin1.setOnClickListener { // 카카오 계정 로그인
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.googleLogin1.setOnClickListener { // 구글 계정 로그인
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}