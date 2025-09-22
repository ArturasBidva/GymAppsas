package com.example.gymappsas.util

import android.content.Context
import android.graphics.BitmapFactory
import coil3.Bitmap
import java.io.IOException

object GetImagePath {
    fun getExerciseImagePath(category: String, exerciseName: String, imageIndex: Int = 0): String {
        // Base path for assets
        val basePath = "file:///android_asset/exercisesbycategories"

        // Convert category and exercise name to match folder naming conventions
        val normalizedCategory = category.lowercase()
        val normalizedExerciseName = exerciseName.lowercase().replace(" ", "_")

        // Construct the final path based on conventions
        return "$basePath/$normalizedCategory/$normalizedExerciseName/$imageIndex.jpg"
    }
}
object GetExerciseBitmap {
    fun getBitmapFromAssets(context: Context, category: String, exerciseName: String, imageIndex: Int = 0): Bitmap? {
        val assetPath = "exercisesbycategories/${category.lowercase()}/${exerciseName.lowercase().replace(" ", "_")}/$imageIndex.jpg"

        return try {
            context.assets.open(assetPath).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}