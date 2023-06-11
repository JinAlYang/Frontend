package com.example.dongsan2mong.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dongsan2mong.adapter.HouseInfoDataAdapter
import com.example.dongsan2mong.data.HouseInfoData
import com.example.dongsan2mong.data.RealEstateData
import com.example.dongsan2mong.databinding.ActivityClusterBinding
import com.example.dongsan2mong.databinding.RowHouseinfoBinding
import com.example.dongsan2mong.event.DataEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ClusterActivity : AppCompatActivity() {

    lateinit var binding: ActivityClusterBinding
    lateinit var adapter: HouseInfoDataAdapter

    var data: ArrayList<HouseInfoData>? = ArrayList()
    val selected: ArrayList<Boolean> = ArrayList()
    var dibshomeArr: ArrayList<HouseInfoData>? = ArrayList()

    var clusterArr: ArrayList<RealEstateData>? = ArrayList<RealEstateData>()
    var houseInfoArr: ArrayList<HouseInfoData>? = ArrayList<HouseInfoData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClusterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clusterArr =
            intent.getParcelableArrayListExtra<RealEstateData>("clusterArr")
        dibshomeArr =
            intent.getParcelableArrayListExtra<HouseInfoData>("dibshomeArr")
        /*
        clusterIntent.putExtra("clusterArr", seperatedEstate[i * 4 + j])
         */
        moveDataToHouseInfoData()
        initRecyclerView()
        init()
    }

    fun moveDataToHouseInfoData() {
        clusterArr?.forEach { realEstateData ->
            val houseInfo = HouseInfoData(
                type = "월세",
                price = realEstateData.deposit.toString().removeSuffix("0000") + "/" +
                        realEstateData.monthlyPayment.toString().removeSuffix("0000"),
                space = realEstateData.roomSize.toString() + "m^2",
                area = realEstateData.areaNumberAddress,
                roomNum = realEstateData.spaceType,
                imgURL = realEstateData.imgUrl
            )
            houseInfoArr?.add(houseInfo)
        }
    }

    fun init() {

    }

    fun initRecyclerView() {
        binding.recyclerViewCluster.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        adapter = HouseInfoDataAdapter(houseInfoArr!!, selected)

        // dibshomeArr = intent.extras?.get("k") as ArrayList<HouseInfoData>

        val parcelableArray2 = intent.getParcelableArrayExtra("dibshomeArr")
        dibshomeArr = if (parcelableArray2 is Array<*>) {
            parcelableArray2.filterIsInstance<HouseInfoData>() as ArrayList<HouseInfoData>
        } else {
            ArrayList<HouseInfoData>()
        }

        adapter.changeDibshomeArr(dibshomeArr!!)
        adapter.itemClickListener = object : HouseInfoDataAdapter.OnItemClickListener {
            override fun OnItemClick(
                data: HouseInfoData,
                binding: RowHouseinfoBinding,
                position: Int
            ) {

            }
        }
        binding.clusterBackBtn.setOnClickListener {
            val returnIntent = Intent()
            // 이전 버튼 눌러서 돌아가면 이전 activity에 dibshomearr 전송
            returnIntent.putExtra("returnDibshomeArr", adapter.findDibshomeArr())
            setResult(RESULT_OK, returnIntent)
            finish()
        }
        binding.recyclerViewCluster.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        try {
            EventBus.getDefault().register(this)
        } catch (e: Exception) {
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        dibshomeArr = adapter.dibshomeArr
        EventBus.getDefault().post(DataEvent(1, dibshomeArr!!))

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printData(event: DataEvent) {
        if (event.int == 0) {
            Log.d("dataEvent", "mapFragment to ClusterActivity")
            dibshomeArr = event.dibsArr
            adapter.changeDibshomeArr(dibshomeArr!!)
            adapter.notifyDataSetChanged()

        }
    }
}