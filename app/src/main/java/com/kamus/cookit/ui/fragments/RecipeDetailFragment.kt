package com.kamus.cookit.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.data.remote.model.RecipeDetailDto
import com.kamus.cookit.databinding.FragmentRecipeDetailBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val RECIPE_ID = "recipe_id"

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    private var recipeId: String? = null
    private lateinit var repository: AppRepository
    private lateinit var recipe: RecipeEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            recipeId = it.getString(RECIPE_ID)
            repository = (requireActivity().application as CookItApp).repository

            if(recipeId?.length!! > 3){
                val call: Call<RecipeDetailDto> = repository.getRecipeDetail(recipeId)
                call.enqueue(object: Callback<RecipeDetailDto> {
                    override fun onResponse(
                        call: Call<RecipeDetailDto>,
                        response: Response<RecipeDetailDto>
                    ) {
                        binding.apply {
                            recipeDetailTitle.text = response.body()?.title

                            response.body()?.ingredients?.forEach {
                                val ingredient = TextView(requireContext())
                                ingredient.text = it
                                recipeDetailIngredientsList.addView(ingredient)
                            }

                            response.body()?.process?.forEach {
                                val process = TextView(requireContext())
                                process.text = it
                                recipeDetailProcessList.addView(process)
                            }

                            Glide.with(requireContext())
                                .load(response.body()?.image)
                                .into(binding.recipeDetailImage)

                        }
                    }

                    override fun onFailure(call: Call<RecipeDetailDto>, t: Throwable) {
                        Log.d("DETAIL", "error en detail")
                    }
                })
            }else {
                lifecycleScope.launch {
                    recipe = repository.getRecipeByID(recipeId)
                    binding.apply {
                        recipeDetailTitle.text = recipe.title
                        recipeDetailImage.setImageResource(R.mipmap.ic_cookit_logo)
                        recipe.ingredients.forEach {
                            val ingredient = TextView(requireContext())
                            ingredient.text = it
                            recipeDetailIngredientsList.addView(ingredient)
                        }
                        recipe.process.forEach {
                            val process = TextView(requireContext())
                            process.text = it
                            recipeDetailProcessList.addView(process)
                        }
                    }
                }
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(recipeId: String) =
            RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(RECIPE_ID, recipeId)
                }
            }
    }
}