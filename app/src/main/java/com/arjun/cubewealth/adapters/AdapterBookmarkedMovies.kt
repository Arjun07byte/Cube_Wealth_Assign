package com.arjun.cubewealth.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arjun.cubewealth.R
import com.arjun.cubewealth.dataModels.ItemEachBookmarkMovie
import com.arjun.cubewealth.fragments.BookmarksFragment
import com.arjun.cubewealth.utills.MovieDBPathToImageLink
import com.bumptech.glide.Glide

class AdapterBookmarkedMovies(private val bookmarkFragmentRVClickListener: BookmarksFragment.BookmarkFragmentRVClickListener) :
    RecyclerView.Adapter<AdapterBookmarkedMovies.ViewHolderNowPlayingMovies>() {
    inner class ViewHolderNowPlayingMovies(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewEachBookmarkMovie: ImageView =
            itemView.findViewById(R.id.imageView_eachBookmarkItem)
        val buttonBookEachBookmarkMovie: Button =
            itemView.findViewById(R.id.button_book_eachBookmarkItem)
        val buttonRemoveBookmarkEachBookmarkMovie: ImageButton =
            itemView.findViewById(R.id.button_remove_bookMark_eachBookmarkMovie)
        val tvTitleBookEachBookmarkMovie: TextView =
            itemView.findViewById(R.id.textView_title_eachBookmarkMovie)
        val tvReleaseDateBookEachBookmarkMovie: TextView =
            itemView.findViewById(R.id.textView_rDate_eachBookmarkMovie)
    }

    val diffUtilCallback = object : DiffUtil.ItemCallback<ItemEachBookmarkMovie>() {
        override fun areItemsTheSame(
            oldItem: ItemEachBookmarkMovie,
            newItem: ItemEachBookmarkMovie
        ): Boolean {
            return oldItem.movie_id == newItem.movie_id
        }

        override fun areContentsTheSame(
            oldItem: ItemEachBookmarkMovie,
            newItem: ItemEachBookmarkMovie
        ): Boolean {
            return oldItem.movie_title == newItem.movie_title
        }
    }

    val differList = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNowPlayingMovies {
        return ViewHolderNowPlayingMovies(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_bookmarks_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolderNowPlayingMovies, position: Int) {
        val posData = differList.currentList[position]

        holder.apply {
            Glide.with(imageViewEachBookmarkMovie)
                .load(MovieDBPathToImageLink.convertPathToImage(posData.backdropPicPath))
                .into(imageViewEachBookmarkMovie)

            tvTitleBookEachBookmarkMovie.text = posData.movie_title
            tvReleaseDateBookEachBookmarkMovie.text =
                this.itemView.context.getString(R.string.txt_release_date, posData.release_date)

            buttonRemoveBookmarkEachBookmarkMovie.setOnClickListener {
                bookmarkFragmentRVClickListener.removeBookmarkedMovie(posData)
            }
        }
    }
}