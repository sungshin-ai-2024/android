package com.example.savewith_android
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savewith_android.databinding.ActivityGuardianInfoBinding

class GuardInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuardianInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_guardian_info)

        val guardians = listOf(
            ItemGuardian("김수룡", "자녀", "+82 10-2345-6789"),
            ItemGuardian("이영희", "형제", "+82 10-9876-5432")
        ) // Django DB 연동 통신으로 수정

        binding.recyclerViewGuard.apply {
            layoutManager = LinearLayoutManager(this@GuardInfoActivity)
            adapter = GuardianAdapter(guardians)
        } //

        binding = ActivityGuardianInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        binding.addGuardBtn.setOnClickListener {
            val intent = Intent(this, AddGuardActivity::class.java)
            startActivity(intent)
        }
    }
}