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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CngPwdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChgPwdBinding
    private lateinit var apiService: ApiService
    private lateinit var token: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChgPwdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로 가기
        binding.left.setOnClickListener { // 뒤로가기 시
            finish()
        }

        // 비밀번호 변경 버튼 클릭 이벤트
        binding.editNumBtn.setOnClickListener {
            val currentPwd = binding.boxOrigPwd.text.toString().trim()
            val newPwd = binding.boxNewPwd.text.toString().trim()
            val ckNewPwd = binding.boxCkNewPwd.text.toString().trim()

            if (validateInputs(currentPwd, newPwd, ckNewPwd)) {
                changePassword(currentPwd, newPwd)
            }
        }

        binding.cancelEditBtn.setOnClickListener {
            Toast.makeText(this, "비밀번호 변경을 취소합니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun validateInputs(currentPwd: String, newPwd: String, ckNewPwd: String): Boolean {
        return when {
            currentPwd.isEmpty() -> {
                Toast.makeText(this, "현재 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                false
            }
            newPwd.isEmpty() -> {
                Toast.makeText(this, "새 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                false
            }
            ckNewPwd.isEmpty() -> {
                Toast.makeText(this, "새 비밀번호 확인 칸을 입력해주세요.", Toast.LENGTH_SHORT).show()
                false
            }
            newPwd != ckNewPwd -> {
                Toast.makeText(this, "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun changePassword(currentPwd: String, newPwd: String) {
        val passwordChangeRequest = PasswordChangeRequest(currentPwd, newPwd)
        apiService.changePassword("Bearer $token", passwordChangeRequest).enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CngPwdActivity, "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    finish() // 성공 시 화면 종료
                } else {
                    Toast.makeText(this@CngPwdActivity, "비밀번호 변경 실패: 서버 오류", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CngPwdActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}