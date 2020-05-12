package com.example.fridgr.popups

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import kotlin.math.ceil

/**
 * Template abstract class to base all popup windows off of.
 * Provides function to show the popup window and ability to parse a dismissListener
 * @param context The context that the PopupWindow will be inflated from and that resources can be
 *                  pulled from
 * @param layoutResource The XML resource ID to inflate to be the PopupWindow
 * @param onDismissListener Function which will be fired on dismiss of the PopupWindow
 * @param width The width, in dp, of the PopupWindow since it cannot be inferred from the XML resource
 * @param height The height, in dp, of the PopupWindow since it cannot be inferred from the XML resource
 */
abstract class AbstractPopup(val context: Context,
                             layoutResource: Int,
                             onDismissListener: (AbstractPopup) -> Unit,
                             width: Int,
                             height: Int) {

    protected val view: View = LayoutInflater.from(context).inflate(layoutResource, null)
    protected val popupWindow = PopupWindow(view, getDp(width), getDp(height), true)
    protected lateinit var parentView: View

    init {
        //Gives the popup a visual elevation from the underlying view
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        popupWindow.elevation = 10F

        //Sets up basic functionality as to what should happen when the popup is dismissed
        popupWindow.setOnDismissListener {
            fetchFields()
            onDismissListener(this)
        }
    }

    /**
     * Helper function to convert pixels to dp
     */
    private fun getDp(pixels: Int): Int {
        return ceil(pixels.toDouble() * context.resources.displayMetrics.density).toInt()
    }

    /**
     * Shows the PopupWindow to the screen in the center of the parent view
     */
    fun showAtLocation(view: View, gravity: Int, x: Int, y: Int) {
        //Saves the parent view so that child popups can be shown from this popup
        parentView = view
        popupWindow.showAtLocation(view, gravity, x, y)
    }

    /**
     * The implementation of this will be fired onDismiss of the PopupWindow. Should be used for
     *  collating all useful information to some public variables so that the code that uses
     *  this PopupWindow can have access to the data that it called the PopupWindow to retrieve
     *  from the user.
     */
    protected abstract fun fetchFields()
}