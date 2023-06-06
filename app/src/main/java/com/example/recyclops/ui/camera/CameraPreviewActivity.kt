package com.example.recyclops.ui.camera

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclops.R
import com.example.recyclops.data.TrashScanned
import com.example.recyclops.databinding.ActivityCameraPreviewBinding
import com.example.recyclops.ui.utils.rotateFile
import java.io.File

class CameraPreviewActivity : AppCompatActivity() {

    private lateinit var viewModel: CameraPreviewViewModel

    private lateinit var binding: ActivityCameraPreviewBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(CameraPreviewViewModel::class.java)
        viewModel.addListTrashScanned(generateFakeData())

//        TODO: ada bug ketika increase dan decrease jumlah trash, ui loncat dan tidak stabil
        showRecylerView(viewModel)

        getPicture()
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

    private fun getPicture() {
        val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("picture", File::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("picture")
        } as? File
        val isBackCamera = intent.getBooleanExtra("isBackCamera", true) as Boolean

        myFile?.let { file ->
            rotateFile(file,isBackCamera)
            binding.ivPreviewImage.setImageURI(Uri.fromFile(file))
        }
    }
}