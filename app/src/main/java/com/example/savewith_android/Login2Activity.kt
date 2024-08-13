package com.example.savewith_android

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.savewith_android.databinding.ActivityLogin2Binding

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login2Activity : AppCompatActivity() {
    private var binding: ActivityLogin2Binding? = null
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // Initialize apiService
        apiService = ApiClient.createApiService(this)

        binding!!.signup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 아이디 입력란에서 엔터 키를 누르면 비밀번호 입력란으로 포커스 이동
        binding!!.login2Id.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                binding!!.login2Pw.requestFocus()
                true
            } else {
                false
            }
        }

        // 비밀번호 입력란에서 엔터 키를 누르면 로그인 로직 실행
        binding!!.login2Pw.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                handleLogin()
                true
            } else {
                false
            }
        }

        binding!!.login2Btn.setOnClickListener {
            handleLogin()
        }
    }

    private fun handleLogin() {
        val userId = binding!!.login2Id.text.toString()
        val password = binding!!.login2Pw.text.toString()
        if (userId.isNotEmpty() && password.isNotEmpty()) {
            login(userId, password)
        } else {
            Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun login(userId: String, password: String) {
        val loginRequest = LoginRequest(userId, password)

        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        val token = loginResponse.token // 토큰
                        val userName = loginResponse.userName // 사용자 이름
                        val userPhone = loginResponse.userPhone // 사용자 전화번호

                        // Save the token using SharedPrefManager
                        SharedPrefManager.saveToken(this@Login2Activity, token)

                        val intent = Intent(this@Login2Activity, MainActivity::class.java)
                        intent.putExtra("TOKEN", token)
                        intent.putExtra("USER_NAME", userName)
                        intent.putExtra("USER_PHONE", userPhone)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@Login2Activity, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Login2Activity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@Login2Activity, "네트워크 오류: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

/*
class Login2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLogin2Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    Toast.makeText(this@Login2Activity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@Login2Activity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

 */