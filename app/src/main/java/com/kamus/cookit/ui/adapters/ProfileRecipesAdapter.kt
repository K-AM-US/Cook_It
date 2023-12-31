package com.kamus.cookit.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kamus.cookit.R
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.databinding.ElementRecipeBinding

class ProfileRecipesAdapter(
    private val onClickedRecipe: (RecipeEntity) -> Unit,
    private val favouriteOnClick: (RecipeEntity) -> Unit,
    private val userId: String?,
    private val onCommentRecipe: () -> Unit,
    private val onShareRecipe: (String) -> Unit
) : RecyclerView.Adapter<ProfileRecipesAdapter.ViewHolder>() {

    private var recipes: List<RecipeEntity> = emptyList()

    class ViewHolder(private val binding: ElementRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val star = binding.btnFavourite
        val comment = binding.btnComment
        val share = binding.btnShare
        val title = binding.cardRecipeTitle
        val img = binding.recipeImage

        fun bind(recipe: RecipeEntity) {
            binding.apply {
                cardRecipeTitle.text = recipe.title
                img.setImageResource(R.mipmap.ic_cookit_logo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ElementRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = recipes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(recipes[position])

        holder.itemView.setOnClickListener {
            onClickedRecipe(recipes[position])
        }

        holder.title.setOnClickListener {
            onClickedRecipe(recipes[position])
        }

        holder.star.setOnClickListener {
            favouriteOnClick(recipes[position])
        }

        holder.comment.setOnClickListener {
            onCommentRecipe()
        }

        holder.share.setOnClickListener {
            onShareRecipe(recipes[position].id.toString())
        }

        if (userId != "0")
            Glide.with(holder.itemView.context)
                .load(recipes[position].img)
                .into(holder.img)
    }

    fun updateList(list: List<RecipeEntity>) {
        recipes = list
        notifyDataSetChanged()
    }
}