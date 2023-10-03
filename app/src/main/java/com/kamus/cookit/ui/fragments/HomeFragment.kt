package com.kamus.cookit.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.remote.RetrofitHelper
import com.kamus.cookit.data.remote.model.CategoriesDto
import com.kamus.cookit.data.remote.model.RecipeDto
import com.kamus.cookit.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: AppRepository


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.inflate(requireActivity().layoutInflater)
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