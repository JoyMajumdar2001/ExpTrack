package com.qdot.exptrackapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.qdot.exptrackapp.databinding.ExpLayoutBinding
import com.qdot.exptrackapp.models.ExpenseModel

class ExpAdapter(private var expList : List<ExpenseModel>) : RecyclerView.Adapter<ExpAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ExpLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ExpLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(expList[position]){
                binding.amount.text = "-"+this.amount.toString()
                binding.noteAbt.text = this.note
                binding.typeCat.text = this.type
                when(this.type){
                    "Food"->{
                        binding.typeImg.load(R.drawable.food)
                    }
                    "Shopping"->{
                        binding.typeImg.load(R.drawable.shopping)
                    }
                    "Travel"->{
                        binding.typeImg.load(R.drawable.travel)
                    }

                    else -> {
                        binding.typeImg.load(R.drawable.other)
                    }
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return expList.size
    }

}