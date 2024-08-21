package com.example.savewith_android

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.databinding.ActivityAddGuardianBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddGuardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddGuardianBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGuardianBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiService = ApiClient.createApiService(this)

        setupSpinner()
        setupClickListeners()
    }

    private fun setupSpinner() {
        val relationships = arrayOf("자녀", "간병인", "배우자", "형제", "손자녀", "기타")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, relationships)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRelationship.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.left.setOnClickListener { finish() }
        binding.addBtn.setOnClickListener { addGuardian() }
    }

    private fun addGuardian() {
        val name = binding.addName.text.toString().trim()
        val phoneNumber = binding.addPhoneNum.text.toString().trim()
        val relationship = binding.spinnerRelationship.selectedItem.toString()

        if (name.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val newGuardian = Guardian(
            id = null,
            name = name,
            phone_number = phoneNumber,
            relationship = relationship
        )

        val token = SharedPrefManager.getToken(this)
        if (token != null) {
            apiService.addGuardian("Bearer $token", newGuardian).enqueue(object : Callback<Guardian> {
                override fun onResponse(call: Call<Guardian>, response: Response<Guardian>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddGuardActivity, "보호자가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@AddGuardActivity, "보호자 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Guardian>, t: Throwable) {
                    Toast.makeText(this@AddGuardActivity, "네트워크 오류로 보호자 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun validateInput(name: String, phoneNumber: String): Boolean {
        if (name.isEmpty()) {
            binding.addName.error = "이름을 입력해주세요."
            return false
        }
        if (phoneNumber.isEmpty()) {
            binding.addPhoneNum.error = "전화번호를 입력해주세요."
            return false
        }
        if (!phoneNumber.matches(Regex("^\\d{10,11}$"))) {
            binding.addPhoneNum.error = "올바른 전화번호 형식이 아닙니다."
            return false
        }
        return true
    }
}