package com.kamus.cookit.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kamus.cookit.data.remote.model.UserDto
import com.kamus.cookit.databinding.ElementUserBinding
import com.kamus.cookit.databinding.FragmentAccountBinding

class UsersAdapter(
    private val users: List<UserDto>
): RecyclerView.Adapter<UsersAdapter.ViewHolder>(){

    class ViewHolder(private val binding: ElementUserBinding): RecyclerView.ViewHolder(binding.root) {

        val profilePhoto = binding.profilePhoto
        fun bind(userDto: UserDto) {
            binding.apply {
                username.text = userDto.userName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ElementUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
        Glide.with(holder.itemView.context)
            .load(users[position].img)
            .into(holder.profilePhoto)
    }
}