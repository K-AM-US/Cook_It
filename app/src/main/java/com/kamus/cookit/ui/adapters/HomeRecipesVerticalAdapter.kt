package com.kamus.cookit.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.data.remote.model.RecipeDto
import com.kamus.cookit.databinding.ElementRecipeBinding

class HomeRecipesVerticalAdapter(
    private var recipes: List<RecipeDto>,
    private val onClickRecipe: (RecipeDto) -> Unit
): RecyclerView.Adapter<HomeRecipesVerticalAdapter.ViewHolder>(
) {
    class ViewHolder(private val binding: ElementRecipeBinding): RecyclerView.ViewHolder(binding.root) {

        val recipeImage = binding.recipeImage
        val favouriteButton = binding.btnFavourite
        val commentButton = binding.btnComment
        val recipeTitle = binding.cardRecipeTitle
        fun bind(recipeDto: RecipeDto) {
            binding.apply {
                cardRecipeTitle.text = recipeDto.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ElementRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun getItemCount(): Int = recipes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipes[position])

        Glide.with(holder.itemView.context)
            .load(recipes[position].img)
            .into(holder.recipeImage)

        holder.itemView.setOnClickListener {
            onClickRecipe(recipes[position])
        }
        holder.recipeTitle.setOnClickListener { 
            onClickRecipe(recipes[position])
        }
        holder.favouriteButton.setOnClickListener {
            Log.d("CLICK", "click en receta: ${recipes[position].title} es favorita")
        }
        holder.commentButton.setOnClickListener {
            Log.d("CLICK", "click en receta: ${recipes[position].title} se comenta")
        }
    }

    fun filteredRecipes(recipes: List<RecipeDto>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }
}