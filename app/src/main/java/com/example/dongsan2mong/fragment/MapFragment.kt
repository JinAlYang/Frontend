package com.example.dongsan2mong.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dongsan2mong.*
import com.example.dongsan2mong.R
import com.example.dongsan2mong.activity.ClusterActivity
import com.example.dongsan2mong.activity.MainActivity
import com.example.dongsan2mong.activity.SearchActivity
import com.example.dongsan2mong.adapter.MapSelectedAreaAdapter
import com.example.dongsan2mong.adapter.SeoulAdapter
import com.example.dongsan2mong.api.RetrofitBuilder
import com.example.dongsan2mong.data.HouseInfoData
import com.example.dongsan2mong.data.PresetInfoData
import com.example.dongsan2mong.data.RealEstateData
import com.example.dongsan2mong.databinding.FragmentMapBinding
import com.example.dongsan2mong.event.*
import com.google.android.material.slider.RangeSlider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentMapBinding
    lateinit var drawerOpenImageView: ImageView
    lateinit var drawerCloseImageView: ImageView
    lateinit var nMap: NaverMap
    var optionClicked = Array<Int>(8, { 0 })
    var optionSelected = false

    private lateinit var tv_markerNum: TextView
    private var route123: Int = 0
    private var markerMaxCount: Int = 16

    var markerList: ArrayList<Marker> = ArrayList<Marker>(16)
    var LBArr: ArrayList<LatLng> = ArrayList<LatLng>(16)
    var RTArr: ArrayList<LatLng> = ArrayList<LatLng>(16)
    var countNum: ArrayList<Int> = ArrayList<Int>(16)
    var allEstate: ArrayList<RealEstateData> = ArrayList<RealEstateData>()
    var seperatedEstate: ArrayList<ArrayList<RealEstateData>> =
        ArrayList<ArrayList<RealEstateData>>(16)
    var dibshomeArr: ArrayList<HouseInfoData> = ArrayList()

    val areaSelected: ArrayList<String> = ArrayList()
    lateinit var areaSelectedAdapter: MapSelectedAreaAdapter
    lateinit var seoulAdapter: SeoulAdapter
    lateinit var deeperAdapter: SeoulAdapter
    lateinit var seoulArr: Array<String>
    lateinit var deeperArr: Array<String>
    var selectedHashMap = HashMap<String, Pair<Int, Int>>()

    var isSelectedArea = arrayOf<BooleanArray>(
        BooleanArray(14),
        BooleanArray(9),
        BooleanArray(4),
        BooleanArray(13),
        BooleanArray(3),
        BooleanArray(7),
        BooleanArray(10),
        BooleanArray(3),
        BooleanArray(5),
        BooleanArray(4),
        BooleanArray(10),
        BooleanArray(9),
        BooleanArray(26),
        BooleanArray(20),
        BooleanArray(10),
        BooleanArray(17),
        BooleanArray(39),
        BooleanArray(13),
        BooleanArray(3),
        BooleanArray(34),
        BooleanArray(36),
        BooleanArray(11),
        BooleanArray(87),
        BooleanArray(74),
        BooleanArray(6)
    )

    var isSelectedSeoulArea = Array<Boolean>(25) { false }

    lateinit var tempArr: Array<Boolean>

    // 선택된 지역 카운트 변수
    var numOfSelectedArea: Int = 0
    var firstSelectedArea: String = ""

    // 매매유형 옵션 어댑터

    // 방갯수 옵션 어댑터
    lateinit var roomNumSelectedAdapter: MapSelectedAreaAdapter
    val roomNumSelected: ArrayList<String> = ArrayList()

    lateinit var roomNumAdapter: SeoulAdapter
    lateinit var roomNumArr: Array<String>
    var isSelectedRoomNum = Array<Boolean>(7) { false }

    // 선택된 roomType
    var numOfSelectedRoomNum: Int = 0
    var firstSelectedRoomNum: String = ""

    // 편의시설 옵션 어댑터
    lateinit var convTypeSelectedAdapter: MapSelectedAreaAdapter
    val convTypeSelected: ArrayList<String> = ArrayList()

    lateinit var convTypeAdapter: SeoulAdapter
    lateinit var convTypeArr: Array<String>
    var isSelectedConvType = Array<Boolean>(5) { false }

    // 선택된 convType
    var numOfSelectedConvType: Int = 0
    var firstSelectedConvType: String = ""

    // 건물유형 옵션 어댑터
    lateinit var buildTypeSelectedAdapter: MapSelectedAreaAdapter
    val buildTypeSelected: ArrayList<String> = ArrayList()

    lateinit var buildTypeAdapter: SeoulAdapter
    lateinit var buildTypeArr: Array<String>
    var isSelectedBuildType = Array<Boolean>(4) { false }

    // 선택된 buildType
    var numOfSelectedBuildType: Int = 0
    var firstSelectedBuildType: String = ""

    // 평형 옵션 어댑터

    // 층 수 옵션 어댑터
    lateinit var floorNumSelectedAdapter: MapSelectedAreaAdapter
    val floorNumSelected: ArrayList<String> = ArrayList()

    lateinit var floorNumAdapter: SeoulAdapter
    lateinit var floorNumArr: Array<String>
    var isSelectedFloorNum = Array<Boolean>(3) { false }

    // 선택된 floorNum
    var numOfSelectedFloorNum: Int = 0
    var firstSelectedFloorNum: String = ""

    // 옵션 옵션 어댑터 (inclusive)
    lateinit var inclTypeSelectedAdapter: MapSelectedAreaAdapter
    val inclTypeSelected: ArrayList<String> = ArrayList()

    lateinit var inclTypeAdapter: SeoulAdapter
    lateinit var inclTypeArr: Array<String>
    var isSelectedInclType = Array<Boolean>(7) { false }

    // 선택된 inclType
    var numOfSelectedInclType: Int = 0
    var firstSelectedInclType: String = ""

    // http 통신용 데이터
    var presetInfo = PresetInfoData()
    // var RealEstateInfoInMap = ArrayList<RealEstateData>()

    var LBLatitude: String = ""
    var LBLongitude: String = ""
    var RTLatitude: String = ""
    var RTLongitude: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fm = childFragmentManager
        val initialMapOption = NaverMapOptions()
            .camera(CameraPosition(LatLng(37.563, 127.08), 15.0))
            .mapType(NaverMap.MapType.Basic)
        val mapFragment =
            fm.findFragmentById(R.id.mapView) as com.naver.maps.map.MapFragment?
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
        drawerCloseImageView =
            activity?.findViewById(R.id.closeBtn) ?: return
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
            mapAreaRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            seoulRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            deeperRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            mapSaleTypeRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            saleTypeRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            mapRoomNumRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            roomNumRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            mapConvTypeRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            convTypeRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            mapBuildTypeRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            buildTypeRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            mapSpaceTypeRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            // spaceTypeRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            mapFloorNumRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            floorNumRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            mapInclTypeRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            inclTypeRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


            roomNumSelectedAdapter = MapSelectedAreaAdapter(roomNumSelected)
            roomNumSelectedAdapter.itemClickListener = object :
                MapSelectedAreaAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int, item: String) {
                    roomNumSelectedAdapter.removeItem(position)
                    isSelectedRoomNum[position] = false
                    roomNumAdapter.notifyDataSetChanged()
                    if (position == roomNumAdapter.selectedPosition) {
                        roomNumAdapter.selectedPosition = -1
                    }

                    roomNumSelectedAdapter.notifyDataSetChanged()
                }
            }
            mapRoomNumRecyclerView.adapter = roomNumSelectedAdapter

            roomNumArr = resources.getStringArray(R.array.roomNum)
            roomNumAdapter = SeoulAdapter(roomNumArr, isSelectedRoomNum)
            roomNumAdapter.selectedPosition = -1
            roomNumAdapter.itemClickListener = object : SeoulAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int) {
                    if (!isSelectedRoomNum[position]) {
                        isSelectedRoomNum[position] = true
                        roomNumAdapter.notifyItemChanged(position)

                        roomNumSelected.add(roomNumArr[position])
                        roomNumSelectedAdapter.notifyDataSetChanged()
                        if (numOfSelectedRoomNum == 0)
                            firstSelectedRoomNum = roomNumArr[position]
                        numOfSelectedRoomNum++
                        mapOption3.text =
                            firstSelectedRoomNum + " +" + numOfSelectedRoomNum.toString()
                    }
                    roomNumAdapter.selectedPosition = position
                    roomNumAdapter.notifyDataSetChanged()
                }
            }
            roomNumAdapter.notifyDataSetChanged()
            roomNumRecyclerView.adapter = roomNumAdapter

            /*
            mapRoomRecyclerView 어댑터 설정하세요~
             */

            convTypeSelectedAdapter = MapSelectedAreaAdapter(convTypeSelected)
            convTypeSelectedAdapter.itemClickListener = object :
                MapSelectedAreaAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int, item: String) {
                    convTypeSelectedAdapter.removeItem(position)
                    isSelectedConvType[position] = false
                    if (position == convTypeAdapter.selectedPosition) {
                        convTypeAdapter.selectedPosition = -1
                    }
                    convTypeAdapter.notifyDataSetChanged()
                    convTypeSelectedAdapter.notifyDataSetChanged()
                }
            }
            mapConvTypeRecyclerView.adapter = convTypeSelectedAdapter

            convTypeArr = resources.getStringArray(R.array.convType)
            convTypeAdapter = SeoulAdapter(convTypeArr, isSelectedConvType)
            convTypeAdapter.selectedPosition = -1
            convTypeAdapter.itemClickListener = object : SeoulAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int) {
                    if (!isSelectedConvType[position]) {
                        isSelectedConvType[position] = true
                        convTypeAdapter.notifyItemChanged(position)

                        convTypeSelected.add(convTypeArr[position])
                        convTypeSelectedAdapter.notifyDataSetChanged()
                        if (numOfSelectedConvType == 0)
                            firstSelectedConvType = convTypeArr[position]
                        numOfSelectedConvType++
                        mapOption4.text =
                            firstSelectedConvType + " +" + numOfSelectedConvType.toString()
                    }
                    convTypeAdapter.selectedPosition = position
                    convTypeAdapter.notifyDataSetChanged()
                }
            }
            convTypeAdapter.notifyDataSetChanged()
            convTypeRecyclerView.adapter = convTypeAdapter

            buildTypeSelectedAdapter = MapSelectedAreaAdapter(buildTypeSelected)
            buildTypeSelectedAdapter.itemClickListener = object :
                MapSelectedAreaAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int, item: String) {
                    buildTypeSelectedAdapter.removeItem(position)
                    isSelectedBuildType[position] = false
                    if (position == buildTypeAdapter.selectedPosition) {
                        buildTypeAdapter.selectedPosition = -1
                    }
                    buildTypeAdapter.notifyDataSetChanged()
                    buildTypeSelectedAdapter.notifyDataSetChanged()
                }
            }
            mapBuildTypeRecyclerView.adapter = buildTypeSelectedAdapter

            buildTypeArr = resources.getStringArray(R.array.buildType)
            buildTypeAdapter = SeoulAdapter(buildTypeArr, isSelectedBuildType)
            buildTypeAdapter.selectedPosition = -1
            buildTypeAdapter.itemClickListener = object : SeoulAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int) {
                    if (!isSelectedBuildType[position]) {
                        isSelectedBuildType[position] = true
                        buildTypeAdapter.notifyItemChanged(position)

                        buildTypeSelected.add(buildTypeArr[position])
                        buildTypeSelectedAdapter.notifyDataSetChanged()
                        if (numOfSelectedBuildType == 0)
                            firstSelectedBuildType = buildTypeArr[position]
                        numOfSelectedBuildType++
                        mapOption5.text =
                            firstSelectedBuildType + " +" + numOfSelectedBuildType.toString()
                    }
                    buildTypeAdapter.selectedPosition = position
                    buildTypeAdapter.notifyDataSetChanged()
                }
            }
            buildTypeAdapter.notifyDataSetChanged()

            buildTypeRecyclerView.adapter = buildTypeAdapter

            /*
            spaceTypeAdapter 구현하세여~
             */

            floorNumSelectedAdapter = MapSelectedAreaAdapter(floorNumSelected)
            floorNumSelectedAdapter.itemClickListener = object :
                MapSelectedAreaAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int, item: String) {
                    floorNumSelectedAdapter.removeItem(position)
                    isSelectedFloorNum[position] = false
                    if (position == floorNumAdapter.selectedPosition) {
                        floorNumAdapter.selectedPosition = -1
                    }
                    floorNumAdapter.notifyDataSetChanged()
                    floorNumSelectedAdapter.notifyDataSetChanged()
                }
            }
            mapFloorNumRecyclerView.adapter = floorNumSelectedAdapter

            floorNumArr = resources.getStringArray(R.array.floorNum)
            floorNumAdapter = SeoulAdapter(floorNumArr, isSelectedFloorNum)
            floorNumAdapter.selectedPosition = -1
            floorNumAdapter.itemClickListener = object : SeoulAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int) {
                    if (!isSelectedFloorNum[position]) {
                        isSelectedFloorNum[position] = true
                        floorNumAdapter.notifyItemChanged(position)

                        floorNumSelected.add(floorNumArr[position])
                        floorNumSelectedAdapter.notifyDataSetChanged()
                        if (numOfSelectedFloorNum == 0)
                            firstSelectedFloorNum = floorNumArr[position]
                        numOfSelectedFloorNum++
                        mapOption7.text =
                            firstSelectedFloorNum + " +" + numOfSelectedFloorNum.toString()
                    }
                    floorNumAdapter.selectedPosition = position
                    floorNumAdapter.notifyDataSetChanged()
                }
            }
            floorNumAdapter.notifyDataSetChanged()
            floorNumRecyclerView.adapter = floorNumAdapter

            inclTypeSelectedAdapter = MapSelectedAreaAdapter(inclTypeSelected)
            inclTypeSelectedAdapter.itemClickListener = object :
                MapSelectedAreaAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int, item: String) {
                    inclTypeSelectedAdapter.removeItem(position)
                    isSelectedInclType[position] = false
                    if (position == inclTypeAdapter.selectedPosition) {
                        inclTypeAdapter.selectedPosition = -1
                    }
                    inclTypeAdapter.notifyDataSetChanged()
                    inclTypeSelectedAdapter.notifyDataSetChanged()
                }
            }
            mapInclTypeRecyclerView.adapter = inclTypeSelectedAdapter

            inclTypeArr = resources.getStringArray(R.array.inclType)
            inclTypeAdapter = SeoulAdapter(inclTypeArr, isSelectedInclType)
            inclTypeAdapter.selectedPosition = -1
            inclTypeAdapter.itemClickListener = object : SeoulAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int) {
                    if (!isSelectedInclType[position]) {
                        isSelectedInclType[position] = true
                        inclTypeAdapter.notifyItemChanged(position)

                        inclTypeSelected.add(inclTypeArr[position])
                        inclTypeSelectedAdapter.notifyDataSetChanged()
                        if (numOfSelectedInclType == 0)
                            firstSelectedInclType = inclTypeArr[position]
                        numOfSelectedInclType++
                        mapOption8.text =
                            firstSelectedInclType + " +" + numOfSelectedInclType.toString()
                    }
                    inclTypeAdapter.selectedPosition = position
                    inclTypeAdapter.notifyDataSetChanged()
                }
            }
            inclTypeAdapter.notifyDataSetChanged()
            inclTypeRecyclerView.adapter = inclTypeAdapter

            areaSelectedAdapter = MapSelectedAreaAdapter(areaSelected)

            // 선택한 구, 동 정보가 담기는 리사이클러뷰의 아이템을 클릭했을 때 해당 아이템을 삭제해주는 이벤트
            areaSelectedAdapter.itemClickListener = object :
                MapSelectedAreaAdapter.OnItemClickListener {
                override fun OnItemClick(position: Int, item: String) {
                    areaSelectedAdapter.removeItem(position)

                    var first = selectedHashMap.get(item)?.first
                    var second = selectedHashMap.get(item)?.second
                    if (first != null && second != null) {
                        isSelectedArea[first][second] = false

                        if (second == deeperAdapter.selectedPosition) {
                            deeperAdapter.selectedPosition = -1
                        }
                        tempArr[second] = false
                        deeperAdapter.notifyDataSetChanged()
                    }

                    selectedHashMap.remove(item)
                    // 아이템 삭제 후 지역 옵션 텍스트뷰 텍스트 변환 작업
                    numOfSelectedArea--
                    if (numOfSelectedArea == 0) {
                        mapOption1.text = getString(R.string.map_option_1)
                    } else
                        mapOption1.text = firstSelectedArea + " +" + numOfSelectedArea.toString()

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

            seoulArr =
                resources.getStringArray(R.array.spinner_region_seoul)
            seoulAdapter = SeoulAdapter(seoulArr, isSelectedSeoulArea)
            deeperArr =
                resources.getStringArray(R.array.spinner_region_seoul_gangnam)
            tempArr = isSelectedArea[0].toTypedArray()
            deeperAdapter = SeoulAdapter(deeperArr, tempArr)

            var seoulPos: Int = 0
            seoulAdapter.selectedPosition = seoulPos
            seoulAdapter.itemClickListener = object : SeoulAdapter.OnItemClickListener {
                override fun OnItemClick(p1: Int) {
                    when (p1) {
                        0 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_gangnam)
                        }
                        1 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_gangdong)
                        }
                        2 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_gangbuk)
                        }
                        3 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_gangseo)
                        }
                        4 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_gwanak)
                        }
                        5 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_gwangjin)
                        }
                        6 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_guro)
                        }
                        7 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_geumcheon)
                        }
                        8 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_nowon)
                        }
                        9 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_dobong)
                        }
                        10 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_dongdaemun)
                        }
                        11 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_dongjag)
                        }
                        12 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_mapo)
                        }
                        13 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_seodaemun)
                        }
                        14 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_seocho)
                        }
                        15 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_seongdong)
                        }
                        16 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_seongbuk)
                        }
                        17 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_songpa)
                        }
                        18 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_yangcheon)
                        }
                        19 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_yeongdeungpo)
                        }
                        20 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_yongsan)
                        }
                        21 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_eunpyeong)
                        }
                        22 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_jongno)
                        }
                        23 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_jung)
                        }
                        24 -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_jungnanggu)
                        }
                        else -> {
                            deeperArr =
                                resources.getStringArray(R.array.spinner_region_seoul_gangnam)
                        }
                    }
                    seoulAdapter.selectedPosition = p1
                    seoulAdapter.notifyDataSetChanged()
                    seoulPos = p1

                    println(seoulPos)

                    tempArr = isSelectedArea[seoulPos].toTypedArray()
                    deeperAdapter = SeoulAdapter(deeperArr, tempArr)
                    deeperRecyclerView.adapter = deeperAdapter

                    deeperAdapter.itemClickListener = object : SeoulAdapter.OnItemClickListener {
                        override fun OnItemClick(p2: Int) {
                            if (!isSelectedArea[seoulPos][p2]) {
                                val str: String = seoulArr[seoulPos] + " " + deeperArr[p2]
                                println(str)
                                isSelectedArea[seoulPos][p2] = true
                                tempArr[p2] = true
                                // temp
                                deeperAdapter.notifyItemChanged(p2)


                                selectedHashMap.put(str, Pair(seoulPos, p2))
                                areaSelected.add(str)
                                areaSelectedAdapter.notifyDataSetChanged()
                                if (numOfSelectedArea == 0)
                                    firstSelectedArea = str
                                numOfSelectedArea++ // 카운트 증가
                                mapOption1.text = firstSelectedArea + " +" +
                                        numOfSelectedArea.toString()
                            }
                            deeperAdapter.selectedPosition = p2
                            deeperAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }

            deeperAdapter.itemClickListener = object : SeoulAdapter.OnItemClickListener {
                override fun OnItemClick(p2: Int) {
                    if (!isSelectedArea[seoulPos][p2]) {
                        val str: String = seoulArr[seoulPos] + " " + deeperArr[p2]
                        println(str)
                        isSelectedArea[seoulPos][p2] = true
                        tempArr[p2] = true

                        // temp
                        deeperAdapter.notifyItemChanged(p2)

                        selectedHashMap.put(str, Pair(seoulPos, p2))
                        areaSelected.add(str)
                        areaSelectedAdapter.notifyDataSetChanged()
                        if (numOfSelectedArea == 0)
                            firstSelectedArea = str
                        numOfSelectedArea++ // 카운트 증가
                        mapOption1.text = firstSelectedArea + " +" +
                                numOfSelectedArea.toString()
                    }
                    deeperAdapter.selectedPosition = p2
                    deeperAdapter.notifyDataSetChanged()
                }
            }
            deeperRecyclerView.adapter = deeperAdapter
            seoulRecyclerView.adapter = seoulAdapter

            refreshAreaIcon.setOnClickListener {
                areaSelected.clear()
                numOfSelectedArea = 0 // 카운트 0으로 초기화
                mapOption1.text = getString(R.string.map_option_1)
                selectedHashMap.clear()
                for (i in isSelectedArea.indices) {
                    isSelectedArea[i].fill(false)
                }
                tempArr.fill(false)
                deeperAdapter.notifyDataSetChanged()
                deeperAdapter.selectedPosition = -1
                areaSelectedAdapter.notifyDataSetChanged()
            }

            refreshAreaIcon2.setOnClickListener {
                /*
                saleType에서 refreshAreaIcon2 클릭하면~
                 */
                saleTypeSlider1.setValues(0.0f, 30.0f)
                saleTypeSliderPrice1.text = "전체"
                saleTypeSlider2.setValues(0.0f, 18.0f)
                saleTypeSliderPrice2.text = "전체"
                mapSaleTypeBtn1.isSelected = true
                mapSaleTypeBtn2.isSelected = false
                mapSaleTypeBtn3.isSelected = false
                saleTypeLinear.visibility = View.VISIBLE
            }

            refreshAreaIcon3.setOnClickListener {
                roomNumSelected.clear()
                numOfSelectedRoomNum = 0
                mapOption2.text = getString(R.string.map_option_3)
                isSelectedRoomNum.fill(false)
                roomNumAdapter.selectedPosition = -1
                roomNumAdapter.notifyDataSetChanged()
                roomNumSelectedAdapter.notifyDataSetChanged()
            }

            refreshAreaIcon4.setOnClickListener {
                convTypeSelected.clear()
                numOfSelectedConvType = 0
                mapOption4.text = getString(R.string.map_option_4)
                isSelectedConvType.fill(false)
                convTypeAdapter.selectedPosition = -1
                convTypeAdapter.notifyDataSetChanged()
                convTypeSelectedAdapter.notifyDataSetChanged()
            }

            refreshAreaIcon5.setOnClickListener {
                buildTypeSelected.clear()
                numOfSelectedBuildType = 0
                mapOption5.text = getString(R.string.map_option_5)
                isSelectedBuildType.fill(false)
                buildTypeAdapter.selectedPosition = -1
                buildTypeAdapter.notifyDataSetChanged()
                buildTypeSelectedAdapter.notifyDataSetChanged()
            }

            refreshAreaIcon6.setOnClickListener {
                /*
                spaceType에서 refreshAreaIcon6 클릭하면~
                 */
                spaceTypeSlider.setValues(0.0f, 6.0f)
                spaceTypeSizeText.text = "전체"
            }

            refreshAreaIcon7.setOnClickListener {
                floorNumSelected.clear()
                numOfSelectedFloorNum = 0
                mapOption7.text = getString(R.string.map_option_7)
                isSelectedFloorNum.fill(false)
                floorNumAdapter.selectedPosition = -1
                floorNumAdapter.notifyDataSetChanged()
                floorNumSelectedAdapter.notifyDataSetChanged()
            }

            refreshAreaIcon8.setOnClickListener {
                inclTypeSelected.clear()
                numOfSelectedInclType = 0
                mapOption8.text = getString(R.string.map_option_8)
                isSelectedInclType.fill(false)
                inclTypeAdapter.selectedPosition = -1
                inclTypeAdapter.notifyDataSetChanged()
                inclTypeSelectedAdapter.notifyDataSetChanged()
            }

            mapOption1.setOnClickListener {
                if (optionClicked[0] == 0) {
                    mapOption1.setTextColor(R.color.main_blue)
                    mapOption1.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption1Extend.setTextColor(R.color.main_blue)
                    mapOption1Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    allOptionPageGone()
                    mapOption1Page.visibility = View.VISIBLE
                    optionClicked[0] = 1
//            deeperArr = resources.getStringArray(R.array)
//            deeperAdapter = MapSelectedAreaAdapter()
                } else {
                    mapOption1.setTextColor(Color.parseColor("#000000"))
                    mapOption1.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption1Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption1Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption1Page.visibility = View.GONE
                    optionClicked[0] = 0
                }
                checkOptionSelected()

                // 미니 창 열기

            }

            mapOption2.setOnClickListener {
                if (optionClicked[1] == 0) {
                    mapOption2.setTextColor(R.color.main_blue)
                    mapOption2.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption2Extend.setTextColor(R.color.main_blue)
                    mapOption2Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[1] = 1
                    allOptionPageGone()
                    mapOption2Page.visibility = View.VISIBLE
                } else {
                    mapOption2.setTextColor(Color.parseColor("#000000"))
                    mapOption2.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption2Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption2Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[1] = 0
                    mapOption2Page.visibility = View.GONE
                }
                checkOptionSelected()
            }

            mapOption3.setOnClickListener {
                if (optionClicked[2] == 0) {
                    mapOption3.setTextColor(R.color.main_blue)
                    mapOption3.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption3Extend.setTextColor(R.color.main_blue)
                    mapOption3Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    allOptionPageGone()
                    mapOption3Page.visibility = View.VISIBLE
                    optionClicked[2] = 1
                } else {
                    mapOption3.setTextColor(Color.parseColor("#000000"))
                    mapOption3.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption3Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption3Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption3Page.visibility = View.GONE
                    optionClicked[2] = 0
                }
                checkOptionSelected()
            }

            mapOption4.setOnClickListener {
                if (optionClicked[3] == 0) {
                    mapOption4.setTextColor(R.color.main_blue)
                    mapOption4.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption4Extend.setTextColor(R.color.main_blue)
                    mapOption4Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    allOptionPageGone()
                    mapOption4Page.visibility = View.VISIBLE
                    optionClicked[3] = 1
                } else {
                    mapOption4.setTextColor(Color.parseColor("#000000"))
                    mapOption4.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption4Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption4Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption4Page.visibility = View.GONE
                    optionClicked[3] = 0
                }
                checkOptionSelected()
            }

            mapOption5.setOnClickListener {
                if (optionClicked[4] == 0) {
                    mapOption5.setTextColor(R.color.main_blue)
                    mapOption5.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption5Extend.setTextColor(R.color.main_blue)
                    mapOption5Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    allOptionPageGone()
                    mapOption5Page.visibility = View.VISIBLE
                    optionClicked[4] = 1
                } else {
                    mapOption5.setTextColor(Color.parseColor("#000000"))
                    mapOption5.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption5Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption5Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption5Page.visibility = View.GONE
                    optionClicked[4] = 0
                }
                checkOptionSelected()
            }

            mapOption6.setOnClickListener {
                if (optionClicked[5] == 0) {
                    mapOption6.setTextColor(R.color.main_blue)
                    mapOption6.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption6Extend.setTextColor(R.color.main_blue)
                    mapOption6Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    allOptionPageGone()
                    mapOption6Page.visibility = View.VISIBLE
                    optionClicked[5] = 1
                } else {
                    mapOption6.setTextColor(Color.parseColor("#000000"))
                    mapOption6.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption6Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption6Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption6Page.visibility = View.GONE
                    optionClicked[5] = 0
                }
                checkOptionSelected()
            }

            mapOption7.setOnClickListener {
                if (optionClicked[6] == 0) {
                    mapOption7.setTextColor(R.color.main_blue)
                    mapOption7.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption7Extend.setTextColor(R.color.main_blue)
                    mapOption7Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    allOptionPageGone()
                    mapOption7Page.visibility = View.VISIBLE
                    optionClicked[6] = 1
                } else {
                    mapOption7.setTextColor(Color.parseColor("#000000"))
                    mapOption7.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption7Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption7Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption7Page.visibility = View.GONE
                    optionClicked[6] = 0
                }
                checkOptionSelected()
            }

            mapOption8.setOnClickListener {
                if (optionClicked[7] == 0) {
                    mapOption8.setTextColor(R.color.main_blue)
                    mapOption8.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption8Extend.setTextColor(R.color.main_blue)
                    mapOption8Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    allOptionPageGone()
                    mapOption8Page.visibility = View.VISIBLE
                    optionClicked[7] = 1
                } else {
                    mapOption8.setTextColor(Color.parseColor("#000000"))
                    mapOption8.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption8Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption8Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption8Page.visibility = View.GONE
                    optionClicked[7] = 0
                }
                checkOptionSelected()
            }

            mapOption1Extend.setOnClickListener {
                if (optionClicked[0] == 0) {
                    mapOption1.setTextColor(R.color.main_blue)
                    mapOption1.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption1Extend.setTextColor(R.color.main_blue)
                    mapOption1Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[0] = 1
                } else {
                    mapOption1.setTextColor(Color.parseColor("#000000"))
                    mapOption1.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption1Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption1Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[0] = 0
                }
                checkOptionSelected()
            }

            mapOption2Extend.setOnClickListener {
                if (optionClicked[1] == 0) {
                    mapOption2.setTextColor(R.color.main_blue)
                    mapOption2.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption2Extend.setTextColor(R.color.main_blue)
                    mapOption2Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[1] = 1
                } else {
                    mapOption2.setTextColor(Color.parseColor("#000000"))
                    mapOption2.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption2Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption2Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[1] = 0
                }
                checkOptionSelected()
            }

            mapOption3Extend.setOnClickListener {
                if (optionClicked[2] == 0) {
                    mapOption3.setTextColor(R.color.main_blue)
                    mapOption3.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption3Extend.setTextColor(R.color.main_blue)
                    mapOption3Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[2] = 1
                } else {
                    mapOption3.setTextColor(Color.parseColor("#000000"))
                    mapOption3.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption3Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption3Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[2] = 0
                }
                checkOptionSelected()
            }

            mapOption4Extend.setOnClickListener {
                if (optionClicked[3] == 0) {
                    mapOption4Extend.setTextColor(R.color.main_blue)
                    mapOption4Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[3] = 1
                } else {
                    mapOption4Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption4Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[3] = 0
                }
                checkOptionSelected()
            }

            mapOption5Extend.setOnClickListener {
                if (optionClicked[4] == 0) {
                    mapOption5.setTextColor(R.color.main_blue)
                    mapOption5.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption5Extend.setTextColor(R.color.main_blue)
                    mapOption5Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[4] = 1
                } else {
                    mapOption5.setTextColor(Color.parseColor("#000000"))
                    mapOption5.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption5Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption5Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[4] = 0
                }
                checkOptionSelected()
            }

            mapOption6Extend.setOnClickListener {
                if (optionClicked[5] == 0) {
                    mapOption6.setTextColor(R.color.main_blue)
                    mapOption6.setBackgroundResource(R.drawable.background_map_option_selected)
                    mapOption6Extend.setTextColor(R.color.main_blue)
                    mapOption6Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[5] = 1
                } else {
                    mapOption6.setTextColor(Color.parseColor("#000000"))
                    mapOption6.setBackgroundResource(R.drawable.background_map_option_expand)
                    mapOption6Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption6Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[5] = 0
                }
                checkOptionSelected()
            }

            mapOption7Extend.setOnClickListener {
                if (optionClicked[6] == 0) {
                    mapOption7Extend.setTextColor(R.color.main_blue)
                    mapOption7Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[6] = 1
                } else {
                    mapOption7Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption7Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[6] = 0
                }
                checkOptionSelected()
            }

            mapOption8Extend.setOnClickListener {
                if (optionClicked[7] == 0) {
                    mapOption8Extend.setTextColor(R.color.main_blue)
                    mapOption8Extend.setBackgroundResource(R.drawable.background_map_option_selected)
                    optionClicked[7] = 1
                } else {
                    mapOption8Extend.setTextColor(Color.parseColor("#000000"))
                    mapOption8Extend.setBackgroundResource(R.drawable.background_map_option_expand)
                    optionClicked[7] = 0
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
                println("저장 버튼 클릭")
                // savePresetHttp()
                val mainActivity = requireActivity() as MainActivity
                mainActivity.changeFragment(WishlistFragment())
            }
        }

//        binding.apply {
//            var list = arrayListOf(1, 2, 3, 4, 5, 0, 7, 8, 9, 10, 11, 12, 13, 0, 15, 16)
//            var listManager = GridLayoutManager(context, 4)
//            var listAdapter = MapGridViewAdapter(list)
//
//            var recyclerList = mapGridView.apply {
//                setHasFixedSize(true)
//                layoutManager = listManager
//                adapter = listAdapter
//
//            }
//        }
        initRangeSlider()
    }

    private fun checkOptionSelected() {
        optionSelected = false
        binding.apply {
            for (idx in optionClicked) {
                if (idx == 1) {
                    optionSelected = true
                    break
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
            mapOption2.setTextColor(Color.parseColor("#000000"))
            mapOption2.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption3.setTextColor(Color.parseColor("#000000"))
            mapOption3.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption4.setTextColor(Color.parseColor("#000000"))
            mapOption4.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption5.setTextColor(Color.parseColor("#000000"))
            mapOption5.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption6.setTextColor(Color.parseColor("#000000"))
            mapOption6.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption7.setTextColor(Color.parseColor("#000000"))
            mapOption7.setBackgroundResource(R.drawable.background_map_option_expand)
            mapOption8.setTextColor(Color.parseColor("#000000"))
            mapOption8.setBackgroundResource(R.drawable.background_map_option_expand)


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

    private fun initRangeSlider() {
        binding.apply {
            mapSaleTypeBtn1.isSelected = true
            mapSaleTypeBtn1.setOnClickListener {
                println("mapSaleTypeBtn1")
                if (!mapSaleTypeBtn1.isSelected) {
                    println("mapSaleTypeBtn1!!")
                    mapSaleTypeBtn1.isSelected = true
                    mapSaleTypeBtn2.isSelected = false
                    mapSaleTypeBtn3.isSelected = false
                    saleTypeSliderText.text = "보증금"
                    saleTypeLinear.visibility = View.VISIBLE
                }
            }

            mapSaleTypeBtn2.setOnClickListener {
                println("mapSaleTypeBtn2")
                // mapSaleTypeBtn3.chec
                if (!mapSaleTypeBtn2.isSelected) {
                    println("mapSaleTypeBtn2!!")
                    mapSaleTypeBtn1.isSelected = false
                    mapSaleTypeBtn2.isSelected = true
                    mapSaleTypeBtn3.isSelected = false
                    saleTypeSliderText.text = "전세금"
                    saleTypeLinear.visibility = View.GONE
                }
            }

            mapSaleTypeBtn3.setOnClickListener {
                println("mapSaleTypeBtn3")
                if (!mapSaleTypeBtn3.isSelected) {
                    println("mapSaleTypeBtn3!!")
                    mapSaleTypeBtn1.isSelected = false
                    mapSaleTypeBtn2.isSelected = false
                    mapSaleTypeBtn3.isSelected = true
                    saleTypeLinear.visibility = View.VISIBLE
                }
            }

            val saleTypeArr1: Array<String> = resources.getStringArray(R.array.saleType1)
            val saleTypeArr2: Array<String> = resources.getStringArray(R.array.saleType2)


            saleTypeSlider1.setValues(0.0f, 30.0f)
            saleTypeSliderPrice1.text = "전체"
            saleTypeSlider1.addOnChangeListener(RangeSlider.OnChangeListener { slider, _, _ ->
                //Use the value
                var strFrom: String = saleTypeArr1[slider.values[0].toInt()]
                // var realStrFrom: String = ""

                if (strFrom.equals("0")) strFrom = "최소"
                if (strFrom.equals("1")) strFrom = "최대"
                var strTo: String = saleTypeArr1[slider.values[1].toInt()]
                if (strTo.equals("0")) strTo = "최소"
                if (strTo.equals("1")) strTo = "최대"
                var str = "$strFrom ~ $strTo"
                if (strFrom.equals("최소") && strTo.equals("최대"))
                    str = "전체"
                saleTypeSlider1.setMinSeparationValue(1.0f)
                saleTypeSliderPrice1.text = str
            })

            saleTypeSlider2.setValues(0.0f, 18.0f)
            saleTypeSliderPrice2.text = "전체"
            saleTypeSlider2.addOnChangeListener(RangeSlider.OnChangeListener { slider, _, _ ->
                //Use the value
                var strFrom: String = saleTypeArr2[slider.values[0].toInt()]
                // var realStrFrom: String = ""

                if (strFrom.equals("0")) strFrom = "최소"
                else if (strFrom.equals("1")) strFrom = "최대"
                var strTo: String = saleTypeArr2[slider.values[1].toInt()]
                if (strTo.equals("0")) strTo = "최소"
                else if (strTo.equals("1")) strTo = "최대"
                var str = "$strFrom ~ $strTo"
                if (strFrom.equals("최소") && strTo.equals("최대"))
                    str = "전체"
                saleTypeSlider2.setMinSeparationValue(1.0f)
                saleTypeSliderPrice2.text = str
            })

            spaceTypeSlider.setValues(0.0f, 6.0f)
            spaceTypeSizeText.text = "전체"
            spaceTypeSlider.addOnChangeListener(RangeSlider.OnChangeListener { slider, _, _ ->
                //Use the value
                var strFrom: String = slider.values[0].toInt().toString() + "0"
                // var realStrFrom: String = ""
                if (strFrom.equals("00")) strFrom = "최소"
                else if (strFrom.equals("60")) strFrom = "최대"
                else strFrom += "평"
                var strTo: String = slider.values[1].toInt().toString() + "0"
                if (strTo.equals("00")) strTo = "최소"
                else if (strTo.equals("60")) strTo = "최대"
                else strTo += "평"
                var str = strFrom + " ~ " + strTo
                if (strFrom.equals("최소") && strTo.equals("최대"))
                    str = "전체"
                spaceTypeSlider.setMinSeparationValue(1.0f)
                spaceTypeSizeText.text = str
            })
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
        seperatedEstate.clear()
        for (i in 0..15) {
            seperatedEstate.add(ArrayList<RealEstateData>())
        }
        nMap = naverMap

        binding.apply {

            // 카메라 움직임이 멈출 때마다 좌하단 우상단 좌표
            nMap.addOnCameraIdleListener {
                // Log.i("NaverMapMSG", "카메라 변경")
                val projection = nMap.projection
                val width: Int = mapViewFrame.width
                val height: Int = mapViewFrame.height - mapOptionBarLayout.height
                val leftBottomCoord = projection.fromScreenLocation(PointF(0f, height.toFloat()))
                val rightTopCoord = projection.fromScreenLocation(PointF(width.toFloat(), 0f))
                Log.i(
                    "NaverMapMSG",
                    "좌하단 : ${leftBottomCoord.latitude}, ${leftBottomCoord.longitude} "
                )
                Log.i("NaverMapMSG", "우상단 : ${rightTopCoord.latitude}, ${rightTopCoord.longitude} ")
                LBLatitude = leftBottomCoord.latitude.toString()
                LBLongitude = leftBottomCoord.longitude.toString()
                RTLatitude = rightTopCoord.latitude.toString()
                RTLongitude = rightTopCoord.longitude.toString()

                CoroutineScope(Dispatchers.Main).launch {
                    getHouseInfoInMapHttp { realEstateList ->
                        // realEstateList를 사용하여 원하는 작업 수행
                        // 예: 마커 생성, 리스트 업데이트 등
                        allEstate.clear() // 기존 데이터를 비우고
                        allEstate.addAll(realEstateList) // 새로운 데이터 추가

                        // 여기서 새로 받은 매물들분류해서 넣을거임
                        for (i in allEstate) {
                            if (i.latitude.toDouble() >= LBArr[0].latitude) {
                                if (i.longitude.toDouble() <= RTArr[0].longitude) {
                                    seperatedEstate[0].add(i)
                                } else if (i.longitude.toDouble() <= RTArr[1].longitude) {
                                    seperatedEstate[1].add(i)
                                } else if (i.longitude.toDouble() <= RTArr[2].longitude) {
                                    seperatedEstate[2].add(i)
                                } else {
                                    seperatedEstate[3].add(i)
                                }
                            } else if (i.latitude.toDouble() >= LBArr[4].latitude) {
                                if (i.longitude.toDouble() <= RTArr[0].longitude) {
                                    seperatedEstate[4].add(i)
                                } else if (i.longitude.toDouble() <= RTArr[1].longitude) {
                                    seperatedEstate[5].add(i)
                                } else if (i.longitude.toDouble() <= RTArr[2].longitude) {
                                    seperatedEstate[6].add(i)
                                } else {
                                    seperatedEstate[7].add(i)
                                }
                            } else if (i.latitude.toDouble() >= LBArr[8].latitude) {
                                if (i.longitude.toDouble() <= RTArr[0].longitude) {
                                    seperatedEstate[8].add(i)
                                } else if (i.longitude.toDouble() <= RTArr[1].longitude) {
                                    seperatedEstate[9].add(i)
                                } else if (i.longitude.toDouble() <= RTArr[2].longitude) {
                                    seperatedEstate[10].add(i)
                                } else {
                                    seperatedEstate[11].add(i)
                                }
                            } else {
                                if (i.latitude.toDouble() <= RTArr[0].latitude) {
                                    seperatedEstate[12].add(i)
                                } else if (i.latitude.toDouble() <= RTArr[1].latitude) {
                                    seperatedEstate[13].add(i)
                                } else if (i.latitude.toDouble() <= RTArr[2].latitude) {
                                    seperatedEstate[14].add(i)
                                } else {
                                    seperatedEstate[15].add(i)
                                }
                            }
                        }

                        for (i in 0..3) {
                            for (j in 0..3) {
                                if (seperatedEstate[i * 4 + j].size == 0)
                                    continue
                                //gridviewText.text = (i * 4 + j).toString() // 원형 마커에 숫자 대입
                                // int그대로넣으니 에러남
                                gridviewText.text = seperatedEstate[i * 4 + j].size.toString()
                                val marker = Marker()
                                val tempCoord = projection.fromScreenLocation(
                                    PointF(
                                        (width * (1.0f + 2 * j) / 8.0f),
                                        mapViewFrame.height * (1.0f + 2 * i) / 8.0f
                                    )
                                )
                                marker.position = LatLng(tempCoord.latitude, tempCoord.longitude)
                                marker.width = 200
                                marker.height = 200
                                marker.icon = OverlayImage.fromView(gridviewText)
                                marker.onClickListener = Overlay.OnClickListener {
                                    Log.d("markerLog", "${marker.position}")
                                    val clusterIntent = Intent(context, ClusterActivity::class.java)
                                    clusterIntent.putExtra(
                                        "clusterArr",
                                        seperatedEstate[i * 4 + j]
                                    )
                                    clusterIntent.putExtra(
                                        "dibshomeArr",
                                        dibshomeArr
                                    )
                                    startActivityForResult(clusterIntent, 99)

                                    true
                                }
                                markerList.add(marker)
                                marker.map = nMap
                            }
                        }

                        for (i in 0..15) {
                            // markerList[i].map = nMap
                        }

                    }
                }


                gridviewText.visibility = View.GONE

                for (i in markerList.indices) {
                    markerList[i].map = null
                }
                markerList.clear()

                LBArr.clear()
                RTArr.clear()
                for (i in 0..3) {
                    for (j in 0..3) {
                        RTArr.add(
                            projection.fromScreenLocation(
                                PointF(
                                    width * (j + 1) / 4.0f,
                                    height * i / 4.0f
                                )
                            )
                        )
                        LBArr.add(
                            projection.fromScreenLocation(
                                PointF(
                                    width * j / 4.0f,
                                    height * (i + 1) / 4.0f
                                )
                            )
                        )
                        // println(RTArr[0])
                        // println(LBArr[0])
                    }
                }
                // 여기서 기존 분류한 매물들 초기화하고,
                for (i in seperatedEstate.indices) {
                    seperatedEstate[i].clear()
                }
            }
        }
    }

    fun allOptionPageGone() {
        binding.apply {
            mapOption1Page.visibility = View.GONE
            mapOption2Page.visibility = View.GONE
            mapOption3Page.visibility = View.GONE
            mapOption4Page.visibility = View.GONE
            mapOption5Page.visibility = View.GONE
            mapOption6Page.visibility = View.GONE
            mapOption7Page.visibility = View.GONE
            mapOption8Page.visibility = View.GONE
        }
    }

    private fun savePresetHttp() {
        val addPreset = RetrofitBuilder.api.addPreset(1, presetInfo)
        addPreset.enqueue(object : Callback<PresetInfoData> {
            override fun onResponse(
                call: Call<PresetInfoData>,
                response: Response<PresetInfoData>
            ) {
                Toast.makeText(activity, "Post Success", Toast.LENGTH_LONG).show()
                if (response.isSuccessful) {
                }
            }

            override fun onFailure(call: Call<PresetInfoData>, t: Throwable) {
                Toast.makeText(activity, "Post Failed", Toast.LENGTH_LONG).show()
            }

        })
    }


    private suspend fun getHouseInfoInMapHttp(callback: (RealEstateList: ArrayList<RealEstateData>) -> Unit) {
        val location = LBLatitude + "_" + LBLongitude + "_" + RTLatitude + "_" + RTLongitude
        val getHouseInfoInMap = RetrofitBuilder.api.getRealEstateInMapWithNoOption(
            location, "null"
        )
        getHouseInfoInMap.enqueue(object : Callback<ArrayList<RealEstateData>> {
            override fun onResponse(
                call: Call<ArrayList<RealEstateData>>,
                response: Response<ArrayList<RealEstateData>>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(activity, "GET Success", Toast.LENGTH_LONG).show()
                    val realEstateList = response.body()
                    if (realEstateList != null) {
                        // Log.d("realEstateList", realEstateList.toString())
                        callback(realEstateList)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<RealEstateData>>, t: Throwable) {
                Toast.makeText(activity, "GET Failed", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                99 -> {
                    Log.d("onActivityResult", "true")
                    dibshomeArr = data?.extras?.get("returnDibshomeArr") as ArrayList<HouseInfoData>
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            EventBus.getDefault().register(this)
        } catch (e: Exception) {
        }
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
        EventBus.getDefault().post(DataEvent(2, dibshomeArr))
    }

    override fun onStop() {
        super.onStop()

        EventBus.getDefault().post(DataEvent(0, dibshomeArr))
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printData(event: DataEvent) {
        if (event.int == 1) {
            Log.d("dataEvent", "ClusterActivity to mapFragment")
            dibshomeArr = event.dibsArr
        }
        else if (event.int == 3) {
            Log.d("dataEvent", "wishlist to mapFragment")
            dibshomeArr = event.dibsArr
        }
    }
}
