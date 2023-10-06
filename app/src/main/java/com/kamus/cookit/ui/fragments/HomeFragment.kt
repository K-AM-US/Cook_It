package com.kamus.cookit.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.remote.model.RecipeDto
import com.kamus.cookit.databinding.FragmentHomeBinding
import com.kamus.cookit.ui.adapters.HomeRecipesVerticalAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(R.layout.fragment_home) {

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
                .replace(R.id.fragmentContainer, NewRecipeFragment.newInstance())
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
                                HomeRecipesVerticalAdapter(it)
                            }
                            set3DItem(true)
                            setAlpha(true)
                            setInfinite(true)
                        }
                    }
                }

                override fun onFailure(call: Call<List<RecipeDto>>, t: Throwable) {
                    TODO("Not yet implemented")
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
                            adapter = HomeRecipesVerticalAdapter(recipes)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}