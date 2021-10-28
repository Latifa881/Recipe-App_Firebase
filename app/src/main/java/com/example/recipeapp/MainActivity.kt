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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var ivAddNewRecipe: ImageView
    lateinit var rvMain: RecyclerView
    val recipesInfo = arrayListOf<Recipes>()
    val recipesDB by lazy {RecipesDatabase.getInstance(applicationContext).recipesDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivAddNewRecipe = findViewById(R.id.ivAddNewRecipe)
        rvMain = findViewById(R.id.rvMain)
        rvMain.adapter = RecyclerViewAdapter(recipesInfo,this)
        rvMain.layoutManager = LinearLayoutManager(applicationContext)

        ivAddNewRecipe.setOnClickListener {
            val intent = Intent(this, AddRecipeDetailsActivity::class.java)
            startActivity(intent)
        }

        getRecipesDetails()
    }
    fun getRecipesDetails() {
        recipesInfo.clear()
        recipesInfo.addAll(recipesDB.getAllRecipes())
        rvMain.adapter!!.notifyDataSetChanged()
        }

}