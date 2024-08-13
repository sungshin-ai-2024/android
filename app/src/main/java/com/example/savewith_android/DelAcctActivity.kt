package com.example.savewith_android

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.databinding.ActivityDelAcctBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import androidx.lifecycle.lifecycleScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DelAcctActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDelAcctBinding
    private lateinit var apiService: ApiService

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDelAcctBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        binding.delAcctBtn.setOnClickListener {
            val cbDelAcct = binding.cbDelAcct.isChecked
            val token = SharedPrefManager.getToken(this)

            if(cbDelAcct){
                deleteUser()
                /*
                lifecycleScope.launch {
                    try {
                        val token = getAuthToken()
                        val response = apiService.deleteUser(
                            token = "Bearer $token"
                        )

                        if (response.isSuccessful) {
                            Toast.makeText(this@DelAcctActivity, "계정이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            // 로그아웃 처리 및 앱 종료 등의 작업을 수행할 수 있습니다.
                            finish()
                        } else {
                            Toast.makeText(this@DelAcctActivity, "계정 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: HttpException) {
                        Toast.makeText(this@DelAcctActivity, "서버 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@DelAcctActivity, "알 수 없는 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }*/
            } else {
                // 체크박스가 체크되지 않은 경우 경고 메시지 표시
                Toast.makeText(this, "탈퇴 안내사항에 동의해야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }

    private fun deleteUser() {
        val token = SharedPrefManager.getToken(this)
        if (token != null) {
            apiService.deleteUser("Token $token").enqueue(object : Callback<DeleteResponse> {
                override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DelAcctActivity, "계정이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        // 로그아웃 처리 및 앱 종료 등의 작업을 수행합니다.
                        performLogoutAndExit()
                    } else {
                        Toast.makeText(this@DelAcctActivity, "계정 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.e("DelAcctActivity", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                    Toast.makeText(this@DelAcctActivity, "네트워크 오류로 계정 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("DelAcctActivity", "Failure: ${t.message}", t)
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogoutAndExit() {
        SharedPrefManager.clearToken(this)

        // 로그아웃 처리를 수행하고 앱을 종료하는 등의 작업을 여기에 추가합니다.

        finishAffinity() // 앱을 완전히 종료합니다.
    }
}