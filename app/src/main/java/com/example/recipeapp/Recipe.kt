package com.example.recipeapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipes")
data class Recipes (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id : Int = 0, // this is how can include id if needed
    @ColumnInfo(name = "Title") val title: String,
    @ColumnInfo(name = "Author") val author: String,
    @ColumnInfo(name = "Ingredients") val ingredients: String,
    @ColumnInfo(name = "Instructions") val instructions: String
)