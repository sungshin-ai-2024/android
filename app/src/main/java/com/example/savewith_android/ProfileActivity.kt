package com.example.savewith_android
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.savewith_android.databinding.ActivityProfileBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val apiService by lazy {
        RetrofitClient.apiService
    }

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
        lifecycleScope.launch {
            try {
                // 토큰 가져오기
                val token = getAuthToken()
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
    }

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

    private fun updateUI(userData: UserData) {
        binding.boxName.text = userData.name
        binding.boxId.text = userData.id
        binding.boxPhone.text = userData.phone
        binding.boxBirth.text = userData.birth
        binding.boxSex.text = userData.gender
        binding.box1Adrss.text = userData.address1
        binding.box2Adrss.text = userData.address2
        binding.box3Adrss.text = userData.address3

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