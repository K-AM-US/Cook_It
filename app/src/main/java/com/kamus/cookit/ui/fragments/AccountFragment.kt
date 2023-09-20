package com.kamus.cookit.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamus.cookit.R
import com.kamus.cookit.databinding.FragmentAccountBinding
import com.kamus.cookit.databinding.FragmentHomeBinding

class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var binding: FragmentAccountBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountBinding.bind(view)

        binding.settinggIcon.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AccountSettingsFragment.newInstance())
                .addToBackStack("AccountSettingsFragment")
                .commit()
        }

        binding.addBox.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, NewRecipeFragment.newInstance())
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
    }

    companion object {

        @JvmStatic
        fun newInstance() = AccountFragment()
    }
}