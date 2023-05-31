package com.example.recyclops.ui.camera

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

class CameraPreviewAdapter(
    private val viewModel: CameraPreviewViewModel
    ) :
    ListAdapter<TrashScanned, CameraPreviewAdapter.ViewHolder>(TrashScannedDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trash_scanned, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trashScanned = getItem(position)
        holder.bind(trashScanned, viewModel)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantity)
        private val pointTextView: TextView = itemView.findViewById(R.id.tv_poin)
        private val imageSampahImageView: ImageView = itemView.findViewById(R.id.iv_imageSampah)
        private val iconDelete : ImageView = itemView.findViewById(R.id.iv_delete_item)
        private val btnPlus : Button = itemView.findViewById(R.id.btn_plus)
        private val btnMinus : Button = itemView.findViewById(R.id.btn_minus)

        fun bind(trashScanned: TrashScanned, viewModel: CameraPreviewViewModel) {
            nameTextView.text = trashScanned.name
            imageSampahImageView.setImageResource(trashScanned.imageSampah)
            viewModel.scannedTrash.observeForever {
                quantityTextView.text = trashScanned.quantity.toString()
                val point = (trashScanned.quantity * trashScanned.point).toString() + " poin"
                pointTextView.text = point
            }

            iconDelete.setOnClickListener {
                viewModel.deleteItem(trashScanned.id)
            }

            btnPlus.setOnClickListener {
                viewModel.increaseQuantity(trashScanned.id)
            }

            btnMinus.setOnClickListener {
                viewModel.decreaseQuantity(trashScanned.id)

            }
        }
    }
}
