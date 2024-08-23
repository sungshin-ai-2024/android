package com.example.savewith_android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)  // 스플래시 화면 레이아웃 설정

        // 일정 시간이 지난 후 MainActivity로 전환
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Login2Activity::class.java)
            startActivity(intent)
            finish()  // 스플래시 화면을 종료하여 뒤로 가기 버튼을 누를 때 다시 나타나지 않도록 합니다.
        }, 2000)  // 3초 후에 MainActivity로 전환
    }
}