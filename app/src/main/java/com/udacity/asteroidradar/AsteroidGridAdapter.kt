package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

/*
    Add an OnClickListener parameter to the PhotoGridAdapter class declaration
*/
class AsteroidGridAdapter(val onClickListener: josephOnClickListener) : ListAdapter<Asteroid, AsteroidGridAdapter.AsteroidViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id === newItem.id
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AsteroidGridAdapter.AsteroidViewHolder {
        return AsteroidViewHolder(AsteroidItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: AsteroidGridAdapter.AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        // set up onClickListener() to pass asteroid on button click:
        holder.itemView.setOnClickListener { onClickListener.onClick(asteroid) }
        holder.bind(asteroid)
    }

    /*an OnClickListener class with a lambda in its constructor that initializes a matching onClick
         function*/
    class josephOnClickListener (val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(oneAsteroid: Asteroid) = clickListener(oneAsteroid)
    }


    class AsteroidViewHolder(private var binding: AsteroidItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.oneasteroid = asteroid
            binding.executePendingBindings()
        }

    }

}


