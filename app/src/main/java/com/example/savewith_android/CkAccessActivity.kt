package com.example.savewith_android
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.savewith_android.databinding.ActivityAccessBinding

class CkAccessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccessBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_access)

        binding = ActivityAccessBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnOk.setOnClickListener { // 확인 버튼 클릭 시
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}