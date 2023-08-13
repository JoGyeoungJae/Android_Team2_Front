package com.example.frontend.member

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.frontend.R
import com.example.frontend.databinding.ActivityModifyBinding

class ModifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}