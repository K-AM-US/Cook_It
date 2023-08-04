package com.kamus.cookit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.kamus.cookit.R
import com.kamus.cookit.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

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
    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}