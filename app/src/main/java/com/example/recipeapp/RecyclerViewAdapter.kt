package com.example.recipeapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.edit_delete_recipe_details.view.*
import kotlinx.android.synthetic.main.item_row.view.*

class RecyclerViewAdapter(var detailsInfo: List<Recipes>,val activity:MainActivity ) :
    RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

  private val myViewModel by lazy { ViewModelProvider(activity).get(MyViewModel::class.java) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = detailsInfo[position]

        holder.itemView.apply {
            tvTitle.text = data.title
            tvAuthor.text = data.author
            tvIngredients.text = data.ingredients
            tvInstructions.text = data.instructions

            ivSettings.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                val dialogView =
                    LayoutInflater.from(context).inflate(R.layout.edit_delete_recipe_details, null)
                builder.setView(dialogView)
                val alertDialog: AlertDialog = builder.create()
                dialogView.etEditTitle.setText(data.title)
                dialogView.etEditAuthor.setText(data.author)
                dialogView.etEditIngredients.setText(data.ingredients)
                dialogView.etEditInstructions.setText(data.instructions)

                dialogView.btEdit.setOnClickListener {
                    val title = dialogView.etEditTitle.text.toString()
                    val author = dialogView.etEditAuthor.text.toString()
                    val ingredients = dialogView.etEditIngredients.text.toString()
                    val instructions = dialogView.etEditInstructions.text.toString()
                    if(title.isNotEmpty()&&author.isNotEmpty()&&ingredients.isNotEmpty()&&instructions.isNotEmpty()){
                        myViewModel.updateRecipe(Recipes(data.id,title,author, ingredients, instructions))
                        Toast.makeText(context,"Updated Successfully",Toast.LENGTH_SHORT).show()

                        alertDialog.dismiss()
                    }else
                    {
                        Toast.makeText(context,"Please enter all the recipe information",Toast.LENGTH_SHORT).show()
                    }

                }
                dialogView.btDelete.setOnClickListener {
                    val builder = androidx.appcompat.app.AlertDialog.Builder(context)
                    //set title for alert dialog
                    builder.setTitle("Delete Recipe")
                    //set message for alert dialog
                    builder.setMessage("Are you sure you want to delete this recipe?\n${data.id}")
                    builder.setIcon(android.R.drawable.ic_dialog_info)

                    //performing positive action
                    builder.setPositiveButton("Delete") { dialogInterface, which ->
                        myViewModel.deleteRecipe(Recipes(data.id,data.title,data.author,data.ingredients,data.instructions))
                        alertDialog.dismiss()
                        dialogInterface.dismiss()

                    }
                    builder.setNegativeButton("Cancel") { dialogInterface, which ->
                        dialogInterface.dismiss()
                    }
                    // Create the AlertDialog
                    val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
                alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.setCancelable(true)
                alertDialog.show()
            }
        }


    }

    override fun getItemCount() = detailsInfo.size
    fun update(recipes: List<Recipes>){
        println("UPDATING DATA")
        this.detailsInfo = recipes
        notifyDataSetChanged()
    }
}
