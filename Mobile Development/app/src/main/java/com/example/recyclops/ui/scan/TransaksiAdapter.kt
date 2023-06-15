package com.example.recyclops.ui.scan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclops.R
import com.example.recyclops.data.TrashScanned
import com.example.recyclops.ui.utils.TrashScannedDiffCallback

class TransaksiAdapter(
    private val onIncreaseQuantity: (Int,Int) -> Unit,
    private val onDecreaseQuantity: (Int,Int) -> Unit,
    private val onDeleteItem: (Int,Int) -> Unit
) : ListAdapter<TrashScanned, TransaksiAdapter.ViewHolder>(TrashScannedDiffCallback()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantity)
        val pointTextView: TextView = itemView.findViewById(R.id.tv_poin)
        val imageSampahImageView: ImageView = itemView.findViewById(R.id.iv_imageSampah)
        val iconDelete : ImageView = itemView.findViewById(R.id.iv_delete_item)
        val increaseButton: Button = itemView.findViewById(R.id.btn_plus)
        val decreaseButton: Button = itemView.findViewById(R.id.btn_minus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trash_scanned, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trashScanned = getItem(position)
        val id = trashScanned.id

        holder.nameTextView.text = trashScanned.name
        holder.quantityTextView.text = "${trashScanned.quantity} Kg"
        holder.pointTextView.text = "${trashScanned.point * trashScanned.quantity} Poin"
        holder.imageSampahImageView.setImageResource(trashScanned.imageSampah)

        holder.increaseButton.setOnClickListener { onIncreaseQuantity(id, position) }
        holder.decreaseButton.setOnClickListener { onDecreaseQuantity(id, position) }
        holder.iconDelete.setOnClickListener { onDeleteItem(id, position) }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}