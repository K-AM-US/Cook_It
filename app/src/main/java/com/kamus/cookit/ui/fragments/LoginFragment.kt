package com.kamus.cookit.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.kamus.cookit.R
import com.kamus.cookit.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            if (!validateFields()) return@setOnClickListener
            authenticateUser(email, password)
        }

        binding.btnSignUp.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SignUpFragment.newInstance())
                .addToBackStack("SignUpFragment")
                .commit()
        }

        binding.recoverPassword.setOnClickListener {
            val resetMail = EditText(requireActivity())
            resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            val passwordResetDialog = AlertDialog.Builder(requireActivity())
                .setTitle("Reestablecer contraseña")
                .setMessage("Ingresa tu correo para recibir el enlace de restablecimiento")
                .setView(resetMail)
                .setPositiveButton("Enviar") { _, _ ->
                    val mail = resetMail.text.toString()
                    if (mail.isNotEmpty()) {
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                            Toast.makeText(
                                requireActivity(),
                                "Se envió el enlace de restablecimiento",
                                Toast.LENGTH_SHORT
                            ).show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                requireActivity(),
                                "No se pudo enviar el enlace de restablecimiento: ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }.setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun validateFields(): Boolean {
        email = binding.usrEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        if (email.isEmpty()) {
            binding.usrEt.error = "Introduce un correo"
            binding.usrEt.requestFocus()
            return false
        }

        if (password.isEmpty() || password.length < 6) {
            binding.passwordEt.error = "Se requiere una contraseña de al menos 6 caracteres"
            binding.passwordEt.requestFocus()
            return false
        }

        return true
    }

    private fun errorHandle(task: Task<AuthResult>) {
        var errorCode = ""

        try {
            errorCode = (task.exception as FirebaseAuthException).errorCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        when (errorCode) {
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(
                    requireActivity(),
                    "Error: El correo electrónico no tiene un formato correcto",
                    Toast.LENGTH_SHORT
                ).show()
                binding.usrEt.error = "Error: El correo electrónico no tiene un formato correcto"
                binding.usrEt.requestFocus()
            }

            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(
                    requireActivity(),
                    "Error: La contraseña no es válida",
                    Toast.LENGTH_SHORT
                ).show()
                binding.passwordEt.error = "La contraseña no es válida"
                binding.passwordEt.requestFocus()
                binding.passwordEt.setText("")

            }

            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(
                    requireActivity(),
                    "Error: Una cuenta ya existe con el mismo correo, pero con diferentes datos de ingreso",
                    Toast.LENGTH_SHORT
                ).show()
            }

            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(
                    requireActivity(),
                    "Error: el correo electrónico ya está en uso con otra cuenta.",
                    Toast.LENGTH_LONG
                ).show()
                binding.usrEt.error =
                    ("Error: el correo electrónico ya está en uso con otra cuenta.")
                binding.usrEt.requestFocus()
            }

            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(
                    requireActivity(),
                    "Error: La sesión ha expirado. Favor de ingresar nuevamente.",
                    Toast.LENGTH_LONG
                ).show()
            }

            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(
                    requireActivity(),
                    "Error: No existe el usuario con la información proporcionada.",
                    Toast.LENGTH_LONG
                ).show()
            }

            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(
                    requireActivity(),
                    "La contraseña porporcionada es inválida",
                    Toast.LENGTH_LONG
                ).show()
                binding.passwordEt.error = "La contraseña debe de tener por lo menos 6 caracteres"
                binding.passwordEt.requestFocus()
            }

            "NO_NETWORK" -> {
                Toast.makeText(
                    requireActivity(),
                    "Red no disponible o se interrumpió la conexión",
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {
                Toast.makeText(
                    requireActivity(),
                    "Error. No se pudo autenticar exitosamente.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun authenticateUser(user: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(user, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireActivity(), "Autenticación exitosa", Toast.LENGTH_SHORT)
                    .show()

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, HomeFragment.newInstance())
                    .commit()
            } else {
                errorHandle(it)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}