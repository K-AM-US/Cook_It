package com.kamus.cookit.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.model.FriendsEntity
import com.kamus.cookit.data.remote.model.UserDto
import com.kamus.cookit.databinding.FragmentFriendsBinding
import com.kamus.cookit.ui.adapters.UsersAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsFragment: Fragment() {

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: AppRepository
    private var friendsTemp: List<UserDto> = emptyList()
    private lateinit var adapter: UsersAdapter
    private lateinit var linearLayoutM: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        linearLayoutM = LinearLayoutManager(requireActivity())
        initRecyclerView()
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
                        val friendsFilter = ArrayList<UserDto>()
                        users.forEach {
                            lifecycleScope.launch {
                                if(repository.getFriend(it.id) != null)
                                    friendsFilter.add(it)
                            }
                        }
                        friendsTemp = friendsFilter
                        adapter.filteredUsers(friendsTemp)
                    }
                }

                override fun onFailure(call: Call<List<UserDto>>, t: Throwable) {
                    Log.d("FRIENDSTEST", "error en friends")
                }
            })
        }

        binding.searchBox.addTextChangedListener { userFilter ->
            val filteredUsers = friendsTemp.filter { user ->
                user.userName.contains(userFilter.toString().trim(), false) ||
                        user.firstName.contains(userFilter.toString().trim(), true) ||
                        user.lastName.contains(userFilter.toString().trim(), true)
            }
            adapter.filteredUsers(filteredUsers)
        }
    }

    private fun onUserClick(user: UserDto) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, AccountFragment.newInstance(user.id, user.userName, user.img.trim(), user.recipes))
            .addToBackStack(null)
            .commit()
    }

    private fun initRecyclerView() {
        adapter = UsersAdapter(friendsTemp){
            onUserClick(it)
        }
        binding.rvFriends.layoutManager = linearLayoutM
        binding.rvFriends.adapter = adapter
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = FriendsFragment()
    }
}