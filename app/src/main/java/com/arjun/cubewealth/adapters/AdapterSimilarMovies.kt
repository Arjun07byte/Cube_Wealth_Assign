package com.arjun.cubewealth.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arjun.cubewealth.R
import com.arjun.cubewealth.utills.MovieDBPathToImageLink
import com.bumptech.glide.Glide

class AdapterSimilarMovies :
    RecyclerView.Adapter<AdapterSimilarMovies.ViewHolderSimilarMovies>() {

    inner class ViewHolderSimilarMovies(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView =
            itemView.findViewById(R.id.imageView_similarMoviesLayout)
    }

    private val diffUtilCallback =
        object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean {
                return oldItem == newItem
            }
        }

    val differList = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderSimilarMovies {
        return ViewHolderSimilarMovies(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_similar_movies_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolderSimilarMovies, position: Int) {
        val posData = differList.currentList[position]

        Glide.with(holder.imageView)
            .load(MovieDBPathToImageLink.convertPathToImage((posData)))
            .into(holder.imageView)
    }
}