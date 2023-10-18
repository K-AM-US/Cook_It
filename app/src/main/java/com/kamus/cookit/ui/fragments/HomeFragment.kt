package com.kamus.cookit.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.model.FavouriteRecipeEntity
import com.kamus.cookit.data.remote.model.RecipeDetailDto
import com.kamus.cookit.data.remote.model.RecipeDto
import com.kamus.cookit.databinding.FragmentHomeBinding
import com.kamus.cookit.ui.adapters.HomeRecipesVerticalAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: AppRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as CookItApp).repository

        binding.loginIcon.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LoginFragment.newInstance())
                .addToBackStack("LoginFragment")
                .commit()
        }

        binding.btnAddRecipe.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, NewRecipeFragment.newInstance(recipeId = "", recipeTitle = "", recipeIngredients = ArrayList<String>(), recipeProcess = ArrayList<String>(), newRecipe = true))
                .addToBackStack("NewRecipeFragment")
                .commit()
        }

        lifecycleScope.launch {
            val call: Call<List<RecipeDto>> = repository.getHomeRecipes()
            call.enqueue(object: Callback<List<RecipeDto>>{
                override fun onResponse(
                    call: Call<List<RecipeDto>>,
                    response: Response<List<RecipeDto>>
                ) {
                    response.body()?.reversed().let { recipes ->
                        binding.rvCarousel.apply {
                            adapter = recipes?.let {
                                HomeRecipesVerticalAdapter(it, onClickRecipe = {
                                    onClickedRecipe(it)
                                }, favouriteOnClick = {
                                    favouriteOnClick(it)
                                })
                            }
                            set3DItem(true)
                            setAlpha(true)
                            setInfinite(true)
                        }
                    }
                }

                override fun onFailure(call: Call<List<RecipeDto>>, t: Throwable) {
                    Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show()
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
                        binding.rvHomeRecipes.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = HomeRecipesVerticalAdapter(recipes, onClickRecipe = {
                                onClickedRecipe(it)
                            }, favouriteOnClick = {
                                favouriteOnClick(it)
                            })
                            Log.d("LOGS", "Adapter: $adapter.")
                        }
                    }
                }

                override fun onFailure(call: Call<List<RecipeDto>>, t: Throwable) {
                    Log.d("LOGS", "ERROOOOOOOOR")
                }
            })
        }
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
                                lifecycleScope.launch {
                                    if (repository.getFavouriteRecipeById(recipe.id) == null)
                                        repository.insertFavouriteRecipe(it)
                                }
                            }
                    }

                }

                override fun onFailure(call: Call<RecipeDetailDto>, t: Throwable) {
                    Log.d("FAVOURITES", "error añadiendo favoritas")
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}