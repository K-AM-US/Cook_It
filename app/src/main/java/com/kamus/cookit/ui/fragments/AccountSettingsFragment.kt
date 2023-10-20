package com.kamus.cookit.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.model.UserDataEntity
import com.kamus.cookit.databinding.FragmentAccountSettingsBinding
import kotlinx.coroutines.launch

class AccountSettingsFragment : Fragment() {

    private var _binding: FragmentAccountSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: AppRepository
    private lateinit var userData: UserDataEntity

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



        binding.apply {

            lifecycleScope.launch {
                userData = repository.getData()
            }

            settingsName.setText(userData.username)
            settingsEmail.setText(userData.email)
            settingsPhone.setText((userData.phone))

            updateButton.setOnClickListener {
                lifecycleScope.launch{
                    if(settingsName.text.toString() != userData.username)
                        repository.updateUsername(settingsName.text.toString())
                    if(settingsPassword.text.toString() != userData.password)
                        repository.updatePassword(settingsPassword.text.toString())
                    if(settingsEmail.text.toString() != userData.email)
                        repository.updateEmail(settingsEmail.text.toString())
                    if(settingsPhone.text.toString() != userData.phone)
                        repository.updatePhone(settingsName.text.toString())
                }

            }
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