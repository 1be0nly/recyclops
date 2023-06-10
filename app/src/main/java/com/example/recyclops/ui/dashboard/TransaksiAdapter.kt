package com.example.recyclops.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclops.R
import com.example.recyclops.data.TrashScanned

class TransaksiAdapter(
    private val dataList: List<TrashScanned>,
    private val onIncreaseQuantity: (Int) -> Unit,
    private val onDecreaseQuantity: (Int) -> Unit,
    private val onDeleteItem: (Int) -> Unit
) : RecyclerView.Adapter<TransaksiAdapter.ViewHolder>() {

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
        val trashScanned = dataList[position]

        holder.nameTextView.text = trashScanned.name
        holder.quantityTextView.text = "Quantity: ${trashScanned.quantity}"
        holder.pointTextView.text = "Poin: ${trashScanned.point * trashScanned.quantity}"
        holder.imageSampahImageView.setImageResource(trashScanned.imageSampah)

        holder.increaseButton.setOnClickListener { onIncreaseQuantity(position) }
        holder.decreaseButton.setOnClickListener { onDecreaseQuantity(position) }
        holder.iconDelete.setOnClickListener { onDeleteItem(position) }
    }

    override fun getItemCount(): Int = dataList.size
}