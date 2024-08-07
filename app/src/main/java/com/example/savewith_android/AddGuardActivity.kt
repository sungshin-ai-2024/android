package com.example.savewith_android
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.savewith_android.databinding.ActivityAddGuardianBinding

class AddGuardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddGuardianBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_guardian)

        binding = ActivityAddGuardianBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.left.setOnClickListener { // 뒤로가기 시
            finish()
        }

        binding.addBtn.setOnClickListener { // 추가하기 버튼 클릭 시
            val userName = binding.addName.text.toString()
            val userPhoneNumber = binding.addPhoneNum.text.toString()

            // 보호자 관계 선택 및 저장
            val userRelationship = binding.spinnerRelationship.selectedItem.toString()

            if (userName.isNotEmpty() && userPhoneNumber.isNotEmpty() && userRelationship.isNotEmpty()) {
                // + threatActivity에도 정보를 보내주는 기능 구현 필요
                // addGuard(userName, userPhoneNumber, userRelationship) // 데베에 보호자 추가하는 함수 추가
                Toast.makeText(this, "보호자가 추가되었습니다.", Toast.LENGTH_SHORT).show()

//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
            } else {
                Toast.makeText(this, "해당 보호자의 정보를 전부 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}