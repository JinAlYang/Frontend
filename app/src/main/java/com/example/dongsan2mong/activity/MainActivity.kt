package com.example.dongsan2mong.activity

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.fragment.app.Fragment
import com.example.dongsan2mong.R
import com.example.dongsan2mong.data.MemberInfoData
import com.example.dongsan2mong.fragment.RealestatecompareFragment
import com.example.dongsan2mong.databinding.ActivityMainBinding
import com.example.dongsan2mong.databinding.FragmentWishlistBinding
import com.example.dongsan2mong.fragment.MapFragment
import com.example.dongsan2mong.fragment.PlannerFragment
import com.example.dongsan2mong.fragment.WishlistFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var bnv: BottomNavigationView

    lateinit var bindingWishList: FragmentWishlistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bnv = binding.bottomNav

        val memberInfo = intent.getSerializableExtra("memberInfo") as? MemberInfoData
        if (memberInfo != null) {
            binding.userName.text = memberInfo.name
            binding.userEmail.text = memberInfo.email
        }
        Log.d("getKeyHash", "" + getKeyHash(this@MainActivity));

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
//        EventBus.getDefault().unregister(this)

        supportFragmentManager
            .beginTransaction()
            .replace(binding.frameLayout.id, fragment)
            .commit()

//        EventBus.getDefault().post(MainEvent("helloMainBus"))

        Log.d("dataEvent", "메인액티비티에서 helloMainBus 전송~")
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
    }

    fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.END)
    }

    fun getKeyHash(context: Context): String? {
        val pm: PackageManager = context.getPackageManager()
        try {
            val packageInfo: PackageInfo =
                pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES)
                    ?: return null
            for (signature in packageInfo.signatures) {
                try {
                    val md: MessageDigest = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

}