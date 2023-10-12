package com.kamus.cookit.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kamus.cookit.data.db.model.FavouriteRecipeEntity
import com.kamus.cookit.databinding.ElementRecipeBinding

class FavouriteRecipesAdapter(

): RecyclerView.Adapter<FavouriteRecipesAdapter.ViewHolder>() {

    private var recipes: List<FavouriteRecipeEntity> = emptyList()

    class ViewHolder(private val binding: ElementRecipeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: FavouriteRecipeEntity) {
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
    }

    fun updateList(list: List<FavouriteRecipeEntity>){
        recipes = list
        notifyDataSetChanged()
    }
}