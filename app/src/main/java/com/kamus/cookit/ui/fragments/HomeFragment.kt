package com.kamus.cookit.ui.fragments

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
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
import com.kamus.cookit.data.remote.model.RecipeDetailDto
import com.kamus.cookit.data.remote.model.RecipeDto
import com.kamus.cookit.databinding.FragmentHomeBinding
import com.kamus.cookit.ui.adapters.HomeRecipesVerticalAdapter
import com.kamus.cookit.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: AppRepository
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            connectionErrorButton.setOnClickListener {
                load()
            }
            connectionErrorButton.performClick()
        }

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth?.currentUser?.uid == null) {
            binding.loginIcon.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LoginFragment.newInstance())
                    .addToBackStack("LoginFragment")
                    .commit()
            }
            binding.btnAddRecipe.visibility = View.GONE
        } else {
            binding.loginIcon.visibility = View.GONE
            binding.btnAddRecipe.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragmentContainer,
                        NewRecipeFragment.newInstance(
                            recipeId = "",
                            recipeTitle = "",
                            recipeIngredients = ArrayList<String>(),
                            recipeProcess = ArrayList<String>(),
                            newRecipe = true
                        )
                    )
                    .addToBackStack("NewRecipeFragment")
                    .commit()
            }
        }
    }

    private fun onClickedRecipe(recipe: RecipeDto) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, RecipeDetailFragment.newInstance(recipe.id))
            .addToBackStack(null)
            .commit()
    }

    private fun favouriteOnClick(recipe: RecipeDto) {
        if (firebaseAuth?.uid == null) {
            Toast.makeText(
                requireActivity(),
                "Inicia sesión para agregar esta receta a tus favoritas",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            lifecycleScope.launch {
                val call: Call<RecipeDetailDto> = repository.getRecipeDetail(recipe.id)
                call.enqueue(object : Callback<RecipeDetailDto> {
                    override fun onResponse(
                        call: Call<RecipeDetailDto>,
                        response: Response<RecipeDetailDto>
                    ) {
                        lifecycleScope.launch {
                            binding.progress.visibility = View.GONE
                            response.body()?.let {
                                FavouriteRecipeEntity(
                                    recipe.id.toLong(),
                                    firebaseAuth?.currentUser?.uid.toString(),
                                    response.body()!!.title,
                                    response.body()!!.ingredients,
                                    it.process
                                )
                            }
                                ?.let {
                                    lifecycleScope.launch {
                                        if (repository.getFavouriteRecipeById(recipe.id) == null) {
                                            repository.insertFavouriteRecipe(it)
                                            Toast.makeText(
                                                requireActivity(),
                                                "Agregaste esta receta a tus favoritas",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            repository.deleteFavouriteRecipe(it)
                                            Toast.makeText(requireActivity(), "Eliminaste esta receta de tus favoritas", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                        }
                    }

                    override fun onFailure(call: Call<RecipeDetailDto>, t: Throwable) {
                        Toast.makeText(
                            requireActivity(),
                            "Error de conexión, intenta más tarde",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progress.visibility = View.GONE
                    }
                })
            }
        }
    }

    private fun load() {
        binding.connectionErrorMessage.visibility = View.GONE
        binding.connectionErrorButton.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE
        repository = (requireActivity().application as CookItApp).repository

        lifecycleScope.launch {
            val call: Call<List<RecipeDto>> = repository.getHomeRecipes()
            call.enqueue(object : Callback<List<RecipeDto>> {
                override fun onResponse(
                    call: Call<List<RecipeDto>>,
                    response: Response<List<RecipeDto>>
                ) {
                    binding.progress.visibility = View.GONE
                    response.body()?.reversed().let { recipes ->
                        binding.rvCarousel.apply {
                            adapter = recipes?.let {
                                HomeRecipesVerticalAdapter(it, onClickRecipe = {
                                    onClickedRecipe(it)
                                }, favouriteOnClick = {
                                    favouriteOnClick(it)
                                }, onCommentRecipe = {
                                    onCommentRecipe()
                                }, onShareRecipe = {
                                    onShareRecipe(it)
                                })
                            }
                            set3DItem(true)
                            setAlpha(true)
                            setInfinite(true)
                        }
                    }
                }

                override fun onFailure(call: Call<List<RecipeDto>>, t: Throwable) {
                    binding.connectionErrorButton.visibility = View.VISIBLE
                    binding.connectionErrorMessage.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE

                    binding.connectionErrorButton.setOnClickListener {
                        load()
                    }
                }
            })
        }

        lifecycleScope.launch {
            val call: Call<List<RecipeDto>> = repository.getHomeRecipes()
            call.enqueue(object : Callback<List<RecipeDto>> {
                override fun onResponse(
                    call: Call<List<RecipeDto>>,
                    response: Response<List<RecipeDto>>
                ) {
                    Log.d("LOGS", "recipes: ${response.body()}")
                    binding.progress.visibility = View.GONE
                    response.body()?.let { recipes ->
                        binding.rvHomeRecipes.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = HomeRecipesVerticalAdapter(recipes, onClickRecipe = {
                                onClickedRecipe(it)
                            }, favouriteOnClick = {
                                favouriteOnClick(it)
                            }, onCommentRecipe = {
                                onCommentRecipe()
                            }, onShareRecipe = {
                                onShareRecipe(it)
                            })
                            Log.d("LOGS", "Adapter: $adapter.")
                        }
                    }
                }

                override fun onFailure(call: Call<List<RecipeDto>>, t: Throwable) {
                    binding.connectionErrorButton.visibility = View.VISIBLE
                    binding.connectionErrorMessage.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE

                    binding.connectionErrorButton.setOnClickListener {
                        load()
                    }
                }
            })
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
        val clipboardManager = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipdata = ClipData.newPlainText("url", "${Constants.BASE_URL}recipe/$id")
        clipboardManager.setPrimaryClip(clipdata)
        Toast.makeText(requireActivity(), "Link copiado en portapapeles", Toast.LENGTH_SHORT).show()
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