package com.arjun.cubewealth.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arjun.cubewealth.R
import com.arjun.cubewealth.dataModels.ItemMovieReviewDisplay

class AdapterMovieReviews :
    RecyclerView.Adapter<AdapterMovieReviews.ViewHolderMovieReviews>() {
    inner class ViewHolderMovieReviews(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewAuthorName: TextView =
            itemView.findViewById(R.id.textView_authorName_reviewItem)
        val textViewContent: TextView =
            itemView.findViewById(R.id.textView_content_reviewItem)
    }

    private val diffUtilCallback =
        object : DiffUtil.ItemCallback<ItemMovieReviewDisplay>() {
            override fun areItemsTheSame(
                oldItem: ItemMovieReviewDisplay,
                newItem: ItemMovieReviewDisplay
            ): Boolean {
                return oldItem.authorName == newItem.authorName
            }

            override fun areContentsTheSame(
                oldItem: ItemMovieReviewDisplay,
                newItem: ItemMovieReviewDisplay
            ): Boolean {
                return oldItem.reviewContent == newItem.reviewContent
            }
        }

    val differList = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderMovieReviews {
        return ViewHolderMovieReviews(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_reviews_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolderMovieReviews, position: Int) {
        val posData = differList.currentList[position]

        holder.textViewAuthorName.text = posData.authorName
        holder.textViewContent.text = posData.reviewContent
    }
}