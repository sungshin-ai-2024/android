package com.example.savewith_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.databinding.ActivitySettingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import androidx.activity.enableEdgeToEdge

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize apiService
        apiService = ApiClient.createApiService(this)

        if (ApiClient.isNetworkAvailable(this)) {
            loadUserInfo()
        } else {
            Toast.makeText(this, "네트워크 연결을 확인하세요.", Toast.LENGTH_SHORT).show()
        }

        // 뒤로 가기
        binding.left.setOnClickListener { // 뒤로가기 시
            finish()
        }

        // 일반
        binding.settTxtMeas.setOnClickListener { // 측정단위 클릭 시
            val intent = Intent(this, MeasureActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtNotif.setOnClickListener { // 알림설정 클릭 시
            val intent = Intent(this, NotifSettActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtDev.setOnClickListener { // 기기연결 클릭 시
            val intent = Intent(this, DeviceConnActivity::class.java)
            startActivity(intent)
        }

        // 개인정보
        binding.settTxtPp.setOnClickListener { // 개인정보 처리방침 클릭 시
            val intent = Intent(this, PrivacyActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtDelPersdata.setOnClickListener { // 개인데이터삭제 클릭 시
            val intent = Intent(this, DelPersActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtAppPerm.setOnClickListener { // 이앱이사용하는권한 클릭 시
            val intent = Intent(this, AppPermissionActivity::class.java)
            startActivity(intent)
        }
        binding.swchSensitiveInfo.setOnClickListener { // 민감정보 스위치 클릭 시
            switchSensitiveInfo()
        }
        binding.swchLoc.setOnClickListener { // 위치정보 스위치 클릭 시
            switchLocation()
        }
        binding.swchMarketing.setOnClickListener { // 마케팅동의 스위치 클릭 시
            switchMarketing()
        }

        // 계정설정
        binding.settTxtLogout.setOnClickListener { // 로그아웃 클릭 시
            handleLogout()
        }
        binding.settTxtProfile.setOnClickListener { // 프로필정보 클릭 시
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtGuardInfo.setOnClickListener { // 보호자정보 클릭 시
            val intent = Intent(this, GuardInfoActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtEditPersdata.setOnClickListener { // 개인정보수정 클릭 시
            val intent = Intent(this, EditInfoActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtEditGuard.setOnClickListener { // 보호자정보수정 클릭 시
            val intent = Intent(this, EditGuardActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtDelAccnt.setOnClickListener { // 회원탈퇴 클릭 시
            val intent = Intent(this, DelAcctActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadUserInfo() {
        val token = SharedPrefManager.getToken(this)
        if (token != null) {
            apiService.getUserProfile("Token $token").enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    if (response.isSuccessful) {
                        val profileResponse = response.body()
                        if (profileResponse != null) {
                            binding.sett1Box1Name.text = profileResponse.profile.signup_name
                            binding.sett1Box1PhoneNum.text = profileResponse.profile.phone_number
                        } else {
                            Log.e("SettingActivity", "Profile response is null")
                        }
                    } else {
                        Toast.makeText(this@SettingActivity, "서버 오류로 사용자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                        Log.e("SettingActivity", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(this@SettingActivity, "네트워크 오류로 사용자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("SettingActivity", "Failure: ${t.message}", t)
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun handleLogout() {
        SharedPrefManager.clearToken(this)
        val intent = Intent(this, Login2Activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    private fun switchSensitiveInfo() {
        // 민감 정보 스위치 로직 추가
    }

    private fun switchLocation() {
        // 위치 정보 스위치 로직 추가
    }

    private fun switchMarketing() {
        // 마케팅 동의 스위치 로직 추가
    }
}
