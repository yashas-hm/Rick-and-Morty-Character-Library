package com.example.rickandmortycharacterlibrary.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rickandmortycharacterlibrary.R
import com.example.rickandmortycharacterlibrary.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("name", Context.MODE_PRIVATE)

        binding.submit.setOnClickListener {
            if(binding.etName.text.isNullOrEmpty()){
                binding.etName.error = "Please Enter Name"
            }else{
                sharedPreferences.edit().putString("name", binding.etName.text.toString()).apply()
                startActivity(Intent(this@RegistrationActivity, CharacterActivity::class.java))
                finish()
            }
        }
    }
}