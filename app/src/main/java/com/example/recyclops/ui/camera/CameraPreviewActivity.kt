package com.example.recyclops.ui.camera

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recyclops.databinding.ActivityCameraPreviewBinding
import com.example.recyclops.ui.utils.rotateFile
import java.io.File

class CameraPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPicture()
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