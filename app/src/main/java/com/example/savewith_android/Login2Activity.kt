package com.example.savewith_android

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.content.Intent
import android.os.Build
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.example.savewith_android.databinding.ActivityLogin2Binding

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.savewith_android.ApiService
import com.example.savewith_android.RetrofitClient

class Login2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLogin2Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        binding = ActivityLogin2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.signup.setOnClickListener { // 로그인 버튼 클릭 시
            val intent = Intent(this, CkAccessActivity::class.java)
            startActivity(intent)
        }
        binding.login2Btn.setOnClickListener { // 회원가입 버튼 클릭 시
            val userId = binding.login2Id.text.toString()
            val password = binding.login2Pw.text.toString()

            if (userId.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(userId, password)
                RetrofitClient.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            val token = response.body()?.token ?: ""
                            saveToken(token)  // SharedPreferences에 토큰 저장
                            navigateToMain()
                        } else {
                            Toast.makeText(this@Login2Activity, "로그인 실패, 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        // 오류 처리
                        Toast.makeText(this@Login2Activity, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveToken(token: String) {
        // SharedPreferences 또는 다른 저장소에 토큰 저장
//        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
//        sharedPreferences.edit().putString("user_token", token).apply()
        val sharedPreferences = MyApplication.appContext.getSharedPreferences("AppPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun login(userId: String, password: String) {
        val apiService = RetrofitClient.apiService
        val loginRequest = LoginRequest(userId, password)

        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        val token = loginResponse.token // 토큰
                        val intent = Intent(this@Login2Activity, MainActivity::class.java)
                        intent.putExtra("TOKEN", token)
                        startActivity(intent)
                        finish() // 현재 액티비티 종료
                    } else {
                        Toast.makeText(this@Login2Activity, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Login2Activity, "로그인 실패: 서버 오류", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@Login2Activity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}