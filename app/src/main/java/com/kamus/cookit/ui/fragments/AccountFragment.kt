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
import com.bumptech.glide.Glide
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.model.FavouriteRecipeEntity
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.data.remote.model.RecipeDto
import com.kamus.cookit.databinding.FragmentAccountBinding
import com.kamus.cookit.ui.adapters.ProfileRecipesAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/* TODO: Funcionalidad
*   1. Click en comentarios, dialog con comentarios
*   2. Click en share, copia link a la receta */

private const val USER = "user"
private const val USER_IMG = "user_img"
private const val USER_RECIPES = "user_recipes"
private const val USER_ID = "user_id"

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private var recipes: List<RecipeEntity> = emptyList()
    private lateinit var repository: AppRepository
    private lateinit var recipeAdapter: ProfileRecipesAdapter
    private var userId: String? = ""
    private var user: String? = ""
    private var userImg: String? = ""
    private var userRecipes: ArrayList<String> = ArrayList()

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

        arguments.let {
            userId = it?.getString(USER_ID)
            user = it?.getString(USER)
            userImg = it?.getString(USER_IMG)
            userRecipes = it?.getStringArrayList(USER_RECIPES)!!
            binding.apply {
                profileUsername.text = user
                if(userImg == "")
                    profilePhoto.setImageResource(R.mipmap.ic_launcher)
                else
                    Glide.with(requireContext())
                        .load(userImg)
                        .into(profilePhoto)

            }
        }

        repository = (requireActivity().application as CookItApp).repository
        recipeAdapter = ProfileRecipesAdapter(userId= userId, onClickedRecipe = {
            onClickedRecipe(it)
        },favouriteOnClick = {
            favouriteOnClick(it)
        })

        if(userId!="0")
            binding.apply {
                settingsIcon.visibility = View.GONE
                addBox.visibility = View.GONE
                favourites.visibility = View.GONE
                friends.visibility = View.GONE
            }

        /* para evitar que se muestre un perfil cuando no se ha iniciado sesión*/
        lifecycleScope.launch {
            if (userId == "0" /*&& repository.getData() == null*/)
                binding.apply {
                    settingsIcon.isClickable = false
                    addBox.isClickable = false
                    favourites.isClickable = false
                    friends.isClickable = false
                    rvRecipes.visibility = View.GONE
                }
        }


        binding.settingsIcon.setOnClickListener {
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
            if(userId?.toInt() == 0) {
                recipes = repository.getRecipes()
            }
            else{
                val recipesTmp = ArrayList<RecipeEntity>()
                val call: Call<List<RecipeDto>> = repository.getHomeRecipes()
                call.enqueue(object: Callback<List<RecipeDto>>{
                    override fun onResponse(
                        call: Call<List<RecipeDto>>,
                        response: Response<List<RecipeDto>>
                    ) {
                        response.body()?.forEach {
                            if(userRecipes.contains(it.id))
                                recipesTmp.add(RecipeEntity(it.id.toLong(), it.title, java.util.ArrayList<String>(), ArrayList<String>(), it.img))
                            recipeAdapter.updateList(recipes)
                        }
                    }

                    override fun onFailure(call: Call<List<RecipeDto>>, t: Throwable) {
                        Log.d("error", "ERROR RECIBIENDO RECETAS DE USUARIO")
                    }
                })
                recipes = recipesTmp
            }

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
        fun newInstance(userId: String, user: String, userImg: String, userRecipes: ArrayList<String>) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, userId)
                    putString(USER, user)
                    putString(USER_IMG, userImg)
                    putStringArrayList(USER_RECIPES, userRecipes)
                }
            }
    }
}