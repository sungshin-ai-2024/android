package com.example.savewith_android
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Spinner
import java.util.Calendar
import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.savewith_android.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup) // SignUpActivity 레이아웃 파일 경로

        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 생년월일 선택 버튼 클릭 시 DatePickerDialog 표시
        binding.birthBtn.setOnClickListener {
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

        binding.signupBtn.setOnClickListener { // 회원가입 버튼 클릭 시
            // EditText에서 입력받은 텍스트를 저장
            val name = binding.signupName.text.toString()
            val phoneNumber = binding.signupPhoneNum.text.toString()
            val userId = binding.signupId.text.toString()
            val password = binding.signupPw.text.toString()
            val address = binding.detailAdrss.text.toString()

            // 성별 선택 및 저장
            val gender = binding.spinnerGender.selectedItem.toString()

            // 데베 API 호출 및 작업 코드 추가

            val intent = Intent(this, Login2Activity::class.java) // 로그인2화면으로
            startActivity(intent)
        }

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        binding.adrssBtn.setOnClickListener { // 주소검색 버튼 클릭 시
            // 주소 검색하는 함수 코드 추가
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.chngPhotoBtn.setOnClickListener { // 사진추가 버튼 클릭 시
            // 사진첩에 접근하여 사진 등록하는 함수 코드 추가
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 시스템 바 패딩 설정
//        ViewCompat.setOnApplyWindowInsetsListener(binding.activitySignUp) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}