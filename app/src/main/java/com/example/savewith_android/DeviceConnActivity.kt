package com.example.savewith_android
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savewith_android.databinding.ActivityDeviceConnectionBinding

class DeviceConnActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceConnectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_device_connection)

        binding = ActivityDeviceConnectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        // RecyclerView 설정
        val devices = listOf(
            ItemDevice("수정의 S22", "Galaxy S22", "2022. 07. 26. 오후 11:41. 대한민국"),
            // 여기에 더 많은 기기 데이터를 추가하세요
        )

        val adapter = ItemDeviceAdapter(devices)
        binding.recyclerViewDevice.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewDevice.adapter = adapter

        binding.addBtn.setOnClickListener {
            // 새 기기 추가하는 작업
        }
    }
}