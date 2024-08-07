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
import com.example.savewith_android.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 뒤로 가기
        binding.left.setOnClickListener { // 뒤로가기 시
            finish()
        }

        // 일반
        binding.settTxtMeas.setOnClickListener { // 측정단위 클릭 시
            val intent = Intent(this, MeasureActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtNotif.setOnClickListener { // 알림설정 클릭 시
            val intent = Intent(this, NotifSettActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtDev.setOnClickListener { // 기기연결 클릭 시
            val intent = Intent(this, DeviceConnActivity::class.java)
            startActivity(intent)
        }

        // 개인정보
        binding.settTxtPp.setOnClickListener { // 개인정보 처리방침 클릭 시
            val intent = Intent(this, PrivacyActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtDelPersdata.setOnClickListener { // 개인데이터삭제 클릭 시
            val intent = Intent(this, DelPersActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtAppPerm.setOnClickListener { // 이앱이사용하는권한 클릭 시
            val intent = Intent(this, AppPermissionActivity::class.java)
            startActivity(intent)
        }
        binding.swchSensitiveInfo.setOnClickListener { // 민감정보 스위치 클릭 시
            switchSensitiveInfo()
        }
        binding.swchLoc.setOnClickListener { // 위치정보 스위치 클릭 시
            switchLocation()
        }
        binding.swchMarketing.setOnClickListener { // 마케팅동의 스위치 클릭 시
            switchMarketing()
        }


        // 계정설정
        binding.settTxtLogout.setOnClickListener { // 로그아웃 클릭 시
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }
        binding.settTxtProfile.setOnClickListener { // 프로필정보 클릭 시
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtGuardInfo.setOnClickListener { // 보호자정보 클릭 시
            val intent = Intent(this, GuardInfoActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtEditPersdata.setOnClickListener { // 개인정보수정 클릭 시
            val intent = Intent(this, EditInfoActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtEditGuard.setOnClickListener { // 보호자정보수정 클릭 시
            val intent = Intent(this, EditGuardActivity::class.java)
            startActivity(intent)
        }
        binding.settTxtDelAccnt.setOnClickListener { // 회원탈퇴 클릭 시
            val intent = Intent(this, DelAcctActivity::class.java)
            startActivity(intent)
        }
    }

    private fun switchSensitiveInfo(){
    }
    private fun switchLocation(){
    }
    private fun switchMarketing(){
    }
}