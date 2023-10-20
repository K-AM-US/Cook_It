package com.kamus.cookit.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.model.UserDataEntity
import com.kamus.cookit.databinding.FragmentSignUpBinding
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: AppRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as CookItApp).repository

        binding.apply {
            btnSend.setOnClickListener {
                if(!name.text.isNullOrBlank() || !lastname.text.isNullOrBlank() || !userName.text.isNullOrBlank() || !password.text.isNullOrBlank() || !confirmPassword.text.isNullOrBlank() || !email.text.isNullOrBlank())
                    if(password.text.toString() == confirmPassword.text.toString()){
                        lifecycleScope.launch { 
                            repository.insertData(UserDataEntity(0, userName.text.toString(), password.text.toString(), name.text.toString(), lastname.text.toString(), email.text.toString(), "", ArrayList<String>()))
                        }
                        Toast.makeText(requireActivity(), "Datos almacenados", Toast.LENGTH_SHORT).show()
                    }
                    else
                        Toast.makeText(requireActivity(), "Las contraseñas no coinciden: ${password.text.toString()} y ${confirmPassword.text.toString()}", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(requireContext(), "Llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()

    }
}