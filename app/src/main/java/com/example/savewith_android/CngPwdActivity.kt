package com.example.savewith_android

import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.databinding.ActivityChgPwdBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CngPwdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChgPwdBinding
    private lateinit var apiService: ApiService
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChgPwdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiClient.createApiService(this)
        token = SharedPrefManager.getToken(this) ?: ""

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        // 비밀번호 입력란을 별표로 표시
        binding.boxOrigPwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.boxNewPwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.boxCkNewPwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        // 안내 메시지 설정
        binding.boxOrigPwd.hint = "현재 비밀번호"
        binding.boxNewPwd.hint = "새 비밀번호 (최소 8자, 영문, 숫자, 특수문자 포함)"
        binding.boxCkNewPwd.hint = "새 비밀번호 확인"
    }

    private fun setupListeners() {
        // 뒤로 가기 버튼 클릭 이벤트
        binding.left.setOnClickListener {
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

        // 취소 버튼 클릭 이벤트
        binding.cancelEditBtn.setOnClickListener {
            Toast.makeText(this, "비밀번호 변경을 취소합니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun validateInputs(currentPwd: String, newPwd: String, ckNewPwd: String): Boolean {
        return when {
            currentPwd.isEmpty() -> {
                showToast("현재 비밀번호를 입력해주세요.")
                false
            }
            newPwd.isEmpty() -> {
                showToast("새 비밀번호를 입력해주세요.")
                false
            }
            ckNewPwd.isEmpty() -> {
                showToast("새 비밀번호 확인 칸을 입력해주세요.")
                false
            }
            newPwd != ckNewPwd -> {
                showToast("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.")
                false
            }
            !isValidPassword(newPwd) -> {
                showToast("비밀번호는 최소 8자리이며, 영문, 숫자, 특수문자를 모두 포함해야 합니다.")
                false
            }
            else -> true
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$".toRegex()
        return passwordRegex.matches(password)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun changePassword(currentPwd: String, newPwd: String) {
        val passwordChangeRequest = PasswordChangeRequest(currentPwd, newPwd)
        apiService.changePassword("Bearer $token", passwordChangeRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showToast("비밀번호가 성공적으로 변경되었습니다.")
                    finish() // 성공 시 화면 종료
                } else {
                    showToast("비밀번호 변경 실패: 서버 오류")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }
}