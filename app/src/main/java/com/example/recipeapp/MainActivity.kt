package com.example.recipeapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var ivAddNewRecipe: ImageView
    lateinit var rvMain: RecyclerView

    private val myViewModel by lazy { ViewModelProvider(this).get(MyViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivAddNewRecipe = findViewById(R.id.ivAddNewRecipe)
        rvMain = findViewById(R.id.rvMain)

        myViewModel.getAllRecipes().observe(this,{recipes->
            rvMain.adapter = RecyclerViewAdapter(recipes,this)
            rvMain.layoutManager = LinearLayoutManager(applicationContext)
        }
        )

        ivAddNewRecipe.setOnClickListener {
            val intent = Intent(this, AddRecipeDetailsActivity::class.java)
            startActivity(intent)
        }

    }

}