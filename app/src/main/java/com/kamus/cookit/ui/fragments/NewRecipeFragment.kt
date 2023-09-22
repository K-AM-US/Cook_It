package com.kamus.cookit.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.databinding.FragmentNewRecipeBinding
import kotlinx.coroutines.launch
import java.io.IOException

class NewRecipeFragment(
    private val updateUI: () -> Unit
): Fragment() {
    private var _binding: FragmentNewRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: AppRepository
    private var recipe: RecipeEntity = RecipeEntity(
        id = 0,
        title = "lala",
        ingredients = "",
        process = ""
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = (requireActivity().application as CookItApp).repository

        binding.btnCreate.setOnClickListener {
            recipe.id = (1..100).random().toLong()
            recipe.title = binding.recipeTitle.text.toString()
            recipe.ingredients = binding.ingredientsList.text.toString()
            recipe.process = binding.processList.text.toString()

            try {
                lifecycleScope.launch {
                    repository.insertRecipe(recipe)
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, AccountFragment.newInstance())
                    .commit()
                updateUI()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireActivity(), "Receta error", Toast.LENGTH_SHORT).show()
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
        fun newInstance() = NewRecipeFragment(){

        }
    }
}