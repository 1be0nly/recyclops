package com.example.recyclops.ui.camera

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.recyclops.databinding.ActivityImageConfirmationBinding
import com.example.recyclops.ui.utils.reduceFileImage
import com.example.recyclops.ui.utils.rotateFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ImageConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageConfirmationBinding
    private lateinit var viewModel: CameraPreviewViewModel
    private var myFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(CameraPreviewViewModel::class.java)

        getPicture()

        binding.btnCancel.setOnClickListener(){ cancel() }
        binding.btnConfirm.setOnClickListener(){
            if (myFile != null){
                val file = reduceFileImage(myFile as File)
                val intent = Intent(this, CameraPreviewActivity::class.java)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestImageFile
                )
                viewModel.uploadImage( imageMultipart)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Silahkan Foto Sampah Terlebih Dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPicture() {
         myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("picture", File::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("picture")
        } as? File
        val isBackCamera = intent.getBooleanExtra("isBackCamera", true) as Boolean

        myFile?.let { file ->
            rotateFile(file,isBackCamera)
            binding.ivTrashPhoto.setImageURI(Uri.fromFile(file))
        }
    }

    private fun cancel(){
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
        finish()
    }
}