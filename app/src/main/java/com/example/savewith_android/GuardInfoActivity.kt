package com.example.savewith_android
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savewith_android.databinding.ActivityGuardianInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface GuardianActionListener {
    fun onEditGuardian(guardian: Guardian)
    fun onDeleteGuardian(guardian: Guardian)
}

class GuardInfoActivity : AppCompatActivity(), GuardianActionListener {
    private lateinit var binding: ActivityGuardianInfoBinding
    private lateinit var guardianAdapter: GuardianAdapter
    private lateinit var apiService: ApiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuardianInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiClient.createApiService(this)
        setupRecyclerView()
        setupClickListeners()

        loadGuardians()
    }

    private fun setupRecyclerView() {
        guardianAdapter = GuardianAdapter(this)
        binding.recyclerViewGuard.apply {
            layoutManager = LinearLayoutManager(this@GuardInfoActivity)
            adapter = guardianAdapter
        }
    }

    private fun setupClickListeners() {
        binding.left.setOnClickListener { finish() }
        binding.addGuardBtn.setOnClickListener {
            val intent = Intent(this, AddGuardActivity::class.java)
            startActivityForResult(intent, ADD_GUARDIAN_REQUEST)
        }
    }

    private fun loadGuardians() {
        val token = SharedPrefManager.getToken(this)
        if (token != null) {
            apiService.getGuardians("Token $token").enqueue(object : Callback<List<Guardian>> {
                override fun onResponse(call: Call<List<Guardian>>, response: Response<List<Guardian>>) {
                    if (response.isSuccessful) {
                        val guardians = response.body() ?: emptyList()
                        Log.d("GuardInfo", "Loaded guardians: $guardians")
                        guardianAdapter.updateGuardians(guardians)
                    } else {
                        Log.e("GuardInfo", "Failed to load guardians. Code: ${response.code()}")
                        Toast.makeText(this@GuardInfoActivity, "보호자 목록을 불러오는 데 실패했습니다. 오류 코드: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<List<Guardian>>, t: Throwable) {
                    Toast.makeText(this@GuardInfoActivity, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onEditGuardian(guardian: Guardian) {
        Log.d("GuardInfo", "Editing guardian: $guardian")
        val intent = Intent(this, EditGuardActivity::class.java)
        intent.putExtra("GUARDIAN_ID", guardian.id)
        intent.putExtra("GUARDIAN_NAME", guardian.name)
        intent.putExtra("GUARDIAN_PHONE", guardian.phone_number)
        intent.putExtra("GUARDIAN_RELATIONSHIP", guardian.relationship)
        startActivityForResult(intent, EDIT_GUARDIAN_REQUEST)
    }

    override fun onDeleteGuardian(guardian: Guardian) {
        AlertDialog.Builder(this)
            .setTitle("보호자 삭제")
            .setMessage("${guardian.name}님을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                deleteGuardian(guardian)
            }
            .setNegativeButton("취소", null)
            .show()
    }


    private fun deleteGuardian(guardian: Guardian) {
        val token = SharedPrefManager.getToken(this)
        Log.d("DeleteGuardian", "Deleting guardian: $guardian")

        if (token != null && guardian.id != null) {
            apiService.deleteGuardian("Token $token", guardian.id).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("DeleteGuardian", "Delete successful")
                        Toast.makeText(this@GuardInfoActivity, "${guardian.name}님이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        loadGuardians()
                    } else {
                        Log.e("DeleteGuardian", "Delete failed. Code: ${response.code()}, Message: ${response.message()}")
                        Toast.makeText(this@GuardInfoActivity, "보호자 삭제에 실패했습니다. 오류 코드: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("DeleteGuardian", "Network error", t)
                    Toast.makeText(this@GuardInfoActivity, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.e("DeleteGuardian", "Token is null: ${token == null}, Guardian ID is null: ${guardian.id == null}")
            Toast.makeText(this, "사용자 인증 정보가 없거나 유효하지 않은 보호자 ID입니다.", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == ADD_GUARDIAN_REQUEST || requestCode == EDIT_GUARDIAN_REQUEST) && resultCode == Activity.RESULT_OK) {
            loadGuardians() // 보호자 추가 또는 수정 후 목록 갱신
        }
    }

    companion object {
        private const val ADD_GUARDIAN_REQUEST = 1
        private const val EDIT_GUARDIAN_REQUEST = 2
    }
}