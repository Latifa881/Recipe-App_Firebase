package com.example.recipeapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Recipes::class],version = 1,exportSchema = false)
abstract class RecipesDatabase: RoomDatabase() {

    companion object{
        var instance:RecipesDatabase?=null;
        fun getInstance(ctx: Context):RecipesDatabase
        {
            if(instance!=null)
            {
                return  instance as RecipesDatabase;
            }
            instance= Room.databaseBuilder(ctx,RecipesDatabase::class.java,"recipes").run { allowMainThreadQueries() }.build()
            return instance as RecipesDatabase;
        }
    }
    abstract fun recipesDao():RecipesDao
}