package com.example.dongsan2mong

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dongsan2mong.databinding.FragmentMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*

class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentMapBinding
    lateinit var drawerOpenImageView: ImageView
    lateinit var drawerCloseImageView: ImageView
    lateinit var nMap: NaverMap
    var optionClicked = Array<Int>(8, {0})
    var optionSelected = false
    val areaSelected: ArrayList<String> = ArrayList()
    lateinit var areaSelectedAdapter: MapSelectedAreaAdapter
    lateinit var seoulAdapter: SeoulAdapter
    lateinit var deeperAdapter: SeoulAdapter

    lateinit var seoulArr : Array<String>
    lateinit var deeperArr : Array<String>

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
            // recyclerView 연결
            mapAreaRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            seoulRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            deeperRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            areaSelectedAdapter = MapSelectedAreaAdapter(areaSelected)

            areaSelectedAdapter.itemClickListener = object : MapSelectedAreaAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int) {
                    areaSelectedAdapter.removeItem(position)
                    areaSelectedAdapter.notifyDataSetChanged()
                }
            }

            mapAreaRecyclerView.adapter = areaSelectedAdapter

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

            seoulArr = resources.getStringArray(R.array.spinner_region_seoul)
            seoulAdapter = SeoulAdapter(seoulArr)
            deeperArr = resources.getStringArray(R.array.spinner_region_seoul_gangnam)
            deeperAdapter = SeoulAdapter(deeperArr)

            var seoulPos : Int = 0
            seoulAdapter.itemClickListener = object : SeoulAdapter.OnItemClickListener {
                override fun OnItemClick(p1: Int) {
                    when (p1) {
                        0 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_gangnam)
                        1 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_gangdong)
                        2 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_gangbuk)
                        3 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_gangseo)
                        4 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_gwanak)
                        5 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_gwangjin)
                        6 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_guro)
                        7 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_geumcheon)
                        8 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_nowon)
                        9 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_dobong)
                        10 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_dongdaemun)
                        11 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_dongjag)
                        12 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_mapo)
                        13 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_seodaemun)
                        14 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_seocho)
                        15 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_seongdong)
                        16 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_seongbuk)
                        17 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_songpa)
                        18 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_yangcheon)
                        19 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_yeongdeungpo)
                        20 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_yongsan)
                        21 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_eunpyeong)
                        22 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_jongno)
                        23 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_jung)
                        24 -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_jungnanggu)
                        else -> deeperArr = resources.getStringArray(R.array.spinner_region_seoul_gangnam)
                    }
                    seoulPos = p1

                    println(seoulPos)

                    deeperAdapter = SeoulAdapter(deeperArr)
                    deeperRecyclerView.adapter = deeperAdapter

                    deeperAdapter.itemClickListener = object : SeoulAdapter.OnItemClickListener {
                        override fun OnItemClick(p2: Int) {
                            val str: String = seoulArr[seoulPos] + " " + deeperArr[p2]
                            println(str)
                            areaSelected.add(str)
                            areaSelectedAdapter.notifyDataSetChanged()

                        }
                    }
                }
            }

            deeperAdapter.itemClickListener = object : SeoulAdapter.OnItemClickListener {
                override fun OnItemClick(p2: Int) {
                    val str: String = seoulArr[seoulPos] + " " + deeperArr[p2]
                    println(str)
                    areaSelected.add(str)
                    areaSelectedAdapter.notifyDataSetChanged()

                }
            }
            deeperRecyclerView.adapter = deeperAdapter
            seoulRecyclerView.adapter = seoulAdapter

            refreshAreaIcon.setOnClickListener {
                areaSelected.clear()
                areaSelectedAdapter.notifyDataSetChanged()
            }

            mapOption1.setOnClickListener {
                if (optionClicked[0] == 0) {
                    mapOption1.setTextColor(R.color.main_blue)
                    mapOption1.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption1Extend.setTextColor(R.color.main_blue)
                    mapOption1Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption1Page.visibility = View.VISIBLE
                    optionClicked[0] = 1;
//            deeperArr = resources.getStringArray(R.array)
//            deeperAdapter = MapSelectedAreaAdapter()
                } else {
                    mapOption1.setTextColor(Color.parseColor("#000000"))
                    mapOption1.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption1Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption1Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption1Page.visibility = View.GONE
                    optionClicked[0] = 0;
                }
                checkOptionSelected()

                // 미니 창 열기

            }

            mapOption5.setOnClickListener {
                if (optionClicked[4] == 0) {
                    mapOption5.setTextColor(R.color.main_blue)
                    mapOption5.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption5Extend.setTextColor(R.color.main_blue)
                    mapOption5Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[4] = 1;
                } else {
                    mapOption5.setTextColor(Color.parseColor("#000000"))
                    mapOption5.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption5Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption5Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[4] = 0;
                }
                checkOptionSelected()
            }

            mapOption2.setOnClickListener {
                if (optionClicked[1] == 0) {
                    mapOption2.setTextColor(R.color.main_blue)
                    mapOption2.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption2Extend.setTextColor(R.color.main_blue)
                    mapOption2Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[1] = 1;
                } else {
                    mapOption2.setTextColor(Color.parseColor("#000000"))
                    mapOption2.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption2Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption2Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[1] = 0;
                }
                checkOptionSelected()
            }

            mapOption6.setOnClickListener {
                if (optionClicked[5] == 0) {
                    mapOption6.setTextColor(R.color.main_blue)
                    mapOption6.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption6Extend.setTextColor(R.color.main_blue)
                    mapOption6Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[5] = 1;
                } else {
                    mapOption6.setTextColor(Color.parseColor("#000000"))
                    mapOption6.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption6Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption6Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[5] = 0;
                }
                checkOptionSelected()
            }

            mapOption3.setOnClickListener {
                if (optionClicked[2] == 0) {
                    mapOption3.setTextColor(R.color.main_blue)
                    mapOption3.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption3Extend.setTextColor(R.color.main_blue)
                    mapOption3Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[2] = 1;
                } else {
                    mapOption3.setTextColor(Color.parseColor("#000000"))
                    mapOption3.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption3Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption3Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[2] = 0;
                }
                checkOptionSelected()
            }

            mapOption1Extend.setOnClickListener {
                if (optionClicked[0] == 0) {
                    mapOption1.setTextColor(R.color.main_blue)
                    mapOption1.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption1Extend.setTextColor(R.color.main_blue)
                    mapOption1Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[0] = 1;
                } else {
                    mapOption1.setTextColor(Color.parseColor("#000000"))
                    mapOption1.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption1Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption1Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[0] = 0;
                }
                checkOptionSelected()
            }

            mapOption2Extend.setOnClickListener {
                if (optionClicked[1] == 0) {
                    mapOption2.setTextColor(R.color.main_blue)
                    mapOption2.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption2Extend.setTextColor(R.color.main_blue)
                    mapOption2Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[1] = 1;
                } else {
                    mapOption2.setTextColor(Color.parseColor("#000000"))
                    mapOption2.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption2Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption2Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[1] = 0;
                }
                checkOptionSelected()
            }

            mapOption3Extend.setOnClickListener {
                if (optionClicked[2] == 0) {
                    mapOption3.setTextColor(R.color.main_blue)
                    mapOption3.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption3Extend.setTextColor(R.color.main_blue)
                    mapOption3Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[2] = 1;
                } else {
                    mapOption3.setTextColor(Color.parseColor("#000000"))
                    mapOption3.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption3Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption3Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[2] = 0;
                }
                checkOptionSelected()
            }

            mapOption4Extend.setOnClickListener {
                if (optionClicked[3] == 0) {
                    mapOption4Extend.setTextColor(R.color.main_blue)
                    mapOption4Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[3] = 1;
                } else {
                    mapOption4Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption4Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[3] = 0;
                }
                checkOptionSelected()
            }

            mapOption5Extend.setOnClickListener {
                if (optionClicked[4] == 0) {
                    mapOption5.setTextColor(R.color.main_blue)
                    mapOption5.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption5Extend.setTextColor(R.color.main_blue)
                    mapOption5Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[4] = 1;
                } else {
                    mapOption5.setTextColor(Color.parseColor("#000000"))
                    mapOption5.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption5Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption5Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[4] = 0;
                }
                checkOptionSelected()
            }

            mapOption6Extend.setOnClickListener {
                if (optionClicked[5] == 0) {
                    mapOption6.setTextColor(R.color.main_blue)
                    mapOption6.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption6Extend.setTextColor(R.color.main_blue)
                    mapOption6Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[5] = 1;
                } else {
                    mapOption6.setTextColor(Color.parseColor("#000000"))
                    mapOption6.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption6Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption6Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[5] = 0;
                }
                checkOptionSelected()
            }

            mapOption7Extend.setOnClickListener {
                if (optionClicked[6] == 0) {
                    mapOption7Extend.setTextColor(R.color.main_blue)
                    mapOption7Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[6] = 1;
                } else {
                    mapOption7Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption7Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[6] = 0;
                }
                checkOptionSelected()
            }

            mapOption8Extend.setOnClickListener {
                if (optionClicked[7] == 0) {
                    mapOption8Extend.setTextColor(R.color.main_blue)
                    mapOption8Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[7] = 1;
                } else {
                    mapOption8Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption8Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[7] = 0;
                }
                checkOptionSelected()
            }

            initializeOption.setOnClickListener {
                if (optionSelected == false) {
                    Toast.makeText(activity, "선택된 옵션이 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    initializeOption()
                }
            }

            saveOptionAsPreset.setOnClickListener {

            }
        }
    }

    private fun checkOptionSelected(){
        optionSelected = false
        binding.apply {
            for (idx in optionClicked) {
                if (idx == 1) {
                    optionSelected = true
                    break;
                }
            }

            if (optionSelected == true) {
                saveOptionAsPreset.setBackgroundResource(R.color.main_blue)

            } else {
                saveOptionAsPreset.setBackgroundResource(R.color.gray_unselected_option)
            }
        }
    }

    private fun initializeOption() {
        for (i: Int in 0 until optionClicked.size) {
            optionClicked[i] = 0
        }
        binding.apply {
            mapOption1.setTextColor(Color.parseColor("#000000"))
            mapOption1.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption5.setTextColor(Color.parseColor("#000000"))
            mapOption5.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption2.setTextColor(Color.parseColor("#000000"))
            mapOption2.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption6.setTextColor(Color.parseColor("#000000"))
            mapOption6.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption3.setTextColor(Color.parseColor("#000000"))
            mapOption3.setBackgroundResource(R.drawable.background_map_option_expand)

            mapOption1Extend.setTextColor(Color.parseColor("#000000"))
            mapOption1Extend.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption2Extend.setTextColor(Color.parseColor("#000000"))
            mapOption2Extend.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption3Extend.setTextColor(Color.parseColor("#000000"))
            mapOption3Extend.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption4Extend.setTextColor(Color.parseColor("#000000"))
            mapOption4Extend.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption5Extend.setTextColor(Color.parseColor("#000000"))
            mapOption5Extend.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption6Extend.setTextColor(Color.parseColor("#000000"))
            mapOption6Extend.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption7Extend.setTextColor(Color.parseColor("#000000"))
            mapOption7Extend.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption8Extend.setTextColor(Color.parseColor("#000000"))
            mapOption8Extend.setBackgroundResource(R.drawable.background_map_option_expand)
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