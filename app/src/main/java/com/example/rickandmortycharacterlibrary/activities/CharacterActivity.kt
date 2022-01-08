package com.example.rickandmortycharacterlibrary.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.rickandmortycharacterlibrary.R
import com.example.rickandmortycharacterlibrary.models.CharacterViewModel
import com.example.rickandmortycharacterlibrary.models.Characters
import com.example.rickandmortycharacterlibrary.RecyclerAdapter
import com.example.rickandmortycharacterlibrary.databinding.ActivityCharacterBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*

@DelicateCoroutinesApi
class CharacterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterBinding
    private lateinit var characterViewModel: CharacterViewModel
    private val characters = arrayListOf<Characters>()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerAdapter: RecyclerAdapter


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    private fun setup(){
        init()
        setupUI()
        makeNetworkRequestForData()
    }

    private fun setUpSearch(characters: ArrayList<Characters>){
        val characterNames = arrayListOf<String>()
        for(i in characters){
            characterNames.add(i.name)
        }
        println(characters)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            characterNames
        )

        binding.search.setDropDownBackgroundResource(R.drawable.background)
        binding.search.setAdapter(adapter)
        binding.search.threshold = 1

        binding.search.doOnTextChanged { text, _, _, _ ->
            recyclerAdapter.search(text.toString())
        }
    }

    private fun setupUI(){
        binding = ActivityCharacterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.name.text = "${sharedPreferences.getString("name", "Rick")} is searching..."

        binding.progressView.visibility = View.VISIBLE
    }

    private fun init(){
        layoutManager = LinearLayoutManager(this)
        sharedPreferences = getSharedPreferences("name", Context.MODE_PRIVATE)
        characterViewModel = ViewModelProvider(this)[CharacterViewModel::class.java]
    }

    private fun makeNetworkRequestForData(){
        val queue = Volley.newRequestQueue(this)
        val url = "https://rickandmortyapi.com/api/character"
        if (checkConnectivity()) {
            println("Database not used")

            GlobalScope.launch(Dispatchers.IO) {
                characterViewModel.deleteDatabase()
            }


            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                {
                    val main = it.getJSONArray("results")
                    for (i in 0 until main.length()) {
                        val data = main.getJSONObject(i)
                        val id = data.getInt("id")
                        val name = data.getString("name")
                        val species = data.getString("species")
                        val status = data.getString("status")
                        val image = data.getString("image")
                        val obj = data.getJSONObject("location")
                        val location = obj.getString("name")

                        val character = Characters(id, name, image, species, status, location)

                        characters.add(character)
                    }

                    GlobalScope.launch(Dispatchers.IO) {
                        characterViewModel.enterAllCharacters(characters)
                    }

                    recyclerAdapter = RecyclerAdapter(this, characters)
                    binding.recycler.layoutManager = layoutManager
                    binding.recycler.adapter = recyclerAdapter
                    setUpSearch(characters)
                    binding.progressView.visibility = View.GONE
                },
                {
                    println(it.message)
                }
            )
            queue.add(jsonObjectRequest)
        } else {

            println("Database used")

            GlobalScope.launch(Dispatchers.IO) {
                val char = characterViewModel.getAllCharacters()
                char.observe(this@CharacterActivity, {
                    characters.addAll(it)
                })
            }

            recyclerAdapter = RecyclerAdapter(this, characters)
            binding.recycler.layoutManager = layoutManager
            binding.recycler.adapter = recyclerAdapter

            setUpSearch(characters)

            binding.progressView.visibility = View.GONE
        }
    }

    private fun checkConnectivity(): Boolean {
        val conMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo
        return netInfo != null
    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Exit!")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.create()
            .show()
    }
}