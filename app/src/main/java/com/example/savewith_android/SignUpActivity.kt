package com.example.savewith_android
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.savewith_android.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var apiService: ApiService

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize apiService
        apiService = ApiClient.createApiService(this)

        // 생년월일 선택 버튼 클릭 시 DatePickerDialog 표시
        binding.birthBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.boxBirth.text = selectedDate
            }, year, month, day).show()
        }

        binding.signupBtn.setOnClickListener { // 회원가입 버튼 클릭 시
            val userId = binding.signupId.text.toString()
            val password = binding.signupPw.text.toString()

            val name = binding.signupName.text.toString()
            val phoneNumber = binding.signupPhoneNum.text.toString()
            val zipcode = binding.adrssBox1.text.toString()
            val address = binding.adrssBox2.text.toString()
            val detailedAddress = binding.detailAdrss.text.toString()
            val gender = when (binding.spinnerGender.selectedItem.toString()) {
                "남성" -> "남"
                "여성" -> "여"
                else -> ""
            }
            val birthDate = binding.boxBirth.text.toString()

            val profile = Profile(userId, password, name, phoneNumber, birthDate, gender, zipcode, address, detailedAddress)
            val signUpRequest = SignUpRequest(userId, password, profile)

            signUp(signUpRequest)
        }

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        binding.adrssBtn.setOnClickListener { // 주소검색 버튼 클릭 시
            showAddressSearch()
        }

        binding.chngPhotoBtn.setOnClickListener { // 사진추가 버튼 클릭 시
            // 사진첩에 접근하여 사진 등록하는 함수 코드 추가
        }
    }

    private fun signUp(signUpRequest: SignUpRequest) {
        apiService.signUp(signUpRequest).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    val signUpResponse = response.body()
                    if (signUpResponse != null) {
                        val token = signUpResponse.token

                        // Save the token using SharedPrefManager
                        SharedPrefManager.saveToken(this@SignUpActivity, token)

                        val name = binding.signupName.text.toString()
                        val phoneNumber = binding.signupPhoneNum.text.toString()
                        saveUserInfo(name, phoneNumber)
                        Toast.makeText(this@SignUpActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignUpActivity, Login2Activity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@SignUpActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "회원가입 실패: 서버 오류", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "네트워크 오류: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserInfo(name: String, phoneNumber: String) {
        val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("name", name)
            putString("phone_number", phoneNumber)
            apply()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showAddressSearch() {
        val webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.domStorageEnabled = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.allowUniversalAccessFromFileURLs = true // 추가된 설정
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(AndroidBridge(), "android")
        setContentView(webView)
        webView.loadUrl("file:///android_asset/address_search.html")
    }

    inner class AndroidBridge {
        @JavascriptInterface
        fun setAddress(zonecode: String, address: String, buildingName: String) {
            Log.d("SignUpActivity", "주소 선택됨: $zonecode, $address, $buildingName") // 디버깅용 로그
            runOnUiThread {
                binding.adrssBox1.text = zonecode
                binding.adrssBox2.text = address
                setContentView(binding.root)
            }
        }

        @JavascriptInterface
        fun closeAddressSearch() {
            runOnUiThread {
                setContentView(binding.root)
            }
        }
    }
}