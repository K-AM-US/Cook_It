package com.kamus.cookit.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.model.FavouriteRecipeEntity
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.databinding.FragmentAccountBinding
import com.kamus.cookit.ui.adapters.ProfileRecipesAdapter
import kotlinx.coroutines.launch


/* TODO: Funcionalidad
*   1. Click en estrella, agregar receta a favoritos
*   2. Click en comentarios, dialog con comentarios
*   3. Click en share, copia link a la receta */

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private var recipes: List<RecipeEntity> = emptyList()
    private lateinit var repository: AppRepository
    private lateinit var recipeAdapter: ProfileRecipesAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as CookItApp).repository
        recipeAdapter = ProfileRecipesAdapter(onClickedRecipe = {
            onClickedRecipe(it)
        },favouriteOnClick = {
            favouriteOnClick(it)
        })

        binding.settingIcon.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AccountSettingsFragment.newInstance())
                .addToBackStack("AccountSettingsFragment")
                .commit()
        }
        binding.addBox.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, NewRecipeFragment.newInstance(recipeId = "", recipeTitle = "", recipeIngredients = ArrayList<String>(), recipeProcess = ArrayList<String>(), newRecipe = true))
                .addToBackStack("NewRecipeFragment")
                .commit()
        }
        binding.favourites.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FavouritesFragments.newInstance())
                .addToBackStack("FavouritesFragment")
                .commit()
        }
        binding.friends.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FriendsFragment.newInstance())
                .addToBackStack("FriendsFragment")
                .commit()
        }

        binding.rvRecipes.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = recipeAdapter
        }
        updateUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateUI(){
        lifecycleScope.launch {
            recipes = repository.getRecipes()
            if(recipes.isNotEmpty())
                binding.noRecipesMessage.visibility = View.INVISIBLE
            else
                binding.noRecipesMessage.visibility = View.VISIBLE
            recipeAdapter.updateList(recipes)
        }
    }
    
    private fun onClickedRecipe(recipe: RecipeEntity) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, RecipeDetailFragment.newInstance(recipe.id.toString()))
            .addToBackStack(null)
            .commit()
    }

    private fun favouriteOnClick(recipe: RecipeEntity) {
        lifecycleScope.launch {
            if (repository.getFavouriteRecipeById(recipe.id.toString()) == null)
                repository.insertFavouriteRecipe(FavouriteRecipeEntity(recipe.id, recipe.title, recipe.ingredients, recipe.process))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AccountFragment()
    }
}