package com.example.savewith_android
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.RetrofitClient.apiService
import com.example.savewith_android.databinding.ActivityEditGuardianBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class EditGuardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditGuardianBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_guardian)

        binding = ActivityEditGuardianBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        binding.editBtn.setOnClickListener {
            val guardName = binding.boxName.text.toString()
            val phoneNum = binding.boxPhone.text.toString()
            val relationship = binding.spinnerRelationship.selectedItem.toString()
            val photoUrl = null // 사진 URL을 선택하는 로직 추가

            // 수정 완료하기 위해 휴대폰 인증을 확인하고 수정 완료하는 코드 추가
            if (validateInputs(guardName, phoneNum)) {
                val updateRequest = GuardianUpdateRequest(
                    name = guardName,
                    phone = phoneNum,
                    relation = relationship,
                    photoUrl = photoUrl
                )
                updateGuardianInfo(updateRequest)
            } else {
                Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNum.setOnClickListener {
            // 휴대폰 번호 인증 완료시
        }

    }

    private fun validateInputs(name: String, phone: String): Boolean {
        return name.isNotEmpty() && phone.isNotEmpty()
    }

    private fun updateGuardianInfo(updateRequest: GuardianUpdateRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = getAuthToken() ?: return@launch
                val response: Response<Void> = apiService.updateGuardianInfo("Bearer $token", updateRequest)

                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@EditGuardActivity, "정보가 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@EditGuardActivity, "정보 업데이트 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditGuardActivity, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }
}