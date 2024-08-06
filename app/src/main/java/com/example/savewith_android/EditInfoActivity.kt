package com.example.savewith_android
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.savewith_android.databinding.ActivityEditInfoBinding
import java.util.Calendar

class EditInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_info)

        binding = ActivityEditInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

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

        binding.editBtn.setOnClickListener {
            // EditText에서 입력받은 텍스트를 저장
            val name = binding.boxName.text.toString()
            val phoneNumber = binding.boxPhone.text.toString()
            val userId = binding.boxId.text.toString()
            val address1 = binding.box1Adrss.text.toString()
            val address2 = binding.box2Adrss.text.toString()
            val address3 = binding.box3Adrss.text.toString()

            // 생년월일 및 성별 저장 코드 추가

            // 위 변수가 모두 isNotEmpty()이면 개인정보 수정하는 작업 수행

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
}