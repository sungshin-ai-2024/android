package com.example.savewith_android
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.savewith_android.databinding.ActivityEditGuardianBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class EditGuardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditGuardianBinding
    private lateinit var apiService: ApiService
    private var guardianId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditGuardianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiClient.createApiService(this)

        guardianId = intent.getIntExtra("GUARDIAN_ID", -1)
        Log.d("EditGuard", "Received guardian ID: $guardianId")
        if (guardianId == -1) {
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

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
        val token = SharedPrefManager.getToken(this)
        if (token != null) {
            apiService.getGuardian("Bearer $token", guardianId!!).enqueue(object : Callback<Guardian> {
                override fun onResponse(call: Call<Guardian>, response: Response<Guardian>) {
                    if (response.isSuccessful) {
                        val guardian = response.body()
                        guardian?.let { updateUIWithGuardianData(it) }
                    } else {
                        Toast.makeText(this@EditGuardActivity, "보호자 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Guardian>, t: Throwable) {
                    Toast.makeText(this@EditGuardActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없거나 유효하지 않은 보호자 ID입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateUIWithGuardianData(guardian: Guardian) {
        binding.boxName.setText(guardian.name)
        binding.boxPhone.setText(guardian.phone_number)
        val relationshipPosition = (binding.spinnerRelationship.adapter as ArrayAdapter<String>).getPosition(guardian.relationship)
        binding.spinnerRelationship.setSelection(relationshipPosition)
    }

    private fun setupClickListeners() {
        binding.left.setOnClickListener { finish() }
        binding.editBtn.setOnClickListener { updateGuardian() }
    }

    private fun updateGuardian() {
        val name = binding.boxName.text.toString()
        val phoneNumber = binding.boxPhone.text.toString()
        val relationship = binding.spinnerRelationship.selectedItem.toString()

        if (name.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedGuardian = Guardian(
            id = guardianId,
            name = name,
            phone_number = phoneNumber,
            relationship = relationship
        )

        val token = SharedPrefManager.getToken(this)
        if (token != null && guardianId != null) {
            apiService.updateGuardian("Bearer $token", guardianId!!, updatedGuardian).enqueue(object : Callback<Guardian> {
                override fun onResponse(call: Call<Guardian>, response: Response<Guardian>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditGuardActivity, "보호자 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@EditGuardActivity, "보호자 정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Guardian>, t: Throwable) {
                    Toast.makeText(this@EditGuardActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없거나 유효하지 않은 보호자 ID입니다.", Toast.LENGTH_SHORT).show()
        }
    }
}