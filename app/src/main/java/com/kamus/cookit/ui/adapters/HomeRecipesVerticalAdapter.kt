package com.kamus.cookit.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kamus.cookit.data.remote.model.RecipeDto
import com.kamus.cookit.databinding.ElementRecipeBinding

class HomeRecipesVerticalAdapter(
    private val recipes: List<RecipeDto>
): RecyclerView.Adapter<HomeRecipesVerticalAdapter.ViewHolder>(
) {
    class ViewHolder(private val binding: ElementRecipeBinding): RecyclerView.ViewHolder(binding.root) {

        val recipeImage = binding.recipeImage
        fun bind(recipeDto: RecipeDto) {
            binding.apply {
                cardRecipeTitle.text = recipeDto.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ElementRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = recipes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)

        Glide.with(holder.itemView.context)
            .load(recipe.img)
            .into(holder.recipeImage)
    }
}