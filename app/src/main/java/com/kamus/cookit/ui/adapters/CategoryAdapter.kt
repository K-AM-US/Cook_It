package com.kamus.cookit.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kamus.cookit.R
import com.kamus.cookit.data.remote.model.CategoriesDto
import com.kamus.cookit.databinding.ElementCategoryBinding

class CategoryAdapter(
    private val categories: List<CategoriesDto>
): RecyclerView.Adapter<CategoryAdapter.ViewHolder>(

) {
    class ViewHolder(private val binding: ElementCategoryBinding): RecyclerView.ViewHolder(binding.root) {

        val categoryIcon = binding.categoryImage

        fun bind(category: CategoriesDto) {
            binding.category.text = category.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ElementCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = categories.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])

        Glide.with(holder.itemView.context)
            .load(categories[position].icon)
            .into(holder.categoryIcon)
    }
}