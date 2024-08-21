package com.example.savewith_android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.databinding.ActivityDelAcctBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ProtocolException

class DelAcctActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDelAcctBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDelAcctBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiClient.createApiService(this)

        binding.left.setOnClickListener {
            finish()
        }

        binding.delAcctBtn.setOnClickListener {
            if (binding.cbDelAcct.isChecked) {
                showConfirmationDialog()
            } else {
                Toast.makeText(this, "탈퇴 안내사항에 동의해야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("회원 탈퇴")
            .setMessage("정말로 탈퇴하시겠습니까? 이 작업은 취소할 수 없습니다.")
            .setPositiveButton("탈퇴") { _, _ ->
                deleteAccount()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun deleteAccount() {
        val token = SharedPrefManager.getToken(this)
        if (token != null) {
            apiService.deleteAccount("Bearer $token").enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful || response.code() == 204) {
                        Toast.makeText(this@DelAcctActivity, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        SharedPrefManager.clearAllUserData(this@DelAcctActivity)
                        navigateToLogin()
                    } else {
                        Toast.makeText(this@DelAcctActivity, "회원 탈퇴에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    if (t is ProtocolException && t.message?.contains("HTTP 204") == true) {
                        Toast.makeText(this@DelAcctActivity, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        SharedPrefManager.clearAllUserData(this@DelAcctActivity)
                        navigateToLogin()
                    } else {
                        Toast.makeText(this@DelAcctActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            Toast.makeText(this, "로그인 정보가 없습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, Login2Activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}