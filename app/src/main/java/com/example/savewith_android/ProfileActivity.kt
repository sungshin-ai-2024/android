package com.example.savewith_android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.databinding.ActivityProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiClient.createApiService(this)

        binding.left.setOnClickListener { // 뒤로가기 버튼 클릭 시
            finish()
        }

        binding.editInfo.setOnClickListener {
            val intent = Intent(this, EditInfoActivity::class.java)
            startActivity(intent)
        }

        binding.infoGuard.setOnClickListener {
            val intent = Intent(this, GuardInfoActivity::class.java)
            startActivity(intent)
        }

        loadUserData()
    }

    private fun loadUserData() {
        val token = SharedPrefManager.getToken(this)
        if (token != null) {
            apiService.getUserProfile().enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let { profileResponse ->
                            updateUIWithProfileData(profileResponse.signup_id, profileResponse.profile)
                        }
                    } else {
                        Toast.makeText(this@ProfileActivity, "Failed to load user data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(this@ProfileActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUIWithProfileData(signupId: String, profile: Profile) {
        binding.boxId.text = signupId                          // 아이디 설정
        binding.boxName.text = profile.signup_name             // 이름 설정
        binding.boxPhone.text = profile.phone_number           // 연락처 설정
        binding.boxBirth.text = profile.birth_date             // 생년월일 설정
        binding.boxSex.text = mapServerValueToGender(profile.sex)  // 성별 설정
        binding.box1Adrss.text = profile.zipcode               // 주소 설정 (우편번호)
        binding.box2Adrss.text = profile.address               // 주소 설정 (기본 주소)
        binding.box3Adrss.text = profile.detailed_address      // 주소 설정 (상세 주소)
    }

    private fun mapServerValueToGender(serverValue: String): String {
        return when (serverValue) {
            "남" -> "남성"
            "여" -> "여성"
            else -> ""
        }
    }
}
