package com.example.savewith_android
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.savewith_android.databinding.ActivityEditInfoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Calendar

class EditInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditInfoBinding

    // Retrofit 인스턴스 설정
//    private val apiService by lazy {
//        RetrofitClient.apiService
//    }
    private lateinit var apiService: ApiService

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)

        binding = ActivityEditInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        // 생년월일 선택 버튼 클릭 시 DatePickerDialog 표시
        binding.birthBtn.setOnClickListener {
            showDatePicker()
        }

        binding.editBtn.setOnClickListener {
            // EditText에서 입력받은 텍스트를 저장
            val name = binding.boxName.text.toString()
            val phone = binding.boxPhone.text.toString()
            val userId = binding.boxId.text.toString()
            val address1 = binding.box1Adrss.text.toString()
            val address2 = binding.box2Adrss.text.toString()
            val address3 = binding.box3Adrss.text.toString()
            val birthDate = binding.boxBirth.text.toString()
            val gender = binding.boxSex.selectedItem.toString()

            // 생년월일 및 성별 저장 코드 추가

            // 위 변수가 모두 isNotEmpty()이면 개인정보 수정하는 작업 수행

            // 입력값이 비어있는지 체크
            if (validateInputs()) {
                val updateRequest = createUpdateRequest()
                updateUserProfile(updateRequest)
            } else {
                Toast.makeText(this, "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnPw.setOnClickListener {
            // 비밀정보 수정하는 화면으로 이동
            val intent = Intent(this, EditPhoneActivity::class.java)
            startActivity(intent)
        }
        binding.btnNum.setOnClickListener {
            // 휴대폰 번호 인증하는 화명 호출
        }
        binding.adrssBtn.setOnClickListener {
            // 주소 검색 및 주소 텍스트로 받아오는 화면 호출
        }
    }
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "${selectedMonth + 1}/$selectedDay/$selectedYear"
            binding.boxBirth.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun validateInputs(): Boolean {
        return binding.boxName.text.isNotEmpty() &&
                binding.boxPhone.text.isNotEmpty() &&
                binding.boxId.text.isNotEmpty()
    }

    private fun createUpdateRequest(): UserProfileUpdateRequest {
        val address = Address(
            binding.box1Adrss.text.toString(),
            binding.box2Adrss.text.toString(),
            binding.box3Adrss.text.toString()
        )
        return UserProfileUpdateRequest(
            name = binding.boxName.text.toString(),
            id = binding.boxId.text.toString(),
            phone = binding.boxPhone.text.toString(),
            birth = binding.boxBirth.text.toString(),
            gender = binding.boxSex.selectedItem.toString(),
            address1 = address.address1,
            address2 = address.address2,
            address3 = address.address3,
            photoUrl = null // Optional
        )
    }

    private fun updateUserProfile(updateRequest: UserProfileUpdateRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Authorization Token을 실제 값으로 대체
                val token = getAuthToken()?: return@launch
//                val token = "Bearer YOUR_AUTH_TOKEN"
                val response: Response<Void> = apiService.updateUserProfile("Bearer $token", updateRequest)

                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@EditInfoActivity, "정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                        finish() // 성공 후 액티비티 종료
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@EditInfoActivity, "정보 업데이트 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditInfoActivity, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }
}