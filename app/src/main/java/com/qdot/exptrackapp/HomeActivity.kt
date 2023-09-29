package com.qdot.exptrackapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.gson.Gson
import com.qdot.exptrackapp.databinding.ActivityHomeBinding
import com.qdot.exptrackapp.models.ExpenseModel
import fuel.Fuel
import fuel.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var date : LocalDate
    private lateinit var uid : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        date  = LocalDate.now()
        val email = intent.getStringExtra("email")
        val dp = intent.getStringExtra("dp")
        uid = intent.getStringExtra("uid").toString()

        binding.chipGroup.check(R.id.todayChip)
        dailySelection()

        binding.chipGroup.setOnCheckedStateChangeListener { _, _ ->
            if (binding.todayChip.isChecked){
                dailySelection()
            }
            if (binding.weekChip.isChecked){
                weeklySelection()
            }
            if (binding.monthChip.isChecked){
                monthlySelection()
            }
        }

        loadTotalExp()

        binding.dpImage.load(dp)

        binding.addFab.setOnClickListener {
            val modalBottomSheet = InputBottomSheet(uid)
            modalBottomSheet.show(supportFragmentManager, "TAG")
        }
        binding.dpImage.setOnClickListener{
            val modalBottomSheet = UserBottomSheet(uid,dp.toString(),email.toString())
            modalBottomSheet.show(supportFragmentManager, "TAG")
        }
    }

    private fun loadTotalExp() {
        CoroutineScope(Dispatchers.IO).launch {
            val dtaList = mutableListOf<ExpenseModel>()
            val gson = Gson()
            val req = Fuel.get("https://ancient-beige.cmd.outerbase.io/getexps?userid=${uid}")
            val dataFetch = req.body
            val obj = JSONObject(dataFetch)
            val totalData= obj.getJSONObject("response").getJSONArray("items")
            for(i in 0..<totalData.length()){
                dtaList.add(gson.fromJson(totalData.getJSONObject(i).toString(),ExpenseModel::class.java))
            }
            val adapter = ExpAdapter(dtaList)
            val layMan = LinearLayoutManager(this@HomeActivity)
            withContext(Dispatchers.Main){
                binding.expRecyclerView.adapter = adapter
                binding.expRecyclerView.layoutManager = layMan
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun dailySelection(){
        CoroutineScope(Dispatchers.IO).launch {
            val req = Fuel.get("https://ancient-beige.cmd.outerbase.io/dtotal?userid=${uid}&day=${getDay()}&month=${getMonth()}&year=${getYear()}")
            val dataFetch = req.body
            val obj = JSONObject(dataFetch)
            val totalAmount = obj.getJSONObject("response")
                .getJSONArray("items").getJSONObject(0).getString("sum")
            withContext(Dispatchers.Main){
                if (totalAmount.isNullOrBlank()||totalAmount.equals("null")){
                    binding.totalExpenseText.text = "₹${00.00}"
                }else{
                    binding.totalExpenseText.text = "₹${totalAmount.toFloat()}"
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun weeklySelection(){
        CoroutineScope(Dispatchers.IO).launch {
            val req = Fuel.get("https://ancient-beige.cmd.outerbase.io/wtotal?userid=${uid}&week=${getWeek()}&month=${getMonth()}&year=${getYear()}")
            val dataFetch = req.body
            val obj = JSONObject(dataFetch)
            val totalAmount = obj.getJSONObject("response")
                .getJSONArray("items").getJSONObject(0).getString("sum")
            withContext(Dispatchers.Main){
                if (totalAmount.isNullOrBlank()||totalAmount.equals("null")){
                    binding.totalExpenseText.text = "₹${00.00}"
                }else{
                    binding.totalExpenseText.text = "₹${totalAmount.toFloat()}"
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun monthlySelection(){
        CoroutineScope(Dispatchers.IO).launch {
            val req = Fuel.get("https://ancient-beige.cmd.outerbase.io/mtotal?userid=${uid}&month=${getMonth()}&year=${getYear()}")
            val dataFetch = req.body
            val obj = JSONObject(dataFetch)
            val totalAmount = obj.getJSONObject("response")
                .getJSONArray("items").getJSONObject(0).getString("sum")
            withContext(Dispatchers.Main){
                if (totalAmount.isNullOrBlank()||totalAmount.equals("null")){
                    binding.totalExpenseText.text = "₹${00.00}"
                }else{
                    binding.totalExpenseText.text = "₹${totalAmount.toFloat()}"
                }
            }
        }
    }

    private fun getDay() : Int{
        return date.dayOfMonth
    }

    private fun getWeek():Int{
        val secondApiFormat = DateTimeFormatter.ofPattern("W")
        val splDate = date.format(secondApiFormat)
        return splDate.toString().toInt()
    }

    private fun getMonth():Int{
        return date.monthValue
    }

    private fun getYear():Int{
        return date.year
    }
}