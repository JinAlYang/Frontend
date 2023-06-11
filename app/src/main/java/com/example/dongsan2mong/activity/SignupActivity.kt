package com.example.dongsan2mong.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.dongsan2mong.R
import com.example.dongsan2mong.data.MemberInfoData
import com.example.dongsan2mong.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        try {
//            println("hello~~~~")
//            val popup = Spinner::class.java.getDeclaredField("mPopup")
//            popup.isAccessible = true
//            val window: ListPopupWindow = popup[binding.yearSpinner] as ListPopupWindow
//            window.height = 50 //pixel
//        } catch (e: Exception) {
//            println("catch~~~")
//            e.printStackTrace()
//        }
        initSignUp()
    }

    private fun initSignUp() {
        val memberInfo = intent.getSerializableExtra("memberInfo") as? MemberInfoData
        if (memberInfo != null) {
            binding.loginName.text = memberInfo.name
            binding.loginEmail.text = memberInfo.email
        }

        val isKakao = intent.getBooleanExtra("isKakao", true)
        if (!isKakao) {
            binding.loginWhereIcon.setImageResource(R.drawable.icon_login_naver)
        }
        binding.signupCheck.setOnClickListener {
            val i = Intent(this@SignupActivity, MainActivity::class.java)
            i.putExtra("memberInfo", memberInfo)
            startActivity(i)
            finish()
        }



        setupSpinnerYear()

    }

    private fun setupSpinnerYear() {
        val years = resources.getStringArray(R.array.yearSpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        binding.yearSpinner.adapter = adapter
    }


}