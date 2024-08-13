package com.example.savewith_android
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.databinding.ActivityEditPhonenumBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class EditPhoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPhonenumBinding
    private lateinit var apiService: ApiService

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_phonenum)

        binding = ActivityEditPhonenumBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        binding.certifBtn.setOnClickListener {
            val phoneNum = binding.boxPhoneNum.text.toString()
            if(phoneNum.isNotEmpty()){
                sendVerificationCode(phoneNum)
            } else {
                Toast.makeText(this, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.editNumBtn.setOnClickListener {
            val certifNum = binding.boxCertificationNum.text.toString()
            if (certifNum.isNotEmpty()) {
                verifyCode(certifNum)
            } else {
                Toast.makeText(this, "인증번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun sendVerificationCode(phoneNum: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<Void> = apiService.sendVerificationCode(PhoneNumberRequest(phoneNum))
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@EditPhoneActivity, "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@EditPhoneActivity, "인증번호 전송 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditPhoneActivity, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun verifyCode(certifNum: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = getAuthToken() ?: return@launch
                val response: Response<Void> = apiService.verifyCode("Bearer $token", VerificationCodeRequest(certifNum))
                if (response.isSuccessful) {
                    runOnUiThread {
                        updatePhoneNumber()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@EditPhoneActivity, "인증번호 검증 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditPhoneActivity, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updatePhoneNumber() {
        val newPhoneNum = binding.boxPhoneNum.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = getAuthToken() ?: return@launch
                val response: Response<Void> = apiService.updatePhoneNumber("Bearer $token", PhoneNumberRequest(newPhoneNum))
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@EditPhoneActivity, "휴대폰 번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                        finish() // 업데이트 후 액티비티 종료
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@EditPhoneActivity, "휴대폰 번호 변경 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditPhoneActivity, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }
}