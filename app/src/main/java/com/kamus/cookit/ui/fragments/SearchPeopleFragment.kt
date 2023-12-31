package com.kamus.cookit.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.remote.model.UserDto
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
    private var peopleListTemp: List<UserDto> = emptyList()
    private lateinit var adapter: UsersAdapter
    private lateinit var linearLayoutM: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPeopleBinding.inflate(inflater, container, false)
        linearLayoutM = LinearLayoutManager(requireActivity())
        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            connectionErrorButton.setOnClickListener {
                load()
            }
            connectionErrorButton.performClick()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initRecyclerView() {
        adapter = UsersAdapter(peopleListTemp) {
            onUserClick(it)
        }
        binding.rvUsers.layoutManager = linearLayoutM
        binding.rvUsers.adapter = adapter
    }

    private fun onUserClick(user: UserDto) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                AccountFragment.newInstance(
                    user.id,
                    user.userName,
                    user.firstName + " " + user.lastName,
                    user.img.trim(),
                    user.recipes
                )
            )
            .addToBackStack(null)
            .commit()
    }

    private fun load() {

        binding.connectionErrorButton.visibility = View.GONE
        binding.connectionErrorMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        repository = (requireActivity().application as CookItApp).repository
        lifecycleScope.launch {
            val call: Call<List<UserDto>> = repository.getUsers()
            call.enqueue(object : Callback<List<UserDto>> {
                override fun onResponse(
                    call: Call<List<UserDto>>,
                    response: Response<List<UserDto>>
                ) {
                    binding.progressBar.visibility = View.GONE
                    response.body()?.let { users ->
                        peopleListTemp = users
                        adapter.filteredUsers(peopleListTemp)
                        Log.d("USERRECIPES", "users: $users")
                    }
                }

                override fun onFailure(call: Call<List<UserDto>>, t: Throwable) {
                    binding.connectionErrorButton.visibility = View.VISIBLE
                    binding.connectionErrorMessage.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    binding.connectionErrorButton.setOnClickListener {
                        load()
                    }
                }
            })
        }

        binding.searchBox.addTextChangedListener { userFilter ->
            val filteredUsers = peopleListTemp.filter { user ->
                user.userName.contains(userFilter.toString().trim(), true) ||
                        user.firstName.contains(userFilter.toString().trim(), true) ||
                        user.lastName.contains(userFilter.toString().trim(), true)
            }
            adapter.filteredUsers(filteredUsers)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchPeopleFragment()
    }
}