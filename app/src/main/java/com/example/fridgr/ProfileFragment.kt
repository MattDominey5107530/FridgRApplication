package com.example.fridgr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.Cuisine
import api.Diet
import api.Intolerance
import com.example.fridgr.local_storage.*
import com.example.fridgr.profile_settings.ProfileSettingsFragment

class ProfileFragment : Fragment() {

    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null
        ): ProfileFragment =
            ProfileFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
            }
    }

    private lateinit var profilePictureImageView: ImageView

    private lateinit var recyclerViewDiets: RecyclerView
    private lateinit var recyclerViewDietsAdapter: DietAdapter
    private lateinit var recyclerViewDietsLayoutManager: RecyclerView.LayoutManager

    private lateinit var recyclerViewCuisines: RecyclerView
    private lateinit var recyclerViewCuisinesAdapter: CuisineAdapter
    private lateinit var recyclerViewCuisinesLayoutManager: RecyclerView.LayoutManager

    /**
     * Fragment instantiation
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_profile, container, false)

        //Get reference to all components
        profilePictureImageView = v.findViewById(R.id.imvProfilePicture)

        recyclerViewDiets = v.findViewById<RecyclerView>(R.id.rcvDietaryRequirements).apply {
            recyclerViewDietsAdapter = DietAdapter(emptyList())
            recyclerViewDietsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            layoutManager = recyclerViewDietsLayoutManager
            adapter = recyclerViewDietsAdapter
        }

        recyclerViewCuisines = v.findViewById<RecyclerView>(R.id.rcvFavouriteCuisines).apply {
            recyclerViewCuisinesAdapter = CuisineAdapter(emptyList())
            recyclerViewCuisinesLayoutManager = GridLayoutManager(context, 2, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            layoutManager = recyclerViewCuisinesLayoutManager
            adapter = recyclerViewCuisinesAdapter
        }

        if (isUserLoggedIn(context!!)) {
            updateFields()
        } else {
            //Show loginFragment
            val loginFragment = LoginFragment.newInstance(switchToFragment, this)
            switchToFragment(this, loginFragment)
        }

        v.findViewById<ImageButton>(R.id.imbProfileSettings)
            .setOnClickListener {
                val profileSettingsFragment =
                    ProfileSettingsFragment.newInstance(switchToFragment, this)
                switchToFragment(this, profileSettingsFragment)
            }

        //TODO: Add edit button for name and profile picture (perhaps use Google profile picture as default if we allow them to login via Google)

        return v
    }

    /**
     * Function which sets all the information to be displayed in all fields
     */
    fun updateFields() {
        updateProfilePicture()
        updateDiets()
        updateCuisines()
    }

    /**
     * Function which gets the profile picture from local storage and adds it to the fragment.
     */
    private fun updateProfilePicture() {
        //Get the profile picture, if there is one
        val profilePicture = getProfilePicture(context!!)
        if (profilePicture != null) {
            profilePictureImageView
                .setImageDrawable(profilePicture.getCircularDrawable(resources))
        } else {
            //Set the profile picture to the default if the user doesn't have one yet
            profilePictureImageView
                .setImageDrawable(
                    ContextCompat.getDrawable(context!!, R.drawable.ic_profile)!!
                        .toBitmap(
                            profilePictureImageView.layoutParams.width,
                            profilePictureImageView.layoutParams.height)
                        .getCircularDrawable(resources)
                )
        }
    }

    private fun updateDiets() {
        val userPreferences = getUserPreferences(context!!)
        val adapterPairs =  arrayListOf<Pair<Diet?, Intolerance?>>()

        if (userPreferences != null) {
            if (userPreferences.diet != null) {
                adapterPairs.add(Pair(userPreferences.diet, null))
            }

            for (intolerance in userPreferences.intolerances) {
                adapterPairs.add(Pair(null, intolerance))
            }
        }

        adapterPairs.add(Pair(null, null)) //Add the "Add more diets/cuisines" button

        recyclerViewDietsAdapter.myDataset = adapterPairs
        recyclerViewDietsAdapter.notifyDataSetChanged()
    }

    private fun updateCuisines() {
        val cuisines = getUserCuisines(context!!)
        val adapterCuisines = ArrayList<Cuisine?>()

        if (cuisines != null) {
            adapterCuisines.addAll(cuisines)
        }

        adapterCuisines.add(null) //Add the "Add more cuisines" button

        recyclerViewCuisinesAdapter.myDataset = adapterCuisines
        recyclerViewCuisinesAdapter.notifyDataSetChanged()
    }

    /**
     * Adapter which displays both the user's diet preferences and intolerances.
     */
    private inner class DietAdapter(var myDataset: List<Pair<Diet?, Intolerance?>>) :
        RecyclerView.Adapter<DietAdapter.DietViewHolder>() {

        inner class DietViewHolder(val view: View) :
            RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): DietViewHolder {
            val userPreferenceIcon: View = LayoutInflater.from(context)
                .inflate(R.layout.user_preference_icon, parent, false)

            return DietViewHolder(userPreferenceIcon)
        }

        override fun onBindViewHolder(holder: DietViewHolder, position: Int) {
            with(holder.view) {
                findViewById<ImageView>(R.id.imvIcon)
                    .setImageResource(
                        //TODO: add actual icons
                        if (myDataset[position].first != null) {
                            when (myDataset[position].first!!) {
                                Diet.GLUTEN_FREE -> R.drawable.sweet_snacks
                                Diet.KETOGENIC -> R.drawable.sweet_snacks
                                Diet.VEGETARIAN -> R.drawable.sweet_snacks
                                Diet.LACTO_VEGETARIAN -> R.drawable.sweet_snacks
                                Diet.OVO_VEGETARIAN -> R.drawable.sweet_snacks
                                Diet.VEGAN -> R.drawable.sweet_snacks
                                Diet.PESCETARIAN -> R.drawable.sweet_snacks
                                Diet.PALEO -> R.drawable.sweet_snacks
                                Diet.PRIMAL -> R.drawable.sweet_snacks
                                Diet.WHOLE30 -> R.drawable.sweet_snacks
                            }
                        } else {
                            //TODO: add actual icons, again
                            when (myDataset[position].second) {
                                Intolerance.DAIRY -> R.drawable.savory_snacks
                                Intolerance.EGG -> R.drawable.savory_snacks
                                Intolerance.GLUTEN -> R.drawable.savory_snacks
                                Intolerance.GRAIN -> R.drawable.savory_snacks
                                Intolerance.PEANUT -> R.drawable.savory_snacks
                                Intolerance.SEAFOOD -> R.drawable.savory_snacks
                                Intolerance.SESAME -> R.drawable.savory_snacks
                                Intolerance.SHELLFISH -> R.drawable.savory_snacks
                                Intolerance.SOY -> R.drawable.savory_snacks
                                Intolerance.SULFITE -> R.drawable.savory_snacks
                                Intolerance.TREE_NUT -> R.drawable.savory_snacks
                                Intolerance.WHEAT -> R.drawable.savory_snacks
                                else -> R.drawable.savory_snacks //TODO: add more diets/intolerances button
                            }
                        }
                    )
            }
        }

        override fun getItemCount(): Int = myDataset.size
    }

    private inner class CuisineAdapter(var myDataset: List<Cuisine?>) :
        RecyclerView.Adapter<CuisineAdapter.CuisineViewHolder>() {

        inner class CuisineViewHolder(val view: View) :
            RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CuisineViewHolder {
            val userPreferenceIcon: View = LayoutInflater.from(context)
                .inflate(R.layout.user_preference_icon, parent, false)

            return CuisineViewHolder(userPreferenceIcon)
        }

        override fun onBindViewHolder(holder: CuisineViewHolder, position: Int) {
            with(holder.view) {
                findViewById<ImageView>(R.id.imvIcon)
                    .setImageResource(
                        //TODO: add actual icons
                        when (myDataset[position]) {
                            Cuisine.AFRIAN -> R.drawable.ic_vegetables
                            Cuisine.AMERICAN -> R.drawable.ic_vegetables
                            Cuisine.BRITISH -> R.drawable.ic_vegetables
                            Cuisine.CAJUN -> R.drawable.ic_vegetables
                            Cuisine.CARIBBEAN -> R.drawable.ic_vegetables
                            Cuisine.CHINESE -> R.drawable.ic_vegetables
                            Cuisine.EASTERN_EUROPEAN -> R.drawable.ic_vegetables
                            Cuisine.EUROPEAN -> R.drawable.ic_vegetables
                            Cuisine.FRENCH -> R.drawable.ic_vegetables
                            Cuisine.GERMAN -> R.drawable.ic_vegetables
                            Cuisine.GREEK -> R.drawable.ic_vegetables
                            Cuisine.INDIAN -> R.drawable.ic_vegetables
                            Cuisine.IRISH -> R.drawable.ic_vegetables
                            Cuisine.ITALIAN -> R.drawable.ic_vegetables
                            Cuisine.JAPANESE -> R.drawable.ic_vegetables
                            Cuisine.JEWISH -> R.drawable.ic_vegetables
                            Cuisine.KOREAN -> R.drawable.ic_vegetables
                            Cuisine.LATIN_AMERICAN -> R.drawable.ic_vegetables
                            Cuisine.MEDITERRANEAN -> R.drawable.ic_vegetables
                            Cuisine.MEXICAN -> R.drawable.ic_vegetables
                            Cuisine.MIDDLE_EASTERN -> R.drawable.ic_vegetables
                            Cuisine.NORDIC -> R.drawable.ic_vegetables
                            Cuisine.SOUTHERN -> R.drawable.ic_vegetables
                            Cuisine.SPANISH -> R.drawable.ic_vegetables
                            Cuisine.THAI -> R.drawable.ic_vegetables
                            Cuisine.VIETAMESE -> R.drawable.ic_vegetables
                            else -> R.drawable.ic_vegetables //TODO: add more cuisines button...
                        }

                    )
            }
        }

        override fun getItemCount(): Int = myDataset.size
    }
}
