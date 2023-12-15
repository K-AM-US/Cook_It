package com.kamus.cookit.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.databinding.FragmentAccountSettingsBinding
import kotlinx.coroutines.launch

class AccountSettingsFragment : Fragment() {

    private var _binding: FragmentAccountSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: AppRepository
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = (requireActivity().application as CookItApp).repository
        firebaseAuth = FirebaseAuth.getInstance()

        binding.updateButton.setOnClickListener {
            lifecycleScope.launch {
                val tmp = repository.getUser(firebaseAuth?.currentUser?.uid.toString())
                if(binding.settingsName.text.toString() != "") {
                    tmp.fullname = binding.settingsName.text.toString()
                    binding.settingsName.setText("")
                    binding.settingsName.clearFocus()
                }
                if(binding.settingsUsername.text.toString() != ""){
                    tmp.username = binding.settingsUsername.text.toString()
                    binding.settingsUsername.setText("")
                    binding.settingsUsername.clearFocus()
                }
                if(binding.settingsPassword.text.toString() != "") {
                    firebaseAuth?.currentUser?.updatePassword(binding.settingsPassword.text.toString())
                    binding.settingsPassword.setText("")
                    binding.settingsPassword.clearFocus()
                }
                Toast.makeText(requireActivity(), "Datos actualizados", Toast.LENGTH_SHORT).show()
                repository.updateUser(tmp)
            }
        }

        binding.logoutBtn.setOnClickListener {
            firebaseAuth?.signOut()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment.newInstance())
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = AccountSettingsFragment()
    }
}