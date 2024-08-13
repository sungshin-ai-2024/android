package com.example.savewith_android
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.savewith_android.databinding.ActivityGuardianInfoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuardInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuardianInfoBinding
    private val viewModel: ItemGuardianModel by viewModels()  // ViewModel 초기화
    private lateinit var apiService: ApiService

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

    /*
    private fun loadGuardianData() {
        val token = SharedPrefManager.getToken(this) // SharedPrefManager를 사용해 토큰을 가져옴
        if (token != null) {
            apiService.getGuardProfile("Bearer $token").enqueue(object : Callback<GuardProfileResponse> {
                override fun onResponse(call: Call<GuardProfileResponse>, response: Response<GuardProfileResponse) {
                    if (response.isSuccessful) {
                        val guardProfileResponse = response.body()
                        if (guardProfileResponse != null) {
                            val userProfile = guardProfileResponse.guardian // ProfileResponse에서 Profile 객체를 가져옴
                            updateUI(userProfile)
                        }else {
                            Toast.makeText(this@GuardInfoActivity, "유저 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
                            Log.e("ProfileActivity", "User data is null")
                        }
                    } else {
                        Toast.makeText(this@GuardInfoActivity, "프로필 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.e("ProfileActivity", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Toast.makeText(this@GuardInfoActivity, "네트워크 오류로 프로필 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("ProfileActivity", "Failure: ${t.message}", t)
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
        }

        CoroutineScope(Dispatchers.IO).launch {
//            val response = RetrofitClient.apiService.getGuardianData() // 적절한 API 호출로 대체
//            if (response.isSuccessful) {
//                response.body()?.let { guardianList ->
//                    viewModel.setGuardians(guardianList)
//                }
//            } else {
//                // 에러 처리
//            }

            /*
            try {
                val token = MyApplication.appContext.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                    .getString("auth_token", null)

                if (token != null) {
                    val response: Response<List<ItemGuardian>> = ApiClient.guardianService.getGuardianData("Bearer $token")
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
            }*/
        }
    }*/

    private fun loadGuardianData() {
        val token = SharedPrefManager.getToken(this) // SharedPrefManager를 사용해 토큰을 가져옴
        if (token != null) {
            apiService.getGuardians("Bearer $token").enqueue(object : Callback<List<Guardian>> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<List<Guardian>>, response: Response<List<Guardian>>) {
                    if (response.isSuccessful) {
                        val guardians = response.body() ?: emptyList()
                        viewModel.setGuardians(guardians.map { ItemGuardian(it.name, it.phone, it.relation, it.photoUrl) })
                    } else {
                        Toast.makeText(this@GuardInfoActivity, "보호자 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.e("GuardInfoActivity", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<List<Guardian>>, t: Throwable) {
                    Toast.makeText(this@GuardInfoActivity, "네트워크 오류로 보호자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("GuardInfoActivity", "Failure: ${t.message}", t)
                }
            })
        } else {
            Toast.makeText(this, "사용자 인증 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    /*private fun updateUI(guardData: Guardian) {
        binding.boxName.text = guardData.name
        binding.boxId.text = guardData.phone
        binding.detailAdrss.text = guardData.relation
    }*/
}