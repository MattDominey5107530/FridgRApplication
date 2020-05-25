package com.example.fridgr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import api.SpoonacularAPIHandler
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null
    private var recipeId: Int = -1

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null,
            recipeId: Int
        ): RecipeFragment =
            RecipeFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
                this.recipeId = recipeId
            }
    }

    private lateinit var imageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var carbTextView: TextView
    private lateinit var fatTextView: TextView
    private lateinit var proteinTextView: TextView
    private lateinit var caloryTextView: TextView
    private lateinit var ingredientListTextView: TextView
    private lateinit var stepListTextView: TextView


    /**
     * Fragment instantiation
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View =
            inflater.inflate(R.layout.fragment_recipe, container, false) //TODO: fragment layout

        v.findViewById<ImageButton>(R.id.imbBack).setOnClickListener {
            switchToFragment(this@RecipeFragment, myParentFragment!!)
        }

        imageView = v.findViewById(R.id.imvRecipePicture)
        nameTextView = v.findViewById(R.id.txvName)
        timeTextView = v.findViewById(R.id.txvTime)
        carbTextView = v.findViewById(R.id.txvCarbs)
        fatTextView = v.findViewById(R.id.txvFats)
        proteinTextView = v.findViewById(R.id.txvProtein)
        caloryTextView = v.findViewById(R.id.txvCalories)
        ingredientListTextView = v.findViewById(R.id.txvIngredientList)
        stepListTextView = v.findViewById(R.id.txvStepList)

        populateFragment()

        return v
    }

    private fun populateFragment() {
        CoroutineScope(IO).launch {
            //Get all of the information about the recipe from the Api
            val recipeInfo =
                SpoonacularAPIHandler.getRecipeInfo(recipeId)
            val recipeInstructions =
                SpoonacularAPIHandler.getRecipeInstructions(recipeId)

            withContext(Main) {
                //Actually populate the fields
                if (recipeInfo != null && recipeInstructions != null) {
                    with(recipeInfo) {
                        Picasso.get()
                            .load(image)
                            .into(imageView)
                        nameTextView.text = title
                        timeTextView.text = "${readyInMinutes}m"

                        carbTextView.text =
                            nutrition.nutrients.first { it.title == "Calories" }.amount.toInt()
                                .toString()
                        fatTextView.text =
                            nutrition.nutrients.first { it.title == "Fat" }.amount.toInt()
                                .toString()
                        proteinTextView.text =
                            nutrition.nutrients.first { it.title == "Carbohydrates" }.amount.toInt()
                                .toString()
                        caloryTextView.text =
                            nutrition.nutrients.first { it.title == "Protein" }.amount.toInt()
                                .toString()

                        ingredientListTextView.text =
                            extendedIngredients.joinToString("\n") { "- ${it.originalString}" }
                    }
                    recipeInstructions.joinToString { rI ->
                        rI.steps.joinToString { step -> "${step.number}. ${step.step}" }
                    }

                    var stepListText = ""
                    for (recipeInstruction in recipeInstructions) {
                        stepListText += "${recipeInstruction.name}\n" +
                                recipeInstruction.steps.joinToString("\n\n") { step -> "${step.number}. ${step.step}" } +
                                "\n\n\n"
                    }
                    stepListTextView.text = stepListText
                } else {
                    Toast.makeText(
                        context,
                        "Something went wrong. Try again later.",
                        Toast.LENGTH_LONG
                    ).show()
                    switchToFragment(this@RecipeFragment, myParentFragment!!)
                }
            }

        }
    }
}