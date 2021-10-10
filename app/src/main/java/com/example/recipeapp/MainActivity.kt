package com.example.recipeapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var ivAddNewRecipe: ImageView
    lateinit var rvMain: RecyclerView
    val detailsInfo = arrayListOf<Recipes.RecipeDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivAddNewRecipe = findViewById(R.id.ivAddNewRecipe)
        rvMain = findViewById(R.id.rvMain)
        //RecyclerView
        rvMain.adapter = RecyclerViewAdapter(detailsInfo)
        rvMain.layoutManager = LinearLayoutManager(applicationContext)
        ivAddNewRecipe.setOnClickListener {
            val intent = Intent(this, AddRecipeDetailsActivity::class.java)
            startActivity(intent)
        }

        getDetails()
    }
    fun getDetails() {
        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getDetails()?.enqueue(object : Callback<List<Recipes.RecipeDetails>> {
                override fun onResponse(
                    call: Call<List<Recipes.RecipeDetails>>,
                    response: Response<List<Recipes.RecipeDetails>>
                ) {
                    progressDialog.dismiss()
                    Log.d("TAG", response.code().toString() + "")
                    for(data in response.body()!!){
                        detailsInfo.add(Recipes.RecipeDetails(data.title,data.author,data.ingredients,data.instructions))
                    }
                    rvMain.adapter!!.notifyDataSetChanged()

                }

                override fun onFailure(call: Call<List<Recipes.RecipeDetails>>, t: Throwable) {
                    progressDialog.dismiss()
                    call.cancel()
                }
            })
        }



    }
}