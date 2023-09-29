package com.qdot.exptrackapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.qdot.exptrackapp.databinding.InputSheetLayoutBinding
import com.qdot.exptrackapp.models.ExpenseModel
import fuel.Fuel
import fuel.post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class InputBottomSheet(private val uid : String): BottomSheetDialogFragment() {
    private var _binding: InputSheetLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var date : LocalDate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InputSheetLayoutBinding.inflate(inflater, container, false)
        date  = LocalDate.now()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = listOf("Food","Shopping","Travel","Others")
        binding.dropText.setSimpleItems(items.toTypedArray())
        binding.submitBtn.setOnClickListener {
            val dataModel = ExpenseModel(
                0,
                binding.amountText.text.toString().toFloat(),
                binding.dropText.text.toString(),
                binding.noteText.text.toString(),
                "Date",
                getDay(),
                getWeek(),
                getMonth(),
                getYear(),
                uid
            )
            CoroutineScope(Dispatchers.IO).launch {
                val bodyStr = Gson().toJson(dataModel)
                Fuel.post(url = "https://ancient-beige.cmd.outerbase.io/adddexp",
                    headers = mapOf("Content-Type" to "application/json"),
                    body = bodyStr
                )
            }
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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