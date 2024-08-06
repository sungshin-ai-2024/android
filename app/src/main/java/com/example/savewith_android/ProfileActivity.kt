package com.example.savewith_android
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.savewith_android.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        // 유저 DB에서 프로필 내용 불러와서 텍스트에 저장하는 코드 구현 //

        binding.editInfo.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.infoGuard.setOnClickListener {
            val intent = Intent(this, GuardInfoActivity::class.java)
            startActivity(intent)
        }
    }
}