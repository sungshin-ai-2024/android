package com.example.savewith_android

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.databinding.ActivityEditGuardianBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditGuardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditGuardianBinding
    private lateinit var apiService: ApiService
    private lateinit var originalGuardian: Guardian

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditGuardianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiClient.createApiService(this)

        setupSpinner()
        loadGuardianData()
        setupClickListeners()
    }

    private fun setupSpinner() {
        val relationships = arrayOf("자녀", "간병인", "배우자", "형제", "손자녀", "기타")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, relationships)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRelationship.adapter = adapter
    }

    private fun loadGuardianData() {
        val name = intent.getStringExtra("GUARDIAN_NAME") ?: ""
        val phone = intent.getStringExtra("GUARDIAN_PHONE") ?: ""
        val relationship = intent.getStringExtra("GUARDIAN_RELATIONSHIP") ?: ""

        originalGuardian = Guardian(name = name, phone_number = phone, relationship = relationship)

        binding.boxName.setText(name)
        binding.boxPhone.setText(phone)
        val relationshipPosition = (binding.spinnerRelationship.adapter as ArrayAdapter<String>).getPosition(relationship)
        binding.spinnerRelationship.setSelection(relationshipPosition)
    }

    private fun setupClickListeners() {
        binding.left.setOnClickListener { finish() }
        binding.editBtn.setOnClickListener { updateGuardian() }
    }

    private fun updateGuardian() {
        val newName = binding.boxName.text.toString()
        val newPhone = binding.boxPhone.text.toString()
        val newRelationship = binding.spinnerRelationship.selectedItem.toString()

        val updateRequest = GuardianUpdateRequest(
            old_name = originalGuardian.name,
            old_phone_number = originalGuardian.phone_number,
            name = if (newName != originalGuardian.name) newName else null,
            phone_number = if (newPhone != originalGuardian.phone_number) newPhone else null,
            relationship = if (newRelationship != originalGuardian.relationship) newRelationship else null
        )

        Log.d("EditGuardActivity", "Updating guardian with data: $updateRequest")

        val token = SharedPrefManager.getToken(this)
        if (token != null) {
            apiService.updateGuardian("Bearer $token", updateRequest).enqueue(object : Callback<Guardian> {
                override fun onResponse(call: Call<Guardian>, response: Response<Guardian>) {
                    if (response.isSuccessful) {
                        Log.d("EditGuardActivity", "Guardian updated successfully")
                        Toast.makeText(this@EditGuardActivity, "보호자 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("EditGuardActivity", "Failed to update guardian. Error: $errorBody")
                        Toast.makeText(this@EditGuardActivity, "보호자 정보 수정에 실패했습니다. 오류: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Guardian>, t: Throwable) {
                    Log.e("EditGuardActivity", "Network error", t)
                    Toast.makeText(this@EditGuardActivity, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}