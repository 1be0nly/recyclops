package com.example.recyclops.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recyclops.databinding.ActivityLoginBinding
import com.example.recyclops.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}