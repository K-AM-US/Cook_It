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
import com.kamus.cookit.databinding.FragmentFavouritesFragmentsBinding
import com.kamus.cookit.ui.adapters.FavouriteRecipesAdapter
import kotlinx.coroutines.launch


class FavouritesFragments : Fragment() {

    private var _binding: FragmentFavouritesFragmentsBinding? = null
    private val binding get() = _binding!!
    private var recipes: List<FavouriteRecipeEntity> = emptyList()
    private lateinit var repository: AppRepository
    private lateinit var recipeAdapter: FavouriteRecipesAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesFragmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as CookItApp).repository
        recipeAdapter = FavouriteRecipesAdapter(favouriteRecipeClicked = {
            favouriteRecipeClicked(it)
        }, deleteFavouriteRecipe = {
            deleteFavourite(it)
            updateUI()
        })


        binding.rvFavourites.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = recipeAdapter
        }

        updateUI()
    }

    private fun updateUI() {
        lifecycleScope.launch {
            recipes = repository.getFavouriteRecipes()
            recipeAdapter.updateList(recipes)
        }

    }

    private fun favouriteRecipeClicked(recipe: FavouriteRecipeEntity){
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, RecipeDetailFragment.newInstance(recipe.id.toString()))
            .addToBackStack(null)
            .commit()

    }

    private fun deleteFavourite(recipe: FavouriteRecipeEntity) {
        lifecycleScope.launch {
            repository.deleteFavouriteRecipe(recipe)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {

        @JvmStatic
        fun newInstance() = FavouritesFragments()
    }
}