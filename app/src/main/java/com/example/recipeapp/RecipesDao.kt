package com.example.recipeapp

import androidx.room.*

//Data Access Object
@Dao
interface RecipesDao {

    @Query("SELECT * FROM Recipes ")
    fun getAllRecipes(): List<Recipes>
    @Insert
    fun insertRecipe(recipe: Recipes)
    @Update
    fun updateRecipe(recipe: Recipes)
    @Delete
    fun deleteRecipe(recipe: Recipes)

}