package com.example.savewith_android
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.savewith_android.databinding.ActivityPrivacyPolicyBinding

class PrivacyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_privacy_policy)

        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnOk.setOnClickListener { // 닫기 버튼 클릭 시
            finish()
        }

        // 아래는 개인정보 처리방침 세부내용 볼 수 있는 화면
        // 구현할지 안할지 아직 잘 모르겠음
        binding.policy1Btn.setOnClickListener {  }
        binding.policy2Btn.setOnClickListener {  }
        binding.policy3Btn.setOnClickListener {  }
        binding.policy4Btn.setOnClickListener {  }
        binding.policy5Btn.setOnClickListener {  }
        binding.policy6Btn.setOnClickListener {  }
        binding.policy7Btn.setOnClickListener {  }
    }
}