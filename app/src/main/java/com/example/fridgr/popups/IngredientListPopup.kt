package com.example.fridgr.popups

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Ingredient
import com.example.fridgr.R
import com.squareup.picasso.Picasso

class IngredientListPopup(
    context: Context,
    onDismissListener: (AbstractPopup) -> Unit,
    checkedIngredientList: ArrayList<Ingredient>
) :
    AbstractPopup(
        context,
        R.layout.popupwindow_ingredient_list, onDismissListener, 300, 510
    ) {

    //Public field for caller to access
    lateinit var newCheckedIngredients: ArrayList<Ingredient>

    private val closeButton: ImageButton =
        view.findViewById(R.id.btnCloseIngredientList)
    private val ingredientListRecyclerView: RecyclerView =
        view.findViewById(R.id.rcvIngredientContainer)
    private val recyclerViewAdapter: CheckedIngredientAdapter

    init {
        val swipeHandler = object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = ingredientListRecyclerView.adapter as CheckedIngredientAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(ingredientListRecyclerView)

        recyclerViewAdapter = CheckedIngredientAdapter(checkedIngredientList)
        ingredientListRecyclerView.apply {
            setHasFixedSize(true)
            adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        closeButton.setOnClickListener {
            // Dismiss the popup window
            popupWindow.dismiss()
        }

        with (popupWindow) {
            isOutsideTouchable = true
            isFocusable = true
        }
    }

    override fun fetchFields() {
        newCheckedIngredients = recyclerViewAdapter.myDataset
    }

    inner class CheckedIngredientAdapter(val myDataset: ArrayList<Ingredient>) :
        RecyclerView.Adapter<CheckedIngredientAdapter.IngredientIconViewHolder>() {

        private val inflater: LayoutInflater = LayoutInflater.from(context)

        inner class IngredientIconViewHolder(val ingredientComponent: View) :
            RecyclerView.ViewHolder(ingredientComponent)

        //Create the view
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): IngredientIconViewHolder {

            val ingredientComponent = inflater.inflate(
                R.layout.ingredient_list_component,
                ingredientListRecyclerView,
                false
            )

            return IngredientIconViewHolder(ingredientComponent)
        }

        //Populate the view
        override fun onBindViewHolder(holder: IngredientIconViewHolder, position: Int) {
            with(holder.ingredientComponent) {
                Picasso.get()
                    .load(myDataset[position].imageString)
                    .into(findViewById<ImageView>(R.id.imvIngredientIcon))
                findViewById<TextView>(R.id.txvIngredientName)
                    .text = myDataset[position].name
            }
        }

        // Return the size of your dataset
        override fun getItemCount() = myDataset.size

        fun removeAt(position: Int) {
            myDataset.removeAt(position)
            notifyItemRemoved(position)
        }
    }

}