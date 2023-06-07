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
    // Viewholder will have the reference to all the variables inside a single
    // recyclerview item
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

    // Diff util library class is being used to update the
    // data inside the recyclerview async.
    private val diffUtilCallback = object : DiffUtil.ItemCallback<ItemEachBookmarkMovie>() {
        // Callback function of diff util defines how each RV item is being
        // differentiated from the other using these two callbacks
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

    // Inflating the respective recycler view item layout
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
            // Loading and displaying the movie Backdrop image using the
            // Glide Library
            Glide.with(imageViewEachBookmarkMovie)
                .load(MovieDBPathToImageLink.convertPathToImage(posData.backdropPicPath))
                .placeholder(R.drawable.ic_progress)
                .into(imageViewEachBookmarkMovie)

            tvTitleBookEachBookmarkMovie.text = posData.movie_title
            tvReleaseDateBookEachBookmarkMovie.text =
                this.itemView.context.getString(R.string.txt_release_date, posData.release_date)

            // Respective book and bookmark button are being updated to
            // handle operations using a Class Obj reference from the
            // fragment/Activity class
            buttonRemoveBookmarkEachBookmarkMovie.setOnClickListener {
                bookmarkFragmentRVClickListener.removeBookmarkedMovie(posData)
            }

            buttonBookEachBookmarkMovie.setOnClickListener {
                bookmarkFragmentRVClickListener.moveToMovieDetailsActivity(posData.movie_id, posData.movie_title,true)
            }
        }
    }
}