package com.kamus.cookit.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.model.FavouriteRecipeEntity
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.data.remote.model.RecipeDetailDto
import com.kamus.cookit.databinding.FragmentRecipeDetailBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

private const val RECIPE_ID = "recipe_id"

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    private var recipeId: String? = null
    private var firebaseAuth: FirebaseAuth? = null
    private lateinit var repository: AppRepository
    private lateinit var recipe: RecipeEntity
    private lateinit var builder: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        builder = AlertDialog.Builder(requireContext())

        arguments?.let {
            recipeId = it.getString(RECIPE_ID)
            repository = (requireActivity().application as CookItApp).repository

            if (recipeId?.length!! > 3) {
                binding.apply {
                    connectionErrorButton.setOnClickListener {
                        load()
                    }
                    connectionErrorButton.performClick()
                }

            } else {
                binding.connectionErrorButton.visibility = View.GONE
                binding.connectionErrorMessage.visibility = View.GONE
                lifecycleScope.launch {
                    recipe = repository.getRecipeByID(recipeId)
                    binding.apply {
                        recipeDetailTitle.text = recipe.title
                        recipeDetailImage.setImageResource(R.mipmap.ic_cookit_logo)
                        recipe.ingredients.forEach {
                            val ingredient = TextView(requireContext())
                            ingredient.text = it
                            ingredient.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                            recipeDetailIngredientsList.addView(ingredient)
                        }
                        recipe.process.forEach {
                            val process = TextView(requireContext())
                            process.text = it
                            process.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                            recipeDetailProcessList.addView(process)
                        }

                        btnDelete.setOnClickListener {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Confirmación")
                                .setMessage("¿Realmente deseas eliminar la receta?")
                                .setPositiveButton(
                                    "aceptar",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        lifecycleScope.launch {
                                            repository.deleteRecipe(recipe)
                                            if (repository.getFavouriteRecipeById(recipe.id.toString()) != null)
                                                repository.deleteFavouriteRecipe(
                                                    FavouriteRecipeEntity(
                                                        recipe.id,
                                                        firebaseAuth?.currentUser?.uid.toString(),
                                                        recipe.title,
                                                        recipe.ingredients,
                                                        recipe.process
                                                    )
                                                )
                                            parentFragmentManager.beginTransaction()
                                                .replace(
                                                    R.id.fragmentContainer,
                                                    AccountFragment.newInstance(
                                                        "0",
                                                        "",
                                                        "",
                                                        "",
                                                        ArrayList()
                                                    )
                                                )
                                                .addToBackStack(null)
                                                .commit()
                                        }
                                    }).setNegativeButton(
                                    "cancelar",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        dialog.dismiss()
                                    })
                                .create()
                                .show()
                        }

                        btnEdit.setOnClickListener {
                            parentFragmentManager.beginTransaction()
                                .replace(
                                    R.id.fragmentContainer,
                                    NewRecipeFragment.newInstance(
                                        recipeId = recipe.id.toString(),
                                        recipeTitle = recipe.title,
                                        recipeIngredients = recipe.ingredients,
                                        recipeProcess = recipe.process,
                                        newRecipe = false
                                    )
                                )
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }
            }
        }
    }

    private fun load() {
        binding.connectionErrorButton.visibility = View.GONE
        binding.connectionErrorMessage.visibility = View.GONE
        binding.btnDelete.visibility = View.GONE
        binding.btnEdit.visibility = View.GONE
        val call: Call<RecipeDetailDto> = repository.getRecipeDetail(recipeId)
        call.enqueue(object : Callback<RecipeDetailDto> {
            override fun onResponse(
                call: Call<RecipeDetailDto>,
                response: Response<RecipeDetailDto>
            ) {
                binding.apply {
                    recipeDetailTitle.text = response.body()?.title

                    response.body()?.ingredients?.forEach {
                        val ingredient = TextView(requireContext())
                        ingredient.text = it
                        ingredient.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                        recipeDetailIngredientsList.addView(ingredient)
                    }

                    response.body()?.process?.forEach {
                        val process = TextView(requireContext())
                        process.text = it
                        process.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                        recipeDetailProcessList.addView(process)
                    }

                    Glide.with(requireContext())
                        .load(response.body()?.image)
                        .into(binding.recipeDetailImage)
                }
            }

            override fun onFailure(call: Call<RecipeDetailDto>, t: Throwable) {
                binding.connectionErrorButton.visibility = View.VISIBLE
                binding.connectionErrorMessage.visibility = View.VISIBLE
                binding.connectionErrorButton.setOnClickListener {
                    load()
                }
            }
        })
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