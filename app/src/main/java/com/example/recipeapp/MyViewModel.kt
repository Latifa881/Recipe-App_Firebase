package com.example.recipeapp


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(application: Application) : AndroidViewModel(application) {
    val db = Firebase.firestore
    private var recipes: MutableLiveData<List<Recipes>> = MutableLiveData()
    private val recipesArray: ArrayList<Recipes> = ArrayList()

    fun getAllRecipes(): LiveData<List<Recipes>> {
        return recipes
    }
    fun getRecipesData():ArrayList<Recipes>{
        recipesArray.clear()
        db.collection("recipes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var title = ""
                    var author = ""
                    var ingredients = ""
                    var instructions = ""
                    Log.d("TAG123", "${document.id} => ${document.data}")
                    document.data.map { (key, value)
                        ->
                        when (key) {
                            "Title" -> title = value as String
                            "Author" -> author = value as String
                            "Ingredients" -> ingredients = value as String
                            "Instructions" -> instructions = value as String
                        }
                    }
                    recipesArray.add(Recipes( document.id,title, author,ingredients,instructions))
                }
                recipes.postValue(recipesArray)
            }
            .addOnFailureListener { exception ->
                Log.w("TAG123", "Error getting documents.", exception)
            }
        return recipesArray
    }

    fun addRecipe(recipesObj: Recipes) {
        CoroutineScope(Dispatchers.IO).launch {
            val myNewNote = hashMapOf(
                "Title" to recipesObj.title,
                "Author" to recipesObj.author,
                "Ingredients" to recipesObj.ingredients,
                "Instructions" to recipesObj.instructions,
            )

            db.collection("recipes")
                .add(myNewNote)
                .addOnSuccessListener { documentReference ->
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                    getRecipesData()
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding document", e)
                }
        }

    }

    fun updateRecipe(recipesObj: Recipes) {
        CoroutineScope(Dispatchers.IO).launch {
            CoroutineScope(Dispatchers.IO).launch {
                val myUpdatedNote = mapOf(
                    "Title" to recipesObj.title,
                    "Author" to recipesObj.author,
                    "Ingredients" to recipesObj.ingredients,
                    "Instructions" to recipesObj.instructions,
                )

                db.collection("recipes").document(recipesObj.id).update(myUpdatedNote)
                    .addOnSuccessListener { documentReference ->
                        getRecipesData()
                    }
                    .addOnFailureListener { e ->
                        Log.w("TAG", "Error updating document", e)
                    }
            }
        }

    }

    fun deleteRecipe(recipesObj: Recipes) {
        CoroutineScope(Dispatchers.IO).launch {

            db.collection("recipes").document(recipesObj.id).delete()
                .addOnSuccessListener { documentReference ->
                    getRecipesData()
                    Log.d("Delete TAG", " Successfully deleting document")
                }
                .addOnFailureListener { e ->
                    Log.w("Delete TAG", "Error deleting document", e)
                }

        }

    }
}