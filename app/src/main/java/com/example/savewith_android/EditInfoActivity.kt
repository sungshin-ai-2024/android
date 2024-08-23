package com.example.savewith_android

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.savewith_android.databinding.ActivityEditInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class EditInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditInfoBinding
    private lateinit var apiService: ApiService
    private var newPassword: String? = null
    private var currentProfile: Profile? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Token 가져오기
        apiService = ApiClient.createApiService(this)


        loadUserData()
        binding.boxId.isEnabled = false
        setupTextChangeListeners()

        // 버튼 이벤트 설정
        binding.left.setOnClickListener {
            finish()
        }

        binding.birthBtn.setOnClickListener {
            showDatePickerDialog()
        }

        binding.editBtn.setOnClickListener {
            updateUserInfo()
        }

        binding.btnPw.setOnClickListener {
            val intent = Intent(this, CngPwdActivity::class.java)
            startActivity(intent)
        }

        binding.adrssBtn.setOnClickListener {
            showAddressSearch()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHANGE_PASSWORD && resultCode == RESULT_OK) {
            // 비밀번호 변경이 완료되면 새 비밀번호를 저장
            newPassword = data?.getStringExtra("NEW_PASSWORD")
            Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun loadUserData() {
        val token = SharedPrefManager.getToken(this)
        if (token != null) {
            apiService.getUserProfile().enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let { profileResponse ->
                            currentProfile = profileResponse.profile  // 현재 프로필 정보 저장
                            updateUIWithProfileData(profileResponse.profile)

                            // 아이디 표시
                            binding.boxId.text = profileResponse.signup_id
                        }
                    } else {
                        Toast.makeText(this@EditInfoActivity, "Failed to load user data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(this@EditInfoActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateUIWithProfileData(profile: Profile?) {
        profile?.let {
            binding.boxId.text = it.signup_id  // 아이디 설정
            binding.boxId.setTextColor(ContextCompat.getColor(this, R.color.gray2))
            binding.boxName.apply {
                setText(it.signup_name)
                setTextColor(ContextCompat.getColor(this@EditInfoActivity, R.color.gray2))
            }
            binding.boxPhone.apply {
                setText(it.phone_number)
                setTextColor(ContextCompat.getColor(this@EditInfoActivity, R.color.gray2))
            }
            binding.boxBirth.apply {
                text = it.birth_date
                setTextColor(ContextCompat.getColor(this@EditInfoActivity, R.color.gray2))
            }

            val genderArray = resources.getStringArray(R.array.choose_gender)
            val genderPosition = genderArray.indexOf(mapServerValueToGender(it.sex))
            if (genderPosition != -1) {
                binding.boxSex.setSelection(genderPosition)
            }

            binding.box1Adrss.apply {
                text = it.zipcode
                setTextColor(ContextCompat.getColor(this@EditInfoActivity, R.color.gray2))
            }
            binding.box2Adrss.apply {
                text = it.address
                setTextColor(ContextCompat.getColor(this@EditInfoActivity, R.color.gray2))
            }
            binding.box3Adrss.apply {
                setText(it.detailed_address)
                setTextColor(ContextCompat.getColor(this@EditInfoActivity, R.color.gray2))
            }
        }
    }
    private fun setupTextChangeListeners() {
        val blackColor = ContextCompat.getColor(this, R.color.font)

        binding.boxName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.boxName.setTextColor(blackColor)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.boxPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.boxPhone.setTextColor(blackColor)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.box3Adrss.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.box3Adrss.setTextColor(blackColor)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
    // 생년월일 선택기
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            binding.boxBirth.apply {
                text = selectedDate
                setTextColor(ContextCompat.getColor(this@EditInfoActivity, R.color.font))
            }
        }, year, month, day)

        datePickerDialog.show()
    }
    private fun mapGenderToServerValue(gender: String): String {
        return when (gender) {
            "남성" -> "남"
            "여성" -> "여"
            else -> ""
        }
    }

    private fun mapServerValueToGender(serverValue: String): String {
        return when (serverValue) {
            "남" -> "남성"
            "여" -> "여성"
            else -> ""
        }
    }
    private fun updateUserInfo() {
        val updatedProfile = Profile(
            signup_id = binding.boxId.text.toString(),
            signup_pw = "",  // 비밀번호 변경이 아니므로 빈 문자열
            signup_name = binding.boxName.text.toString().trim(),
            phone_number = binding.boxPhone.text.toString().trim(),
            birth_date = binding.boxBirth.text.toString().trim(),
            sex = mapGenderToServerValue(binding.boxSex.selectedItem.toString()),
            zipcode = binding.box1Adrss.text.toString().trim(),
            address = binding.box2Adrss.text.toString().trim(),
            detailed_address = binding.box3Adrss.text.toString().trim()
        )

        val profileUpdateRequest = ProfileUpdateRequest(profile = updatedProfile)

        apiService.updateUserProfile(profileUpdateRequest).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditInfoActivity, "프로필이 성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@EditInfoActivity, "프로필 수정 실패: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@EditInfoActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    companion object {
        private const val REQUEST_CHANGE_PASSWORD = 1
    }

    // 주소 검색 기능
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

    // WebView에서 JavaScript 인터페이스로 주소 설정
    inner class AndroidBridge {
        @JavascriptInterface
        fun setAddress(zonecode: String, address: String, buildingName: String) {
            runOnUiThread {
                binding.box1Adrss.apply {
                    text = zonecode
                    setTextColor(ContextCompat.getColor(this@EditInfoActivity, R.color.font))
                }
                binding.box2Adrss.apply {
                    text = address
                    setTextColor(ContextCompat.getColor(this@EditInfoActivity, R.color.font))
                }
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
