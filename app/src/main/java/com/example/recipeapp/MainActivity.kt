package com.example.recipeapp

import android.Manifest
import com.itextpdf.text.Document
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var ivAddNewRecipe: ImageView
    lateinit var generatePDF: ImageView
    lateinit var rvMain: RecyclerView
    private val STORAGECODE: Int = 100
    private val myViewModel by lazy { ViewModelProvider(this).get(MyViewModel::class.java) }
    lateinit var myAdapter: RecyclerViewAdapter
    var recipesArray = ArrayList<Recipes>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivAddNewRecipe = findViewById(R.id.ivAddNewRecipe)
        generatePDF = findViewById(R.id.generatePDF)
        rvMain = findViewById(R.id.rvMain)

        myViewModel.getRecipesData()
        myViewModel.getAllRecipes().observe(this, { recipes ->
            myAdapter.update(recipes)
        }
        )
        myAdapter = RecyclerViewAdapter(recipesArray, this)
        rvMain.adapter = myAdapter
        rvMain.layoutManager = LinearLayoutManager(applicationContext)

        ivAddNewRecipe.setOnClickListener {
            val intent = Intent(this, AddRecipeDetailsActivity::class.java)
            startActivity(intent)
        }
        generatePDF.setOnClickListener {
            //we need to handle runtime permission for devices with marshmallow and above
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                //system OS >= Marshmallow(6.0), check permission is enabled or not
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    //permission was not granted, request it
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, STORAGECODE)
                    Log.d("permissions", "permissions")
                } else {
                    Log.d("permissions", "permission already granted")
                    //permission already granted, call savePdf() method
                    savePdf()
                }
            } else {

                //system OS < marshmallow, call savePdf() method
                savePdf()
            }
        }
    }

    fun savePdf() {
        //create object of Document class
        val mDoc = Document()
        //pdf file name
        val mFileName = "Recipes " + SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())
        //pdf file path
        val mFilePath =
            File(this.externalCacheDir!!.absolutePath.toString(), "/" + mFileName + ".pdf")

        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))

            //open the document for writing
            mDoc.open()

            val recipes = myViewModel.getRecipesData()
            for (i in recipes) {
                val recipe = "Title: ${i.title}\n" +
                        "Author: ${i.author}\n" +
                        "Ingredients: ${i.ingredients}\n" +
                        "Instructions: ${i.instructions}\n\n"
                //add paragraph to the document
                mDoc.add(Paragraph(recipe))
            }
            //add author of the document (metadata)
            mDoc.addAuthor("Recipes App")


            //close document
            mDoc.close()

            //show file saved message with file name and path
            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT)
                .show()
            Log.d("PDF LOCATION", "$mFilePath")

        } catch (e: Exception) {
            //if anything goes wrong causing exception, get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGECODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted, call savePdf() method
                    savePdf()
                    Toast.makeText(this, "PDF Generated", Toast.LENGTH_SHORT).show()
                } else {
                    //permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


