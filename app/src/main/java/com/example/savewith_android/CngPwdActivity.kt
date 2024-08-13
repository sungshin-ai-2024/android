package com.example.savewith_android


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import com.example.savewith_android.databinding.ActivityChgPwdBinding

class CngPwdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChgPwdBinding
    /*private val apiService: ApiService by lazy {
        RetrofitClient.apiService
    }*/
    private lateinit var apiService: ApiService

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChgPwdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로 가기
        binding.left.setOnClickListener { // 뒤로가기 시
            finish()
        }

        binding.editNumBtn.setOnClickListener { // 변경하기 클릭시
            val currentPwd = binding.boxOrigPwd.text.toString()
            val newPwd = binding.boxNewPwd.text.toString()
            val ckNewPwd = binding.boxCkNewPwd.text.toString()

            if (currentPwd.isNotEmpty() && newPwd.isNotEmpty() && ckNewPwd.isNotEmpty()) {
//                changePwd(userName, userPhoneNumber, userRelationship) // 데베에 비밀번호 변경 코드 추가
//                Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
//                finish()
                if (newPwd != ckNewPwd) {
                    Toast.makeText(this, "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // 비밀번호 변경 요청
                    lifecycleScope.launch {
                        try {
                            val token = getAuthToken()
                            val response = apiService.changePassword(
                                token = "Bearer $token",
                                passwordChangeRequest = PasswordChangeRequest(
                                    currentPassword = currentPwd,
                                    newPassword = newPwd
                                )
                            )

                            if (response.isSuccessful) {
                                response.body()?.let {
                                    if (it.success) {
                                        Toast.makeText(this@CngPwdActivity, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                                        finish()
                                    } else {
                                        Toast.makeText(this@CngPwdActivity, it.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(this@CngPwdActivity, "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: HttpException) {
                            Toast.makeText(this@CngPwdActivity, "서버 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@CngPwdActivity, "알 수 없는 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else if(currentPwd.isEmpty()) {
                Toast.makeText(this, "현재 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if(newPwd.isEmpty()) {
                Toast.makeText(this, "새 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if(ckNewPwd.isEmpty()) {
                Toast.makeText(this, "새 비밀번호 확인 칸을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cancelEditBtn.setOnClickListener {
            Toast.makeText(this, "비밀번호 변경을 취소합니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }
}