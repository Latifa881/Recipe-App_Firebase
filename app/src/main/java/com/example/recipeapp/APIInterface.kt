package com.example.recipeapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterface {
    @Headers("Content-Type: application/json")
    @GET("/recipes/")
    fun getDetails(): Call<List<Recipes.RecipeDetails>>

    @Headers("Content-Type: application/json")
    @POST("/recipes/")
    fun addDetails(@Body details: Recipes.RecipeDetails): Call<List<Recipes.RecipeDetails>>

}