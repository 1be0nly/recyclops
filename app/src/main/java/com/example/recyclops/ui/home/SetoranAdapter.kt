package com.example.recyclops.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclops.data.SetoranTerakhir
import com.example.recyclops.databinding.ItemSetoranHomeBinding

class SetoranAdapter (private val listSetoran: ArrayList<SetoranTerakhir>) : RecyclerView.Adapter<SetoranAdapter.UserViewHolder>() {
    inner class UserViewHolder(private val binding : ItemSetoranHomeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind (setoran :SetoranTerakhir) {
            binding.apply {
                val berat = setoran.quantity
                val quantity = "$berat KG"
                imgItemSetoran.setImageResource(setoran.imageSampah)
                tvItemNama.text = setoran.name
                tvItemQuantity.text = quantity
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemSetoranHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserViewHolder (view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listSetoran[position])
    }

    override fun getItemCount(): Int = listSetoran.size
}