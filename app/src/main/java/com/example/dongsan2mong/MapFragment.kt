package com.example.dongsan2mong

import android.content.Intent
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

    private fun init() {
        binding.apply {
            // 지도 옵션 오른쪽 화살표 버튼 클릭 시 펼치기
            var isOptionOpened = false
            mapOpenOptionBtn.setOnClickListener {
                if (isOptionOpened) {
                    isOptionOpened = false
                    mapOpenOptionBtn.setImageResource(R.drawable.icon_map_arrow_down)
                    mapOptionLayout.visibility = View.GONE
                } else {
                    isOptionOpened = true
                    mapOpenOptionBtn.setImageResource(R.drawable.icon_map_arrow_up)
                    mapOptionLayout.visibility = View.VISIBLE
                }
            }

            // 돋보기 버튼 클릭 시 searchActivity로 화면 전환
            searchBtn.setOnClickListener {
                var i = Intent(activity, SearchActivity::class.java)
                startActivity(i)
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