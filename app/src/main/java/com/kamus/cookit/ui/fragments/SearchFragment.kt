package com.kamus.cookit.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.kamus.cookit.R
import com.kamus.cookit.databinding.FragmentSearchBinding

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)


        binding.searchFood.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SearchFoodFragment.newInstance())
                .addToBackStack("SearchFoodFragment")
                .commit()
        }

        binding.searchFriends.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SearchPeopleFragment.newInstance())
                .addToBackStack("SearchPeopleFragment")
                .commit()
        }
    }



    companion object {
        @JvmStatic fun newInstance() = SearchFragment()
    }
}