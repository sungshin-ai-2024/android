package com.example.savewith_android

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.savewith_android.databinding.ActivityAppPermissionBinding

class AppPermissionActivity : AppCompatActivity(){
    private lateinit var binding: ActivityAppPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_guardian)

        binding = ActivityAppPermissionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.left.setOnClickListener { // 뒤로가기 시
            finish()
        }

        binding.swchLocation.setOnClickListener { // 위치서비스 스위치 클릭 시
            switchLocation()
        }
        binding.swchPhoto.setOnClickListener { // 사진 스위치 클릭 시
            switchPhoto()
        }
        binding.swchNotif.setOnClickListener { // 알림 스위치 클릭 시
            switchNotif()
        }
        binding.swchPhone.setOnClickListener { // 전화 스위치 클릭 시
            switchPhone()
        }
        binding.swchHealthapp.setOnClickListener { // 헬스앱 스위치 클릭 시
            switchHealth()
        }
    }

    private fun switchLocation(){

    }
    private fun switchPhoto(){

    }
    private fun switchNotif(){

    }
    private fun switchPhone(){

    }
    private fun switchHealth(){

    }
}