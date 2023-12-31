package com.kamus.cookit.ui.fragments

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.model.FavouriteRecipeEntity
import com.kamus.cookit.databinding.FragmentFavouritesFragmentsBinding
import com.kamus.cookit.ui.adapters.FavouriteRecipesAdapter
import com.kamus.cookit.utils.Constants
import kotlinx.coroutines.launch


class FavouritesFragments : Fragment() {

    private var _binding: FragmentFavouritesFragmentsBinding? = null
    private val binding get() = _binding!!
    private var recipes: List<FavouriteRecipeEntity> = emptyList()
    private var firebaseAuth: FirebaseAuth? = null
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
        firebaseAuth = FirebaseAuth.getInstance()

        recipeAdapter = FavouriteRecipesAdapter(favouriteRecipeClicked = {
            favouriteRecipeClicked(it)
        }, deleteFavouriteRecipe = {
            deleteFavourite(it)
            updateUI()
        }, onCommentRecipe = {
            onCommentRecipe()
        }, onShareRecipe = {
            onShareRecipe(it)
        })

        binding.rvFavourites.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = recipeAdapter
        }

        updateUI()
    }

    private fun updateUI() {
        lifecycleScope.launch {
            recipes = repository.getUserFavourites(firebaseAuth?.currentUser?.uid.toString())
            recipeAdapter.updateList(recipes)
        }
    }

    private fun onCommentRecipe() {
        val comment = EditText(requireActivity())
        AlertDialog.Builder(requireActivity())
            .setTitle("Comentario")
            .setMessage("Deja un comentario para esta receta")
            .setView(comment)
            .setPositiveButton("Comentar") { _, _ ->

            }.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun onShareRecipe(id: String) {
        val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipdata = ClipData.newPlainText("url", "${Constants.BASE_URL}recipe/$id")
        clipboardManager.setPrimaryClip(clipdata)
        Toast.makeText(requireActivity(), "Link copiado en portapapeles", Toast.LENGTH_SHORT).show()
    }

    private fun favouriteRecipeClicked(recipe: FavouriteRecipeEntity) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, RecipeDetailFragment.newInstance(recipe.id.toString()))
            .addToBackStack(null)
            .commit()
    }

    private fun deleteFavourite(recipe: FavouriteRecipeEntity) {
        lifecycleScope.launch {
            repository.deleteFavouriteRecipe(recipe)
        }
        Toast.makeText(requireActivity(), "Eliminaste esta receta de tus favoritas", Toast.LENGTH_SHORT).show()
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