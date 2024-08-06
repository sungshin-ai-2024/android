package com.example.savewith_android
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.savewith_android.databinding.ActivityNotificationSettingsBinding

class NotifSettActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification_settings)

        binding = ActivityNotificationSettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.left.setOnClickListener { // 뒤로가기(left) 버튼 클릭 시
            finish()
        }

        binding.swchBox1.setOnClickListener { // PUSH알림허용 스위치 클릭 시

        }
        binding.swchBox2.setOnClickListener { // 식사알림 스위치 클릭 시

        }
        binding.swch2Box2.setOnClickListener { // 기타건강활동알림 스위치 클릭 시

        }
        binding.swch3Box2.setOnClickListener { // 위험감지알림 스위치 클릭 시
            val switch3Box2 = binding
        }

        binding.check1.setOnClickListener{
            val cbBreakfast = binding.check1.isChecked
            if(cbBreakfast){
                // 아침식사 체크박스가 체크된 경우 수행할 작업
                // 예: 아침 알림
                Toast.makeText(this, " ", Toast.LENGTH_SHORT).show()
                // 탈퇴 처리를 여기서 수행합니다.
            } else{

            }
        }
        binding.check2.setOnClickListener{
            val cbLunch = binding.check2.isChecked
            if(cbLunch) {
                // 점심식사 체크박스가 체크된 경우 수행할 작업
                // 예: 점심 알림
                Toast.makeText(this, " ", Toast.LENGTH_SHORT).show()
                // 탈퇴 처리를 여기서 수행합니다.
            }else{

            }
        }
        binding.check3.setOnClickListener{
            val cbDinner = binding.check3.isChecked
            if(cbDinner) {
                // 저녁식사 체크박스가 체크된 경우 수행할 작업
                // 예: 저녁 알림
                Toast.makeText(this, " ", Toast.LENGTH_SHORT).show()
                // 탈퇴 처리를 여기서 수행합니다.
            }else{

            }
        }

        binding.btnAddMedicine.setOnClickListener {
            // 해당 유저 - 약 정보: 약이름, 복약 시간, 주기를 저장하는 코드 구현
        }


        // 만약 위험감지 알림 스위치가 On 상태이면 아래 체크박스 클릭시
        binding.check4.setOnClickListener{

        }
        binding.check5.setOnClickListener{

        }
        binding.check6.setOnClickListener{

        }
        binding.check6.setOnClickListener{

        }
    }
}