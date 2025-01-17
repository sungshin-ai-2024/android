package com.example.savewith_android

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var apiService: ApiService

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize apiService
        apiService = ApiClient.createApiService(this)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

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

        binding.signupBtn.setOnClickListener {
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

            if (!validateInputs(name, userId, password, phoneNumber, birthDate, gender, zipcode, address, detailedAddress)) {
                return@setOnClickListener
            }

            val profile = Profile(userId, password, name, phoneNumber, birthDate, gender, zipcode, address, detailedAddress)
            val signUpRequest = SignUpRequest(userId, password, profile)

            signUp(signUpRequest)
        }

        binding.adrssBtn.setOnClickListener {
            showAddressSearch()
        }
    }

    private fun validateInputs(name: String, userId: String, password: String, phoneNumber: String,
                               birthDate: String, gender: String, zipcode: String, address: String, detailedAddress: String): Boolean {
        if (name.isEmpty() || userId.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() ||
            birthDate.isEmpty() || gender.isEmpty() || zipcode.isEmpty() || address.isEmpty()) {
            showToast("모든 필드를 입력해주세요.")
            return false
        }

        if (!isValidName(name)) {
            showToast("이름에는 한글과 영문만 사용할 수 있습니다.")
            return false
        }

        if (!isValidPassword(password)) {
            showToast("비밀번호는 최소 8자리이며, 영문, 숫자, 특수문자를 모두 포함해야 합니다.")
            return false
        }

        if (!isValidInput(userId) || !isValidInput(phoneNumber) || !isValidInput(zipcode) ) {
            showToast("입력 필드에 특수문자나 줄바꿈을 포함할 수 없습니다.")
            return false
        }

        return true
    }

    private fun isValidName(name: String): Boolean {
        return name.matches(Regex("^[가-힣a-zA-Z]+$"))
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$".toRegex()
        return passwordRegex.matches(password)
    }

    private fun isValidInput(input: String): Boolean {
        return input.matches(Regex("^[가-힣a-zA-Z0-9\\s]+$"))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
        webView.settings.allowUniversalAccessFromFileURLs = true
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(AndroidBridge(), "android")
        setContentView(webView)
        webView.loadUrl("file:///android_asset/address_search.html")
    }

    inner class AndroidBridge {
        @JavascriptInterface
        fun setAddress(zonecode: String, address: String, buildingName: String) {
            Log.d("SignUpActivity", "주소 선택됨: $zonecode, $address, $buildingName")
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