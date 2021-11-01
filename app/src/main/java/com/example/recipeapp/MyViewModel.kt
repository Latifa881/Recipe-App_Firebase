package com.example.recipeapp


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val recipesDB by lazy { RecipesDatabase.getInstance(application).recipesDao()  }
    private var recipes: LiveData<List<Recipes>> = recipesDB.getAllRecipes()


    fun getAllRecipes(): LiveData<List<Recipes>> {
        return recipes
    }


    fun addRecipe(recipesObj: Recipes) {
        CoroutineScope(Dispatchers.IO).launch {
            recipesDB.insertRecipe(recipesObj)
        }

    }

    fun updateRecipe(recipesObj: Recipes) {
        CoroutineScope(Dispatchers.IO).launch {
            recipesDB.updateRecipe(recipesObj)
        }

    }

    fun deleteRecipe(recipesObj: Recipes) {
        CoroutineScope(Dispatchers.IO).launch {
            recipesDB.deleteRecipe(recipesObj)
        }

    }
}