package com.example.savewith_android

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.savewith_android.databinding.ActivityChgPwdBinding

class CngPwdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChgPwdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chg_pwd)

        binding = ActivityChgPwdBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 뒤로 가기
        binding.left.setOnClickListener { // 뒤로가기 시
            finish()
        }

        binding.editNumBtn.setOnClickListener { // 변경하기 클릭시
            val currentPwd = binding.boxOrigPwd.text.toString()
            val newPwd = binding.boxNewPwd.text.toString()
            val ckNewPwd = binding.boxCkNewPwd.text.toString()

            if (currentPwd.isNotEmpty() && newPwd.isNotEmpty() && ckNewPwd.isNotEmpty()) {
//                changePwd(userName, userPhoneNumber, userRelationship) // 데베에 비밀번호 변경 코드 추가

                Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()

//                finish()
            } else if(currentPwd.isEmpty()) {
                Toast.makeText(this, "현재 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if(newPwd.isEmpty()) {
                Toast.makeText(this, "새 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if(ckNewPwd.isEmpty()) {
                Toast.makeText(this, "새 비밀번호 확인 칸을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cancelEditBtn.setOnClickListener {
            Toast.makeText(this, "비밀번호 변경을 취소합니다.", Toast.LENGTH_SHORT).show()

            finish()
        }
    }
}