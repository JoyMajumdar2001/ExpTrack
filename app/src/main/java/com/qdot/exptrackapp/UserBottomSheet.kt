package com.qdot.exptrackapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.qdot.exptrackapp.databinding.UserSheetBinding
import fuel.Fuel
import fuel.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class UserBottomSheet(private val uid : String,private val dp:String,private val email: String): BottomSheetDialogFragment() {
    private var _binding: UserSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dpImg.load(dp)
        binding.email.text = email
        CoroutineScope(Dispatchers.IO).launch {
           val resBody =  Fuel.get("https://ancient-beige.cmd.outerbase.io/limits?userid=${uid}").body
            val mLimit = JSONObject(resBody).getJSONObject("response")
                .getJSONArray("items").getJSONObject(0).getString("mlim")
            withContext(Dispatchers.Main){
                binding.limitText.setText(mLimit)
            }
        }
        binding.updateBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                Fuel.get("https://ancient-beige.cmd.outerbase.io/updatelimit?userid=${uid}&mlim=${binding.limitText.text.toString().toFloat()}")
            }
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}