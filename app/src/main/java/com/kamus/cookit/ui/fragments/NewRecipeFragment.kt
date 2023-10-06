package com.kamus.cookit.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.view.get
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
        ingredients = ArrayList<String>(),
        process = ArrayList<String>()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = (requireActivity().application as CookItApp).repository

        binding.btnCreate.setOnClickListener {
            recipe.id = (1..100).random().toLong()
            recipe.title = binding.recipeTitle.text.toString()

            for(item in 0 until binding.linearLayoutIngredients.childCount){
                val tmpEditText = binding.linearLayoutIngredients.getChildAt(item) as EditText
                recipe.ingredients.add(tmpEditText.text.toString())
            }

            for(item in 0 until binding.linearLayoutProcess.childCount){
                val tmpEditText = binding.linearLayoutProcess.getChildAt(item) as EditText
                recipe.process.add(tmpEditText.text.toString())
            }

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
                newEditText.hint = "Ingrediente"
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
        fun newInstance() = NewRecipeFragment(){

        }
    }
}