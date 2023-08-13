package com.example.frontend.member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.frontend.R
import com.example.frontend.databinding.ActivityDeleteBinding

class DeleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}