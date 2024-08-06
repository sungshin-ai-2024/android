package com.example.savewith_android
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.savewith_android.databinding.ActivityDelAcctBinding

class DelAcctActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDelAcctBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_del_acct)

        binding = ActivityDelAcctBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        binding.delAcctBtn.setOnClickListener {
            val cbDelAcct = binding.cbDelAcct.isChecked

            if(cbDelAcct){
                // 체크박스가 체크된 경우 수행할 작업
                // 예: 탈퇴를 위한 API 호출, 화면 전환 등
                Toast.makeText(this, "탈퇴 진행 중...", Toast.LENGTH_SHORT).show()
                // 탈퇴 처리를 여기서 수행합니다.
            } else {
                // 체크박스가 체크되지 않은 경우 경고 메시지 표시
                Toast.makeText(this, "탈퇴 안내사항에 동의해야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}