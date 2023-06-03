package com.example.dongsan2mong.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.fragment.app.Fragment
import com.example.dongsan2mong.R
import com.example.dongsan2mong.fragment.RealestatecompareFragment
import com.example.dongsan2mong.databinding.ActivityMainBinding
import com.example.dongsan2mong.fragment.MapFragment
import com.example.dongsan2mong.fragment.PlannerFragment
import com.example.dongsan2mong.fragment.WishlistFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var bnv: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bnv = binding.bottomNav

        init()
    }

    private fun init() {
        // 초기 화면 MapFragment()로
        supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, MapFragment())
            .commitAllowingStateLoss()

        bnv.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.mapMenu -> {
                        MapFragment()
                    }
                    R.id.wishlistMenu -> {
                        WishlistFragment()
                    }
                    R.id.comparisonMenu -> {
                        RealestatecompareFragment()
                    }
                    else -> {
                        PlannerFragment()
                    }
                }
            )
            true
        }
        binding.drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)

        binding.logout.setOnClickListener {
            finish()
        }

    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.frameLayout.id, fragment)
            .commit()
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
    }

    fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.END)
    }

}