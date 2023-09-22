package com.kamus.cookit.ui.adapters

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.databinding.ElementRecipeBinding

class ProfileRecipesAdapter(private val onClickedRecipe: (RecipeEntity) -> Unit): RecyclerView.Adapter<ProfileRecipesAdapter.ViewHolder>() {

    private var recipes: List<RecipeEntity> = emptyList()

    class ViewHolder(private val binding: ElementRecipeBinding): RecyclerView.ViewHolder(binding.root) {

        val star = binding.btnFavourite
        val comment = binding.btnComment
        val share = binding.btnShare
        val detail = binding.recipeImage
        val title = binding.cardRecipeTitle

        fun bind(recipe: RecipeEntity){
            Log.d("LOGS", "recipe $recipe.title")
            binding.apply {
                cardRecipeTitle.text = recipe.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ElementRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = recipes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipes[position])

        holder.detail.setOnClickListener {
            onClickedRecipe(recipes[position])
        }

        holder.title.setOnClickListener {
            onClickedRecipe(recipes[position])
        }

        holder.star.setOnClickListener {
            Log.d("LOGS", "click en favorito")
        }

        holder.comment.setOnClickListener {
            Log.d("LOGS", "click en comment")
        }

        holder.share.setOnClickListener {
            Log.d("LOGS", "click en share")
        }


    }

    fun updateList(list: List<RecipeEntity>){
        recipes = list
        notifyDataSetChanged()
    }
}