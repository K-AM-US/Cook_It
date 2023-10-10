package com.kamus.cookit.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.remote.model.UserDto
import com.kamus.cookit.databinding.FragmentSearchBinding
import com.kamus.cookit.databinding.FragmentSearchPeopleBinding
import com.kamus.cookit.ui.adapters.UsersAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPeopleFragment : Fragment() {

    private var _binding: FragmentSearchPeopleBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: AppRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as CookItApp).repository

        lifecycleScope.launch {
            val call: Call<List<UserDto>> = repository.getUsers()
            call.enqueue(object: Callback<List<UserDto>>{
                override fun onResponse(
                    call: Call<List<UserDto>>,
                    response: Response<List<UserDto>>
                ) {
                    response.body()?.let { users ->
                        binding.rvUsers.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = UsersAdapter(users)
                        }
                    }
                }

                override fun onFailure(call: Call<List<UserDto>>, t: Throwable) {
                    Log.d("LOGS", "error en los usuarios")
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchPeopleFragment()
    }
}