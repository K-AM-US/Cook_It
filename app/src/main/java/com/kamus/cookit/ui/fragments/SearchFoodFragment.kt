package com.kamus.cookit.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.model.FavouriteRecipeEntity
import com.kamus.cookit.data.remote.model.CategoriesDto
import com.kamus.cookit.data.remote.model.RecipeDetailDto
import com.kamus.cookit.data.remote.model.RecipeDto
import com.kamus.cookit.databinding.FragmentSearchFoodBinding
import com.kamus.cookit.ui.adapters.CategoryAdapter
import com.kamus.cookit.ui.adapters.HomeRecipesVerticalAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFoodFragment : Fragment() {

    private var _binding: FragmentSearchFoodBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: AppRepository
    private var recipesTemp: List<RecipeDto> = emptyList()
    private lateinit var recipesAdapter: HomeRecipesVerticalAdapter
    private lateinit var linearLayoutM: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFoodBinding.inflate(inflater, container, false)
        linearLayoutM = LinearLayoutManager(requireActivity())
        initRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = (requireActivity().application as CookItApp).repository

        lifecycleScope.launch {
            val call: Call<List<CategoriesDto>> = repository.getCategories()
            call.enqueue(object: Callback<List<CategoriesDto>>{
                override fun onResponse(
                    call: Call<List<CategoriesDto>>,
                    response: Response<List<CategoriesDto>>
                ) {
                    response.body()?.let { categories ->
                        binding.rvCategories.apply {
                            layoutManager = GridLayoutManager(requireContext(), 2)
                            adapter = CategoryAdapter(categories){ category ->
                                categoryFilterClick(category)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<CategoriesDto>>, t: Throwable) {
                    Log.d("test categorias", "Error en categorias")
                }
            })
        }

        lifecycleScope.launch {
            val call: Call<List<RecipeDto>> = repository.getHomeRecipes()
            call.enqueue(object: Callback<List<RecipeDto>>{
                override fun onResponse(
                    call: Call<List<RecipeDto>>,
                    response: Response<List<RecipeDto>>
                ) {
                    Log.d("LOGS", "recipes: ${response.body()}")
                    response.body()?.let { recipes ->
                        recipesTemp = recipes
                        recipesAdapter.filteredRecipes(recipesTemp)
                    }
                }

                override fun onFailure(call: Call<List<RecipeDto>>, t: Throwable) {
                    Log.d("LOGS", "ERROOOOOOOOR")
                }
            })
        }

        binding.searchFood.addTextChangedListener { recipeFilter ->
            val filteredRecipes = recipesTemp.filter { recipe ->
                recipe.title.contains(recipeFilter.toString().trim(), true) ||
                        recipe.tags.contains(recipeFilter.toString().trim().lowercase()) ||
                        recipe.type.contains(recipeFilter.toString().trim(), true)
            }
            recipesAdapter.filteredRecipes(filteredRecipes)
        }
    }

    private fun initRecyclerView(){
        recipesAdapter = HomeRecipesVerticalAdapter(recipesTemp, onClickRecipe = {
            onClickedRecipe(it)
        }, favouriteOnClick = {
            favouriteOnClick(it)
        })
        binding.rvRecipes.layoutManager = linearLayoutM
        binding.rvRecipes.adapter = recipesAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFoodFragment()
    }

    private fun onClickedRecipe(recipe: RecipeDto) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, RecipeDetailFragment.newInstance(recipe.id))
            .addToBackStack(null)
            .commit()
    }

    private fun favouriteOnClick(recipe: RecipeDto) {
        lifecycleScope.launch {
            val call: Call<RecipeDetailDto> = repository.getRecipeDetail(recipe.id)
            call.enqueue(object: Callback<RecipeDetailDto>{
                override fun onResponse(
                    call: Call<RecipeDetailDto>,
                    response: Response<RecipeDetailDto>
                ) {
                    lifecycleScope.launch {
                        response.body()?.let { FavouriteRecipeEntity(recipe.id.toLong(), response.body()!!.title, response.body()!!.ingredients, it.process) }
                            ?.let {
                                if (repository.getFavouriteRecipeById(recipe.id) == null)
                                    repository.insertFavouriteRecipe(it)
                            }
                    }

                }

                override fun onFailure(call: Call<RecipeDetailDto>, t: Throwable) {
                    Log.d("FAVOURITES", "error añadiendo favoritas")
                }
            })
        }
    }

    private fun categoryFilterClick(category: CategoriesDto) {
        val filteredRecipes = recipesTemp.filter { recipe ->
            recipe.type.contains(category.category)
        }

        Log.d("categorias", "calor recibido: ${category.category} y recetas: ${recipesTemp[2].type}")
        recipesAdapter.filteredRecipes(filteredRecipes)
    }
}