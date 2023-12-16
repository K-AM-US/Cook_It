package com.kamus.cookit.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kamus.cookit.R
import com.kamus.cookit.application.CookItApp
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.model.FavouriteRecipeEntity
import com.kamus.cookit.data.db.model.FriendsEntity
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.data.db.model.UserDataEntity
import com.kamus.cookit.data.remote.model.RecipeDto
import com.kamus.cookit.databinding.FragmentAccountBinding
import com.kamus.cookit.ui.adapters.ProfileRecipesAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val USER = "user"
private const val USER_IMG = "user_img"
private const val USER_RECIPES = "user_recipes"
private const val USER_ID = "user_id"
private const val USER_FULLNAME = "user_fullname"

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private var recipes: List<RecipeEntity> = emptyList()
    private lateinit var repository: AppRepository
    private lateinit var recipeAdapter: ProfileRecipesAdapter
    private var userId: String? = ""
    private var user: String? = ""
    private var userFullname: String? = ""
    private var userImg: String? = ""
    private var userRecipes: ArrayList<String> = ArrayList()
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        repository = (requireActivity().application as CookItApp).repository
        recipeAdapter = ProfileRecipesAdapter(userId = userId, onClickedRecipe = {
            onClickedRecipe(it)
        }, favouriteOnClick = {
            favouriteOnClick(it)
        })

        arguments.let {
            userId = it?.getString(USER_ID)
            user = it?.getString(USER)
            userFullname = it?.getString(USER_FULLNAME)
            userImg = it?.getString(USER_IMG)
            userRecipes = it?.getStringArrayList(USER_RECIPES)!!
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            firebaseAuth = FirebaseAuth.getInstance()
            binding.connectionErrorButton.visibility = View.GONE
            binding.connectionErrorMessage.visibility = View.GONE
            if (firebaseAuth?.uid == null && userId == "0") {
                binding.apply {
                    profilePhoto.visibility = View.GONE
                    settingsIcon.visibility = View.GONE
                    profileUsername.visibility = View.GONE
                    profileName.visibility = View.GONE
                    addFriend.visibility = View.GONE
                    removeFriend.visibility = View.GONE
                    addBox.visibility = View.GONE
                    friends.visibility = View.GONE
                    favourites.visibility = View.GONE

                    rvRecipes.visibility = View.GONE

                    loginBtn.setOnClickListener {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, LoginFragment.newInstance())
                            .commit()
                    }
                }
            } else {
                binding.loginBtn.visibility = View.GONE
                binding.loginIcon.visibility = View.GONE
                binding.loginMessage.visibility = View.GONE
                if (userId != "0") {
                    settingsIcon.visibility = View.GONE
                    addBox.visibility = View.GONE
                    favourites.visibility = View.GONE
                    friends.visibility = View.GONE

                    profileUsername.text = user
                    profileName.text = userFullname
                    Glide.with(requireContext())
                        .load(userImg)
                        .into(profilePhoto)

                    lifecycleScope.launch {
                        if (repository.getUserFriend(
                                userId!!,
                                firebaseAuth?.currentUser?.uid.toString()
                            ) == null
                        ) {
                            addFriend.isClickable = true
                            removeFriend.isClickable = false
                        } else {
                            addFriend.isClickable = false
                            removeFriend.isClickable = true
                        }
                    }
                } else {
                    addFriend.visibility = View.GONE
                    removeFriend.visibility = View.GONE
                    lifecycleScope.launch {
                        val usertmp = repository.getUser(firebaseAuth?.currentUser?.uid.toString())

                        binding.profileUsername.text = usertmp.username
                        binding.profileName.text = usertmp.fullname
                    }
                    profilePhoto.setImageResource(R.mipmap.ic_launcher)
                }
            }

            binding.settingsIcon.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, AccountSettingsFragment.newInstance())
                    .addToBackStack("AccountSettingsFragment")
                    .commit()
            }
            binding.addBox.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragmentContainer,
                        NewRecipeFragment.newInstance(
                            recipeId = "",
                            recipeTitle = "",
                            recipeIngredients = ArrayList<String>(),
                            recipeProcess = ArrayList<String>(),
                            newRecipe = true
                        )
                    )
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

            binding.rvRecipes.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = recipeAdapter
            }

            binding.apply {
                if (firebaseAuth?.uid == null) {
                    addFriend.visibility = View.GONE
                    removeFriend.visibility = View.GONE
                } else {
                    addFriend.setOnClickListener {
                        addFriend.isClickable = false
                        removeFriend.isClickable = true
                        lifecycleScope.launch {
                            userId?.let {
                                if (repository.getUserFriend(
                                        it,
                                        firebaseAuth?.currentUser?.uid.toString()
                                    ) == null
                                ) {
                                    repository.insertFriend(
                                        FriendsEntity(
                                            it,
                                            firebaseAuth?.currentUser?.uid.toString()
                                        )
                                    )
                                    Toast.makeText(
                                        requireActivity(),
                                        "Amigo agregado",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else
                                    Toast.makeText(
                                        requireContext(),
                                        "Usuario ya es un amigo",
                                        Toast.LENGTH_SHORT
                                    ).show()
                            }
                        }
                    }

                    removeFriend.setOnClickListener {
                        addFriend.isClickable = true
                        removeFriend.isClickable = false
                        lifecycleScope.launch {
                            userId?.let {
                                if (repository.getUserFriend(
                                        it,
                                        firebaseAuth?.currentUser?.uid.toString()
                                    ) != null
                                ){
                                    repository.deleteFriend(
                                        FriendsEntity(
                                            it,
                                            firebaseAuth?.currentUser?.uid.toString()
                                        )
                                    )
                                    Toast.makeText(requireActivity(), "Amigo eliminado", Toast.LENGTH_SHORT)
                                        .show()
                                } else
                                    Toast.makeText(
                                        requireContext(),
                                        "Usuario ya se borró",
                                        Toast.LENGTH_SHORT
                                    ).show()
                            }
                        }
                    }
                }
            }
            updateUI()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateUI() {
        lifecycleScope.launch {
            if (userId?.toInt() == 0) {
                recipes = repository.getUserRecipes(firebaseAuth?.currentUser?.uid.toString())
                if (recipes.isNotEmpty()){
                    binding.noRecipesMessage.visibility = View.GONE
                    binding.rvRecipes.visibility = View.VISIBLE
                } else {
                    binding.noRecipesMessage.visibility = View.VISIBLE
                    binding.rvRecipes.visibility = View.GONE
                }
                if(firebaseAuth?.currentUser?.uid == null)
                    binding.noRecipesMessage.visibility = View.GONE
            } else {
                binding.apply {
                    connectionErrorButton.setOnClickListener {
                        load()
                    }
                    connectionErrorButton.performClick()
                }
            }
            recipeAdapter.updateList(recipes)
        }
    }

    private fun onClickedRecipe(recipe: RecipeEntity) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, RecipeDetailFragment.newInstance(recipe.id.toString()))
            .addToBackStack(null)
            .commit()
    }

    private fun favouriteOnClick(recipe: RecipeEntity) {
        if (firebaseAuth?.uid == null) {
            Toast.makeText(
                requireActivity(),
                "Inicia sesión para agregar esta receta a tus favoritas",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            lifecycleScope.launch {
                if (repository.getFavouriteRecipeById(recipe.id.toString()) == null) {
                    repository.insertFavouriteRecipe(
                        FavouriteRecipeEntity(
                            recipe.id,
                            firebaseAuth?.currentUser?.uid.toString(),
                            recipe.title,
                            recipe.ingredients,
                            recipe.process
                        )
                    )
                    Toast.makeText(
                        requireActivity(),
                        "Receta agregada a favoritas",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Receta ya es favorita",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun load() {
        binding.connectionErrorButton.visibility = View.GONE
        binding.connectionErrorMessage.visibility = View.GONE
        val recipesTmp = ArrayList<RecipeEntity>()
        val call: Call<List<RecipeDto>> = repository.getHomeRecipes()
        call.enqueue(object : Callback<List<RecipeDto>> {
            override fun onResponse(
                call: Call<List<RecipeDto>>,
                response: Response<List<RecipeDto>>
            ) {
                response.body()?.forEach {
                    if (userRecipes.contains(it.id))
                        recipesTmp.add(
                            RecipeEntity(
                                it.id.toLong(),
                                firebaseAuth?.currentUser?.uid.toString(),
                                it.title,
                                java.util.ArrayList<String>(),
                                ArrayList<String>(),
                                it.img
                            )
                        )
                    recipeAdapter.updateList(recipes)
                }
            }

            override fun onFailure(call: Call<List<RecipeDto>>, t: Throwable) {
                binding.connectionErrorButton.visibility = View.VISIBLE
                binding.connectionErrorMessage.visibility = View.VISIBLE
                binding.connectionErrorButton.setOnClickListener {
                    load()
                }
            }
        })
        recipes = recipesTmp
    }

    companion object {
        @JvmStatic
        fun newInstance(
            userId: String,
            user: String,
            userFullname: String,
            userImg: String,
            userRecipes: ArrayList<String>
        ) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, userId)
                    putString(USER, user)
                    putString(USER_FULLNAME, userFullname)
                    putString(USER_IMG, userImg)
                    putStringArrayList(USER_RECIPES, userRecipes)
                }
            }
    }
}