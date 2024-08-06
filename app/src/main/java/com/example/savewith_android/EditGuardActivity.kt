package com.example.savewith_android
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.savewith_android.databinding.ActivityEditGuardianBinding

class EditGuardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditGuardianBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_guardian)

        binding = ActivityEditGuardianBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        binding.editBtn.setOnClickListener {
            val guardName = binding.boxName.text.toString()
            val phoneNum = binding.boxPhone.text.toString()
            val relationship = binding.spinnerRelationship.selectedItem.toString()

            // 수정 완료하기 위해 휴대폰 인증을 확인하고 수정 완료하는 코드 추가
        }

        binding.btnNum.setOnClickListener {
            // 휴대폰 번호 인증 완료시
        }

    }
}