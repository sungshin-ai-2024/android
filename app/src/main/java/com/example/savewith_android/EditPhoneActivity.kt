package com.example.savewith_android
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.savewith_android.databinding.ActivityEditPhonenumBinding

class EditPhoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPhonenumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_phonenum)

        binding = ActivityEditPhonenumBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        binding.certifBtn.setOnClickListener {
            val phoneNum = binding.boxPhoneNum.text.toString()
            if(phoneNum.isNotEmpty()){
                // 해당 번호로 인증번호 보내는 코드
            }
        }
        binding.editNumBtn.setOnClickListener {
            val certifNum = binding.boxCertificationNum.text.toString()
            if(certifNum.isNotEmpty()){
                // 인증번호가 일치하는 지 확인하는 코드
                // 데베 API 호출 및 번호 수정
                Toast.makeText(this, "휴대폰 번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}