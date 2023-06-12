package com.example.recyclops.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclops.data.UserHistoryItem
import com.example.recyclops.databinding.ItemHistoryBinding

class HistoryAdapter(private val listSetoran: ArrayList<UserHistoryItem>) : RecyclerView.Adapter<HistoryAdapter.UserViewHolder>() {
    inner class UserViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(setoran: UserHistoryItem) {
            binding.apply {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listSetoran[position])
    }

    override fun getItemCount(): Int = listSetoran.size
}