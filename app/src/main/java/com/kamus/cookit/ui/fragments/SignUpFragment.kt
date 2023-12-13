package com.kamus.cookit.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.userProfileChangeRequest
import com.kamus.cookit.R
import com.kamus.cookit.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSend.setOnClickListener {
            if(!validateFields()) return@setOnClickListener
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener {
                        Toast.makeText(
                            requireActivity(),
                            "El correo de validación ha sido enviado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }?.addOnFailureListener {
                        Toast.makeText(
                            requireActivity(),
                            "No se pudo enviar el correo de verificación",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    Toast.makeText(requireContext(), "Usuario Creado", Toast.LENGTH_SHORT).show()

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, HomeFragment.newInstance())
                        .addToBackStack("SignUpFragment")
                        .commit()
                } else {
                    errorHandle(it)
                }
            }
        }
    }

    private fun validateFields(): Boolean {
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()

        if(email.isEmpty()) {
            binding.email.error = "Introduce un correo"
            binding.email.requestFocus()
            return false
        }

        if(password.isEmpty() || password.length < 6) {
            binding.password.error = "Se requiere una contraseña de al menos 6 caracteres"
            binding.password.requestFocus()
            return false
        }

        return true
    }

    private fun errorHandle(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            e.printStackTrace()
        }

        when(errorCode){
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(requireActivity(), "Error: El correo electrónico no tiene un formato correcto", Toast.LENGTH_SHORT).show()
                binding.email.error = "Error: El correo electrónico no tiene un formato correcto"
                binding.email.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(requireActivity(), "Error: La contraseña no es válida", Toast.LENGTH_SHORT).show()
                binding.password.error = "La contraseña no es válida"
                binding.password.requestFocus()
                binding.password.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(requireActivity(), "Error: Una cuenta ya existe con el mismo correo, pero con diferentes datos de ingreso", Toast.LENGTH_SHORT).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(requireActivity(), "Error: el correo electrónico ya está en uso con otra cuenta.", Toast.LENGTH_LONG).show()
                binding.email.error = ("Error: el correo electrónico ya está en uso con otra cuenta.")
                binding.email.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(requireActivity(), "Error: La sesión ha expirado. Favor de ingresar nuevamente.", Toast.LENGTH_LONG).show()
            }
            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(requireActivity(), "Error: No existe el usuario con la información proporcionada.", Toast.LENGTH_LONG).show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(requireActivity(), "La contraseña porporcionada es inválida", Toast.LENGTH_LONG).show()
                binding.password.error = "La contraseña debe de tener por lo menos 6 caracteres"
                binding.password.requestFocus()
            }
            "NO_NETWORK" -> {
                Toast.makeText(requireActivity(), "Red no disponible o se interrumpió la conexión", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(requireActivity(), "Error. No se pudo autenticar exitosamente.", Toast.LENGTH_SHORT).show()
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