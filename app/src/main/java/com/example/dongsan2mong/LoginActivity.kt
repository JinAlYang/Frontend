package com.example.dongsan2mong

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.dongsan2mong.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var backpressedTime: Long = 0
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            // 다시 돌아왔을 때 할 거
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.apply {

            loginNullBtn.setOnClickListener {
                val i = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            }

            val loginIntent = Intent(this@LoginActivity, SignupActivity::class.java)
            loginKakaoBtn.setOnClickListener {
                loginIntent.putExtra("isKakao", true)
                launcher.launch(loginIntent)

            }
            loginNaverBtn.setOnClickListener {
                loginIntent.putExtra("isKakao", false)
                launcher.launch(loginIntent)

            }
        }

    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis()
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish()
        }
    }

}