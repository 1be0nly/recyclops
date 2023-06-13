package com.example.recyclops.ui.camera

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
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
import com.example.recyclops.databinding.LayoutYesnoDialogBinding
import com.example.recyclops.repository.TokenPreferences
import com.example.recyclops.ui.login.dataStore
import com.example.recyclops.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class CameraPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraPreviewBinding
    private lateinit var bindingDialog: LayoutYesnoDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = TokenPreferences.getInstance(dataStore)
        val viewModel = ViewModelProvider(this, ImageConfirmViewModelFactory(pref))[CameraPreviewViewModel::class.java]

        val imageUrl = intent.getStringExtra("imageUrl").toString()
        val wasteType = intent.getStringExtra("wasteType")
        val confidence = intent.getStringExtra("confidence").toString().toFloat()
        val uniqueId = intent.getStringExtra("uniqueId")

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@CameraPreviewActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        })

        binding.btnPreviewSend.setOnClickListener{
            if (binding.tfPreviewWeight.text!!.isNotEmpty()){
                val wasteType = binding.tvPreviewType.text.toString()
                val weight = binding.tfPreviewWeight.text.toString().toInt()
                val mUser = FirebaseAuth.getInstance().currentUser
                mUser!!.getIdToken(true)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val idToken: String? = task.result.token
                            val status = "Berhasil"
                            val message = "Scan Lagi ?"
                            viewModel.uploadImageConfirmation("Bearer $idToken",wasteType,weight, imageUrl, confidence)
                            showYesNoDialog(status,message)
                            Log.d("upload", "$idToken,$wasteType,$weight")
                            Log.d("token", idToken.toString())
                        } else {
                            val status = "Gagal"
                            val message = "Scan Lagi ?"
                            showYesNoDialog(status,message)
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

    private fun showYesNoDialog(status:String, message: String) {
        val dialog = Dialog(this)
        bindingDialog = LayoutYesnoDialogBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(bindingDialog.root)

        bindingDialog.apply {
            tvDialog.text = message
            tvStatus.text = status

            btnYes.setOnClickListener{
                startActivity(Intent(this@CameraPreviewActivity, CameraActivity::class.java))
            }

            btnNo.setOnClickListener{
                startActivity(Intent(this@CameraPreviewActivity, MainActivity::class.java))
            }
        }
        dialog.show()
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