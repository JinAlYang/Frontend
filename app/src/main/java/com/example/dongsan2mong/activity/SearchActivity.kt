package com.example.dongsan2mong.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dongsan2mong.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.apply {
            searchBackBtn.setOnClickListener {
                finish()
            }
        }
    }
}