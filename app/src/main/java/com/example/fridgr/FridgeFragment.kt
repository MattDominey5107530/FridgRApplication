package com.example.fridgr

import android.annotation.TargetApi
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Ingredient
import com.example.fridgr.ingredient_search_component.IngredientSearchComponent

class FridgeFragment : Fragment() {

    private lateinit var ingredientSearchComponent: IngredientSearchComponent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_fridge, container, false)

        //Creates the subcat search component and adds it to the specified frame layout
        ingredientSearchComponent =
            IngredientSearchComponent(v.context)
        v.findViewById<FrameLayout>(R.id.frlIngredientSubcatSearcher)
            .addView(ingredientSearchComponent)

        //onClick listeners
        v.findViewById<Button>(R.id.btnSearch).setOnClickListener { onClickSearch() }
        v.findViewById<Button>(R.id.btnIngredientList)
            .setOnClickListener { onClickIngredientList() }

        v.findViewById<EditText>(R.id.edtIngredientSearch)
            .setOnEditorActionListener { _, id: Int, _ ->
                if (id == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }

        return v
    }

    private fun onClickSearch() {
        //TODO: getIngredientList from component; search for recipes through API and switch to recipesearch fragment (fragment to display the results of your search)
    }

    private fun onClickIngredientList() {
        //TODO: if theres no ingredients checked then dont show popupwindow!

        showIngredientListPopup()
    }

    companion object {
        fun newInstance(): FridgeFragment = FridgeFragment()
    }

    //TODO: should definitely be a class
    private fun showIngredientListPopup() {
        // Initialize a new layout inflater instance
        val inflater: LayoutInflater = LayoutInflater.from(this.context)

        // Inflate a custom view using layout inflater
        val v = inflater.inflate(R.layout.popupwindow_ingredient_list, null)

        // Initialize a new instance of popup window
        val popupWindow = PopupWindow(
            v, // Custom view to show in popup window
            1000, // Width of popup window
            1500 // Window height
        )

        @TargetApi(21)
        popupWindow.elevation = 10.0F

        class CheckedIngredientAdapter(val myDataset: ArrayList<Ingredient>) :
            RecyclerView.Adapter<CheckedIngredientAdapter.IngredientIconViewHolder>() {

            inner class IngredientIconViewHolder(val ingredientComponent: View) :
                RecyclerView.ViewHolder(ingredientComponent)

            //Create the view
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): IngredientIconViewHolder {

                val ingredientComponent = inflater.inflate(R.layout.ingredient_list_component, null)

                return IngredientIconViewHolder(ingredientComponent)
            }

            //Populate the view
            override fun onBindViewHolder(holder: IngredientIconViewHolder, position: Int) {
                with(holder.ingredientComponent) {
                    findViewById<ImageView>(R.id.imvIngredientIcon)
                        .setImageBitmap(myDataset[position].image)
                    findViewById<TextView>(R.id.txvIngredientName)
                        .text = myDataset[position].name
                    findViewById<ImageButton>(R.id.btnDeleteIngredient)
                        .setOnClickListener {
                            myDataset.remove(myDataset[position])
                            notifyDataSetChanged()
                        }
                }
            }

            // Return the size of your dataset
            override fun getItemCount() = myDataset.size
        }

        val closeButton: ImageButton = v.findViewById(R.id.btnCloseIngredientList)
        val recyclerViewAdapter =
            CheckedIngredientAdapter(ingredientSearchComponent.checkedIngredients)
        val ingredientListRecyclerView: RecyclerView =
            v.findViewById<RecyclerView>(R.id.rcvIngredientContainer).apply {
                setHasFixedSize(true)
                adapter = recyclerViewAdapter
                layoutManager = LinearLayoutManager(context)
            }

        closeButton.setOnClickListener {
            // Dismiss the popup window
            popupWindow.dismiss()
        }

        //Handle closing of the PopupWindow if a click lands outside of the PopupWindow
        with(popupWindow) {
            setOnDismissListener {
                val newCheckedIngredients = recyclerViewAdapter.myDataset
                ingredientSearchComponent.updateCheckedIngredients(newCheckedIngredients)
            }
            isOutsideTouchable = true
            isFocusable = true
        }

        //Show the popup window on app
        popupWindow.showAtLocation(
            this.view, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            -75 // Y offset
        )
    }
}