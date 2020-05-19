package com.example.fridgr

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment

/**
 * Helper functions to make hiding the soft keyboard simple across the app
 */
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Helper function to get the center circular bitmap from any bitmap for the profile-picture
 *  image view.
 */
fun Bitmap.getCircularDrawable(resources: Resources): Drawable {

    val squareDrawable = when {
        this.width == this.height -> {
            this
        }
        this.width >= this.height -> {
            Bitmap.createBitmap(
                this,
                this.width / 2 - this.height / 2,
                0,
                this.height,
                this.height
            )
        }
        else -> {
            Bitmap.createBitmap(
                this,
                0,
                this.height / 2 - this.width / 2,
                this.width,
                this.width

            )
        }
    }

    return RoundedBitmapDrawableFactory.create(resources, squareDrawable).apply {
        isCircular = true
    }
}