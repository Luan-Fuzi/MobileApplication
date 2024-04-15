package com.example.experiment1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.experiment1.databinding.ActivityDialogBinding
import com.example.experiment1.databinding.ActivityMainBinding

class DialogActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.closeButton.setOnClickListener {
            finish()
        }
    }
}