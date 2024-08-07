package com.example.savewith_android

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savewith_android.databinding.ActivityDeviceConnectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class DeviceConnActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceConnectionBinding
    private val devices = mutableListOf<ItemDevice>()  // 기기 정보를 저장할 리스트
    private lateinit var adapter: ItemDeviceAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        // RecyclerView 설정
//        val devices = listOf(
//            ItemDevice("수정의 S22", "Galaxy S22", "2022. 07. 26. 오후 11:41. 대한민국"),
//            // 여기에 더 많은 기기 데이터를 추가하세요
//        )

        // RecyclerView 설정
        adapter = ItemDeviceAdapter(devices)
        binding.recyclerViewDevice.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewDevice.adapter = adapter

        // 기기 정보 가져오기 및 RecyclerView 업데이트
        fetchDeviceInfo()

        binding.addBtn.setOnClickListener {
            // 새 기기 추가하는 작업
            // 예를 들어, 기기 정보를 입력받는 다이얼로그를 띄우고, 입력받은 정보를 RecyclerView에 추가
            // showAddDeviceDialog()
        }
    }

    private fun fetchDeviceInfo() {
        // 토큰 가져오기
        val token = getAuthToken() ?: return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 서버에서 유저 정보 가져오기
                val response: Response<UserProfileResponse> = RetrofitClient.apiService.getUserProfile("Bearer $token")

                if (response.isSuccessful) {
                    val userProfile = response.body()
                    val deviceInfo = ItemDevice(
                        UserName = userProfile?.name ?: "Unknown User",  // 서버에서 받은 사용자 이름
                        Model = "${Build.MANUFACTURER} ${Build.MODEL}",
                        LastUsed = Build.VERSION.RELEASE
                    )

                    // 리스트에 기기 정보 추가 및 RecyclerView 업데이트
                    devices.add(deviceInfo)
                    runOnUiThread {
                        adapter.updateItems(devices)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@DeviceConnActivity, "유저 정보 가져오기 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@DeviceConnActivity, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // 기기 정보 수집
//        val deviceInfo = ItemDevice(
//            UserName = "User",  // 여기에 사용자 이름을 넣을 수 있습니다 (예: 로그인된 사용자 이름)
//            Model = "${Build.MANUFACTURER} ${Build.MODEL}",
//            LastUsed = Build.VERSION.RELEASE
//        )

        // 리스트에 기기 정보 추가 및 RecyclerView 업데이트
//        devices.add(deviceInfo)
//        adapter.updateItems(devices)
    }

    private fun showAddDeviceDialog() {
        // 사용자로부터 기기 정보를 입력받는 다이얼로그를 띄우고, 기기 정보를 추가하는 코드 구현
    }

    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }
}