package com.example.dongsan2mong

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.example.dongsan2mong.databinding.FragmentMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*

class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentMapBinding
    lateinit var drawerOpenImageView: ImageView
    lateinit var drawerCloseImageView: ImageView
    lateinit var nMap: NaverMap
    var optionClicked = Array<Int>(8, {0})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fm = childFragmentManager
        val initialMapOption = NaverMapOptions()
            .camera(CameraPosition(LatLng(37.541618, 127.079374), 16.0))
            .mapType(NaverMap.MapType.Basic)
        val mapFragment = fm.findFragmentById(R.id.mapView) as com.naver.maps.map.MapFragment?
            ?: com.naver.maps.map.MapFragment.newInstance(initialMapOption).also {
                fm.beginTransaction().add(R.id.mapView, it).commit()
            }
        mapFragment.getMapAsync(this@MapFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawerOpenImageView = view.findViewById(R.id.sidemenuBtn)
        drawerCloseImageView = activity?.findViewById(R.id.closeBtn) ?: return
        drawerOpenImageView.setOnClickListener {
            val mainActivity = activity as? MainActivity
            mainActivity?.openDrawer()
        }

        drawerCloseImageView.setOnClickListener {
            val mainActivity = activity as? MainActivity
            mainActivity?.closeDrawer()
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun init() {
        binding.apply {
            // 지도 옵션 오른쪽 화살표 버튼 클릭 시 펼치기
            mapOpenOptionBtn.setOnClickListener {
                mapOptionLayout.visibility = View.VISIBLE
                mapOptionBarLayout.visibility = View.GONE
            }
            mapCloseOptionBtn.setOnClickListener {
                mapOptionBarLayout.visibility = View.VISIBLE
                mapOptionLayout.visibility = View.GONE
            }

            // 돋보기 버튼 클릭 시 searchActivity로 화면 전환
            searchBtn.setOnClickListener {
                var i = Intent(activity, SearchActivity::class.java)
                startActivity(i)
            }
            mapOption1.setOnClickListener {
                if (optionClicked[0] == 0) {
                    mapOption1.setTextColor(R.color.main_blue)
                    mapOption1.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[0] = 1;
                } else {
                    mapOption1.setTextColor(Color.parseColor("#000000"))
                    mapOption1.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[0] = 0;
                }
            }

            mapOption2.setOnClickListener {
                if (optionClicked[1] == 0) {
                    mapOption2.setTextColor(R.color.main_blue)
                    mapOption2.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[1] = 1;
                } else {
                    mapOption2.setTextColor(Color.parseColor("#000000"))
                    mapOption2.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[1] = 0;
                }
            }

            mapOption3.setOnClickListener {
                if (optionClicked[2] == 0) {
                    mapOption3.setTextColor(R.color.main_blue)
                    mapOption3.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[2] = 1;
                } else {
                    mapOption3.setTextColor(Color.parseColor("#000000"))
                    mapOption3.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[2] = 0;
                }
            }

            mapOption4.setOnClickListener {
                if (optionClicked[3] == 0) {
                    mapOption4.setTextColor(R.color.main_blue)
                    mapOption4.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[3] = 1;
                } else {
                    mapOption4.setTextColor(Color.parseColor("#000000"))
                    mapOption4.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[3] = 0;
                }
            }

            mapOption5.setOnClickListener {
                if (optionClicked[4] == 0) {
                    mapOption5.setTextColor(R.color.main_blue)
                    mapOption5.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[4] = 1;
                } else {
                    mapOption5.setTextColor(Color.parseColor("#000000"))
                    mapOption5.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[4] = 0;
                }
            }

            mapOption6.setOnClickListener {
                if (optionClicked[5] == 0) {
                    mapOption6.setTextColor(R.color.main_blue)
                    mapOption6.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[5] = 1;
                } else {
                    mapOption6.setTextColor(Color.parseColor("#000000"))
                    mapOption6.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[5] = 0;
                }
            }

            mapOption7.setOnClickListener {
                if (optionClicked[6] == 0) {
                    mapOption7.setTextColor(R.color.main_blue)
                    mapOption7.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[6] = 1;
                } else {
                    mapOption7.setTextColor(Color.parseColor("#000000"))
                    mapOption7.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[6] = 0;
                }
            }

            mapOption8.setOnClickListener {
                if (optionClicked[7] == 0) {
                    mapOption8.setTextColor(R.color.main_blue)
                    mapOption8.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[7] = 1;
                } else {
                    mapOption8.setTextColor(Color.parseColor("#000000"))
                    mapOption8.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[7] = 0;
                }
            }

        }
    }

    @UiThread
    // 다음은 OnMapReadyCallback을 등록해 NaverMap 객체를 얻어오는 예제입니다.
    /*
    MapFragment 및 MapView는 지도에 대한 뷰 역할만을 담당하므로 API를 호출하려면 인터페이스 역할을 하는 NaverMap 객체가 필요합니다.
    MapFragment 또는 MapView의 getMapAsync() 메서드로 OnMapReadyCallback을 등록하면 비동기로 NaverMap 객체를 얻을 수 있습니다.
    NaverMap 객체가 준비되면 onMapReady() 콜백 메서드가 호출됩니다.
     */
    override fun onMapReady(naverMap: NaverMap) {
        // ...
        println("onMapReady!!!")
        nMap = naverMap
    }
}