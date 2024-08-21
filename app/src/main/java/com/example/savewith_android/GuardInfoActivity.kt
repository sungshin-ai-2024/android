package com.example.savewith_android
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Spinner
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
    private var guardians: List<Guardian> = listOf()

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
            // showAddGuardianDialog()
        }
    }

    private fun loadGuardians() {
        val token = SharedPrefManager.getToken(this)
        if (token != null) {
            apiService.getGuardians("Token $token").enqueue(object : Callback<List<Guardian>> {
                override fun onResponse(call: Call<List<Guardian>>, response: Response<List<Guardian>>) {
                    if (response.isSuccessful) {
                        guardians = response.body() ?: emptyList()
                        guardianAdapter.updateGuardians(guardians)
                    } else {
                        Toast.makeText(this@GuardInfoActivity, "보호자 목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<List<Guardian>>, t: Throwable) {
                    Toast.makeText(this@GuardInfoActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onEditGuardian(guardian: Guardian) {
        Log.d("GuardInfoActivity", "Edit guardian called for: ${guardian.name}")
        val intent = Intent(this, EditGuardActivity::class.java)
        intent.putExtra("GUARDIAN_NAME", guardian.name)
        intent.putExtra("GUARDIAN_PHONE", guardian.phone_number)
        intent.putExtra("GUARDIAN_RELATIONSHIP", guardian.relationship)
        startActivityForResult(intent, EDIT_GUARDIAN_REQUEST)
    }
    override fun onDeleteGuardian(guardian: Guardian) {
        Log.d("GuardInfoActivity", "Delete guardian called for: ${guardian.name}")
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
        if (token != null) {
            apiService.deleteGuardian("Token $token", guardian.name, guardian.phone_number).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@GuardInfoActivity, "${guardian.name}님이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        loadGuardians()  // 목록 새로고침
                    } else {
                        Toast.makeText(this@GuardInfoActivity, "보호자 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@GuardInfoActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
//    private fun showAddGuardianDialog() {
//        val dialogView = layoutInflater.inflate(R.layout.dialog_add_guardian, null)
//        val nameEditText = dialogView.findViewById<EditText>(R.id.nameEditText)
//        val phoneEditText = dialogView.findViewById<EditText>(R.id.phoneEditText)
//        val relationshipSpinner = dialogView.findViewById<Spinner>(R.id.relationshipSpinner)
//
//        AlertDialog.Builder(this)
//            .setTitle("보호자 추가")
//            .setView(dialogView)
//            .setPositiveButton("추가") { _, _ ->
//                val name = nameEditText.text.toString()
//                val phone = phoneEditText.text.toString()
//                val relationship = relationshipSpinner.selectedItem.toString()
//                if (name.isNotEmpty() && phone.isNotEmpty()) {
//                    addGuardian(Guardian(name = name, phone_number = phone, relationship = relationship))
//                } else {
//                    Toast.makeText(this, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
//                }
//            }
//            .setNegativeButton("취소", null)
//            .show()
//    }
//
//    private fun addGuardian(guardian: Guardian) {
//        val token = SharedPrefManager.getToken(this)
//        if (token != null) {
//            apiService.addGuardian("Token $token", guardian).enqueue(object : Callback<Guardian> {
//                override fun onResponse(call: Call<Guardian>, response: Response<Guardian>) {
//                    if (response.isSuccessful) {
//                        Toast.makeText(this@GuardInfoActivity, "보호자가 추가되었습니다.", Toast.LENGTH_SHORT).show()
//                        loadGuardians()  // 목록 새로고침
//                    } else {
//                        Toast.makeText(this@GuardInfoActivity, "보호자 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<Guardian>, t: Throwable) {
//                    Toast.makeText(this@GuardInfoActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
//                }
//            })
//        } else {
//            Toast.makeText(this, "사용자 인증 정보가 없습니다.", Toast.LENGTH_SHORT).show()
//        }
//    }

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