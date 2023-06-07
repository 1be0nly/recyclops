package com.example.recyclops.ui.camera

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.recyclops.R
import com.example.recyclops.data.TrashScanned
import com.example.recyclops.databinding.ActivityCameraPreviewBinding
import com.example.recyclops.ui.utils.rotateFile
import java.io.File

class CameraPreviewActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCameraPreviewBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this).get(CameraPreviewViewModel::class.java)
        viewModel.addListTrashScanned(generateFakeData())

//        TODO: ada bug ketika increase dan decrease jumlah trash, ui loncat dan tidak stabil
        showRecylerView(viewModel)

        val imageUrl = intent.getStringExtra("imageUrl")
        Log.d("imageURL", imageUrl.toString())
        val wasteType = intent.getStringExtra("wasteType")
        val confidence = intent.getStringExtra("confidence")

        binding.apply {
            Glide.with(this@CameraPreviewActivity)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(ivPreviewImage)
            tvType.text = wasteType
            tvConfidence.text = confidence
        }
    }
    fun generateFakeData(): List<TrashScanned> {
        return listOf(
            TrashScanned(1,"Trash 1", 5,11, R.drawable.img_botol_kaca),
            TrashScanned(2,"Trash 2", 10,9, R.drawable.img_kardus),
            TrashScanned(3,"Trash 3", 7,6,R.drawable.img_styrofoam),
            TrashScanned(4,"Trash 4", 3,15,R.drawable.img_botol_kaca),
            TrashScanned(5,"Trash 5", 2,10,R.drawable.img_styrofoam),
            TrashScanned(6,"Trash 6", 1,13,R.drawable.img_kardus),
            TrashScanned(7,"Trash 7", 4,5,R.drawable.img_styrofoam),
        )
    }
    fun showRecylerView(viewModel: CameraPreviewViewModel) {
        recyclerView = findViewById(R.id.rv_trash_preview)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val adapter = CameraPreviewAdapter(viewModel)

        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        recyclerView.adapter = adapter

        viewModel.scannedTrash.observe(this) { newList ->
            val scrollPosition = layoutManager.findFirstVisibleItemPosition()

            adapter.submitList(newList)
            recyclerView.scrollToPosition(scrollPosition)
        }
    }
}