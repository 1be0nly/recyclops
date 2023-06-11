package com.example.recyclops.ui.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.recyclops.R
import com.example.recyclops.api.FileUploadResponse
import com.example.recyclops.api.ResponseInterface
import com.example.recyclops.data.TrashScanned
import com.example.recyclops.databinding.ActivityCameraPreviewBinding
import com.example.recyclops.repository.TokenPreferences
import com.example.recyclops.ui.login.dataStore
import com.google.firebase.auth.FirebaseAuth

class CameraPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPreviewSend.setOnClickListener{}

        val pref = TokenPreferences.getInstance(dataStore)
        val viewModel = ViewModelProvider(this, ImageConfirmViewModelFactory(pref))[CameraPreviewViewModel::class.java]

        val imageUrl = intent.getStringExtra("imageUrl")
        val wasteType = intent.getStringExtra("wasteType")
        val confidence = intent.getStringExtra("confidence")
        val uniqueId = intent.getStringExtra("uniqueId")

        binding.btnPreviewSend.setOnClickListener{
            if (binding.tfPreviewWeight.text!!.isNotEmpty()){
                val wasteType = binding.tvPreviewType.text.toString()
                val weight = binding.tfPreviewWeight.text.toString().toInt()
                val mUser = FirebaseAuth.getInstance().currentUser
                mUser!!.getIdToken(true)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val idToken: String? = task.result.token
                            viewModel.uploadImageConfirmation("Bearer $idToken",wasteType,weight,
                                uniqueId!!,this)
                            Log.d("upload", "$idToken,$wasteType,$weight,$imageUrl")
                            Log.d("token", idToken.toString())
                        } else {
                            Log.d("Exception", task.exception.toString())
                        }
                    }
            }else{
                binding.tfPreviewWeight.error = "Silahkan Masukkan Berat Sampah Terlebih Dahulu"
                binding.tfPreviewWeight.requestFocus()
            }
        }

        binding.apply {
            Glide.with(this@CameraPreviewActivity)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(ivPreviewTrash)
            tvPreviewType.text = wasteType
        }
    }

    //override fun fileUploadResponseAdded(fileUploadResponse: FileUploadResponse) {
       // binding.apply {
            //Glide.with(this@CameraPreviewActivity)
                //.load(fileUploadResponse.imageUrl)
                //.transition(DrawableTransitionOptions.withCrossFade())
                //.centerCrop()
                //.into(ivPreviewTrash)
           // tvPreviewType.text = fileUploadResponse.wasteType
       // }
}