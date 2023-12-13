package com.kamus.cookit.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.model.FavouriteRecipeEntity
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.databinding.FragmentNewRecipeBinding
import kotlinx.coroutines.launch
import java.io.IOException

private const val RECIPE_ID = "recipe_id"
private const val RECIPE_TITLE = "recipe_title"
private const val RECIPE_INGREDIENTS = "recipe_ingredients"
private const val RECIPE_PROCESS = "recipe_process"
private const val NEW_RECIPE = "new_recipe"

class NewRecipeFragment(
    private val updateUI: () -> Unit
): Fragment() {
    private var _binding: FragmentNewRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: AppRepository
    private var recipe: RecipeEntity = RecipeEntity(
        id = 0,
        title = "",
        ingredients = ArrayList<String>(),
        process = ArrayList<String>(),
        img = ""
    )

    private lateinit var getRecipeId: String
    private lateinit var getRecipeTitle: String
    private lateinit var getRecipeIngredients: ArrayList<String>
    private lateinit var getRecipeProcess: ArrayList<String>
    private var getNewRecipe = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = (requireActivity().application as CookItApp).repository

        arguments.let {
            getRecipeId = it?.getString(RECIPE_ID).toString()
            getRecipeTitle = it?.getString(RECIPE_TITLE).toString()
            getRecipeIngredients = it?.getStringArrayList(RECIPE_INGREDIENTS)!!
            getRecipeProcess = it.getStringArrayList(RECIPE_PROCESS)!!
            getNewRecipe = it.getBoolean(NEW_RECIPE)
        }

        if (!getNewRecipe){
            binding.apply {
                recipeTitle.setText(getRecipeTitle)
                getRecipeIngredients.forEach {
                    val ingredient = EditText(requireContext())
                    ingredient.setText(it)
                    linearLayoutIngredients.addView(ingredient)
                }
                getRecipeProcess.forEach {
                    val process = EditText(requireContext())
                    process.setText(it)
                    linearLayoutProcess.addView(process)
                }
            }
        }

        binding.btnCreate.setOnClickListener {
            recipe.title = binding.recipeTitle.text.toString()
            for(item in 0 until binding.linearLayoutIngredients.childCount){
                val tmpEditText = binding.linearLayoutIngredients.getChildAt(item) as EditText
                if(tmpEditText.text.toString() != "")
                    recipe.ingredients.add(tmpEditText.text.toString())
            }
            for(item in 0 until binding.linearLayoutProcess.childCount){
                val tmpEditText = binding.linearLayoutProcess.getChildAt(item) as EditText
                if(tmpEditText.text.toString() != "")
                    recipe.process.add(tmpEditText.text.toString())
            }
            if(recipe.title.isNotEmpty()) {
                try {
                    if (getNewRecipe) {
                        recipe.id = (1..100).random().toLong()
                        lifecycleScope.launch {
                            repository.insertRecipe(recipe)
                        }
                    }
                    else{
                        recipe.id = getRecipeId.toLong()
                        lifecycleScope.launch {
                            repository.updateRecipe(recipe)
                            repository.updateFavouriteRecipe(
                                FavouriteRecipeEntity(
                                    recipe.id,
                                    recipe.title,
                                    recipe.ingredients,
                                    recipe.process
                                )
                            )
                        }
                    }

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, AccountFragment.newInstance("0","","", "", ArrayList()))
                        .commit()
                    updateUI()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireActivity(), "Receta error", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireActivity(), "Faltan datos en la receta", Toast.LENGTH_SHORT).show()
            }
            /* TODO: falta validar que no hayan cadenas vacías y que no estén vacios los arreglos */
        }

        binding.apply {
            addIngredient.setOnClickListener{
                val newEditText = EditText(requireActivity())
                newEditText.hint = "Ingrediente"
                linearLayoutIngredients.addView(newEditText)
            }

            removeIngredients.setOnClickListener {
                if(linearLayoutIngredients.childCount > 0)
                    linearLayoutIngredients.removeViewAt(linearLayoutIngredients.childCount - 1)
            }

            addProcess.setOnClickListener{
                val newEditText = EditText(requireActivity())
                newEditText.hint = "Proceso"
                linearLayoutProcess.addView(newEditText)
            }

            removeProcess.setOnClickListener {
                if(linearLayoutProcess.childCount > 0)
                    linearLayoutProcess.removeViewAt(linearLayoutProcess.childCount - 1)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(recipeId: String, recipeTitle: String, recipeIngredients: ArrayList<String>, recipeProcess: ArrayList<String>, newRecipe: Boolean) = NewRecipeFragment(updateUI = {

        }).apply {
            arguments = Bundle().apply {
                putString(RECIPE_ID, recipeId)
                putString(RECIPE_TITLE, recipeTitle)
                putStringArrayList(RECIPE_INGREDIENTS, recipeIngredients)
                putStringArrayList(RECIPE_PROCESS, recipeProcess)
                putBoolean(NEW_RECIPE, newRecipe)
            }
        }
    }
}