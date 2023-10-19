package com.kamus.cookit.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kamus.cookit.R
import com.kamus.cookit.data.remote.model.UserDto
import com.kamus.cookit.databinding.ElementUserBinding

class UsersAdapter(
    private var users: List<UserDto>,
    private val onUserClick: (UserDto) -> Unit
): RecyclerView.Adapter<UsersAdapter.ViewHolder>(){

    class ViewHolder(private val binding: ElementUserBinding): RecyclerView.ViewHolder(binding.root) {

        val profilePhoto = binding.profilePhoto
        fun bind(userDto: UserDto, context: Context) {
            binding.apply {
                username.text = userDto.userName
                fullName.text = context.getString(R.string.fullName, userDto.firstName, userDto.lastName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ElementUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position], holder.itemView.context)
        Glide.with(holder.itemView.context)
            .load(users[position].img)
            .into(holder.profilePhoto)
        holder.itemView.setOnClickListener {
            onUserClick(users[position])
        }
    }

    fun filteredUsers(users: List<UserDto>) {
        this.users = users
        notifyDataSetChanged()
    }
}