package com.example.savewith_android
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savewith_android.databinding.ActivityGuardianInfoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class GuardInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuardianInfoBinding
    private val viewModel: ItemGuardianModel by viewModels()  // ViewModel 초기화

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGuardianInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapterGuard = ItemGuardianAdapter(emptyList())
        binding.recyclerViewGuard.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewGuard.adapter = adapterGuard

        viewModel.guardians.observe(this, Observer { guardians ->
            if (guardians.isEmpty()) {
                // 빈 리스트에 대한 처리 (예: 메시지 표시)
                // binding.emptyMessage.visibility = View.VISIBLE
            } else {
                adapterGuard.updateItems(guardians)
            }
//            adapterGuard.updateItems(guardians)
        })
//        val adapter_contact = ThreatContactAdapter(this, emptyList())

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        binding.addGuardBtn.setOnClickListener {
            val intent = Intent(this, AddGuardActivity::class.java)
            startActivity(intent)
        }
        // 로그인된 유저의 보호자 정보 로드
        loadGuardianData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadGuardianData() {
        CoroutineScope(Dispatchers.IO).launch {
//            val response = RetrofitClient.apiService.getGuardianData() // 적절한 API 호출로 대체
//            if (response.isSuccessful) {
//                response.body()?.let { guardianList ->
//                    viewModel.setGuardians(guardianList)
//                }
//            } else {
//                // 에러 처리
//            }

            try {
                val token = MyApplication.appContext.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                    .getString("auth_token", null)

                if (token != null) {
                    val response: Response<List<ItemGuardian>> = RetrofitClient.guardianService.getGuardianData("Bearer $token")
                    if (response.isSuccessful) {
                        response.body()?.let { guardianList ->
                            runOnUiThread {
                                viewModel.setGuardians(guardianList)
                            }
                        }
                    } else {
                        // 에러 처리 (UI 쓰레드에서 처리해야 함)
                        runOnUiThread {
                            // Show error message
                            Toast.makeText(this@GuardInfoActivity, "로그인 상태가 불안정합니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@GuardInfoActivity, "유저의 토큰이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                    // Token이 없을 때의 처리
                }
            } catch (e: Exception) {
                // Exception 처리
                runOnUiThread {
                    // Show error message
                }
            }
        }
    }
}