package com.example.savewith_android

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.databinding.ActivityProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var apiService: ApiService

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        // 유저 DB에서 프로필 내용 불러와서 텍스트에 저장하는 코드 구현 //
        loadProfileData()

        binding.editInfo.setOnClickListener {
            val intent = Intent(this, EditInfoActivity::class.java)
            startActivity(intent)
        }
        binding.infoGuard.setOnClickListener {
            val intent = Intent(this, GuardInfoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadProfileData() {
        val token = SharedPrefManager.getToken(this) // SharedPrefManager를 사용해 토큰을 가져옴
        if (token != null) {
            apiService.getUserProfile("Bearer $token").enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    if (response.isSuccessful) {
//                        val userData = response.body()
//                        if (userData != null) {
//                            updateUI(userData)
//                        }
                        val profileResponse = response.body()
                        if (profileResponse != null) {
                            val userProfile = profileResponse.profile // ProfileResponse에서 Profile 객체를 가져옴
                            updateUI(userProfile)
                        }else {
                            Toast.makeText(this@ProfileActivity, "유저 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
                            Log.e("ProfileActivity", "User data is null")
                        }
                    } else {
                        Toast.makeText(this@ProfileActivity, "프로필 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.e("ProfileActivity", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(this@ProfileActivity, "네트워크 오류로 프로필 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("ProfileActivity", "Failure: ${t.message}", t)
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    /*
    private fun loadProfileData() {
        lifecycleScope.launch {
            try {
                // 토큰 가져오기
                val token = SharedPrefManager.getToken(this)
//                val token = getAuthToken()
                if (token != null) {
                    // API 호출하여 유저 정보 가져오기
                    val response: Response<UserData> = apiService.getUserData("Bearer $token")
                    if (response.isSuccessful) {
                        response.body()?.let { userData ->
                            updateUI(userData)
                        } ?: run {
                            Toast.makeText(this@ProfileActivity, "유저 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ProfileActivity, "프로필 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ProfileActivity, "인증 토큰이 없습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                Toast.makeText(this@ProfileActivity, "서버 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }*/

//    private fun loadProfileData() {
//        lifecycleScope.launch {
//            try {
//                // API 호출하여 유저 정보 가져오기
//                val response: Response<UserData> = apiService.getUserData() // API 호출 시 인증 토큰이 필요할 경우, 헤더에 추가
//                if (response.isSuccessful) {
//                    response.body()?.let { userData ->
//                        updateUI(userData)
//                    } ?: run {
//                        Toast.makeText(this@ProfileActivity, "유저 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(this@ProfileActivity, "프로필 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
//                }
//            } catch (e: Exception) {
//                Toast.makeText(this@ProfileActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun updateUI(userData: Profile) {
        binding.boxName.text = userData.signup_name
        binding.boxId.text = userData.id
        binding.boxPhone.text = userData.phone_number
        binding.boxBirth.text = userData.birth_date
        binding.boxSex.text = userData.gender
        binding.box1Adrss.text = userData.zipcode
        binding.box2Adrss.text = userData.address
        binding.detailAdrss.text = userData.detailed_address

        // 프로필 사진 로딩 (Glide 또는 Picasso와 같은 라이브러리를 사용할 수 있음)
//        userData.photoUrl?.let {
//            Glide.with(this)
//                .load(it)
//                .placeholder(R.drawable.placeholder_image) // 이미지 로딩 전 placeholder
//                .into(binding.proflUserIc)
//        } ?: run {
//            binding.proflUserIc.setImageResource(R.drawable.default_profile_image) // 기본 이미지
//        }

        // Load profile photo using Glide or any image loading library
//        Glide.with(this)
//            .load(userData.photoUrl)
//            .into(binding.proflUserIc)
    }
}