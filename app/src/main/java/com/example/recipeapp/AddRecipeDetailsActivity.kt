package com.example.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRecipeDetailsActivity : AppCompatActivity() {
    lateinit var etTitle: EditText
    lateinit var etAuthor: EditText
    lateinit var etIngredients: EditText
    lateinit var etInstructions: EditText
    lateinit var btSave: ImageView
    lateinit var btView: ImageView
    var title = ""
    var author = ""
    var ingredients = ""
    var instructions=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe_details)
        etTitle = findViewById(R.id.etTitle)
        etAuthor = findViewById(R.id.etAuthor)
        etIngredients = findViewById(R.id.etIngredients)
        etInstructions = findViewById(R.id.etInstructions)
        btSave = findViewById(R.id.btSave)
        btView = findViewById(R.id.btView)
        btSave.setOnClickListener {
            title = etTitle.text.toString()
            author = etAuthor.text.toString()
            ingredients = etIngredients.text.toString()
            instructions = etInstructions.text.toString()
            if (title.isNotEmpty() && author.isNotEmpty() && ingredients.isNotEmpty() && instructions.isNotEmpty()) {
                addDetails()
                Toast.makeText(applicationContext, "Save success", Toast.LENGTH_SHORT).show()
                etTitle.setText("")
                etAuthor.setText("")
                etIngredients.setText("")
                etInstructions.setText("")
            } else {
                Toast.makeText(this, "Enter a name and location", Toast.LENGTH_SHORT).show()
            }
        }
        btView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    fun addDetails() {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.addDetails(Recipes.RecipeDetails(title, author, ingredients, instructions))
                .enqueue(object : Callback<List<Recipes.RecipeDetails>> {
                    override fun onResponse(
                        call: Call<List<Recipes.RecipeDetails>>,
                        response: Response<List<Recipes.RecipeDetails>>
                    ) {

                    }

                    override fun onFailure(call: Call<List<Recipes.RecipeDetails>>, t: Throwable) {
                        call.cancel()
                    }
                })

        }
    }
}