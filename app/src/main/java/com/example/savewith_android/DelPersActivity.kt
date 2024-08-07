package com.example.savewith_android

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.savewith_android.databinding.ActivityDelPersdataBinding

class DelPersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDelPersdataBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_del_persdata)

        binding = ActivityDelPersdataBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        binding.delBtn.setOnClickListener {
            // 개인데이터 삭제를 요청할 경우 수행할 작업
            // 예: 삭제를 위한 API 호출, 화면 전환 등
            Toast.makeText(this, "개인 데이터 삭제 요청 중...", Toast.LENGTH_SHORT).show()
            // 개인 데이터 삭제 처리를 여기서 수행합니다.
        }
    }
}