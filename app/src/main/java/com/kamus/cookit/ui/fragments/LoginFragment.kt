package com.kamus.cookit.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.kamus.cookit.R
import com.kamus.cookit.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.btnSignUp.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SignUpFragment.newInstance())
                .addToBackStack("SignUpFragment")
                .commit()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()

    }
}