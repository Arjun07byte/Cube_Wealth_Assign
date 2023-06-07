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
import com.arjun.cubewealth.dataModels.ItemEachMovie
import com.arjun.cubewealth.fragments.ExploreFragment
import com.arjun.cubewealth.utills.MovieDBPathToImageLink
import com.bumptech.glide.Glide

class AdapterNowPlayingMovies(private val exploreFragmentRVClickListener: ExploreFragment.ExploreFragmentRVClickListener) :
    RecyclerView.Adapter<AdapterNowPlayingMovies.ViewHolderNowPlayingMovies>() {
    inner class ViewHolderNowPlayingMovies(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewEachNowPlayingMovie: ImageView =
            itemView.findViewById(R.id.imageView_eachNowPlayingItem)
        val buttonBookEachNowPlayingMovie: Button =
            itemView.findViewById(R.id.button_book_eachNowPlayingItem)
        val buttonBookmarkEachNowPlayingMovie: ImageButton =
            itemView.findViewById(R.id.button_bookMark_eachNowPlayingMovie)
        val tvTitleBookEachNowPlayingMovie: TextView =
            itemView.findViewById(R.id.textView_title_eachNowPlayingMovie)
        val tvReleaseDateBookEachNowPlayingMovie: TextView =
            itemView.findViewById(R.id.textView_rDate_eachNowPlayingMovie)
    }

    private val diffUtilCallback = object : DiffUtil.ItemCallback<ItemEachMovie>() {
        override fun areItemsTheSame(
            oldItem: ItemEachMovie,
            newItem: ItemEachMovie
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ItemEachMovie,
            newItem: ItemEachMovie
        ): Boolean {
            return oldItem.title == newItem.title
        }
    }

    val differList = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNowPlayingMovies {
        return ViewHolderNowPlayingMovies(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_now_playing_item,
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
            Glide.with(imageViewEachNowPlayingMovie)
                .load(MovieDBPathToImageLink.convertPathToImage(posData.backdrop_path))
                .placeholder(R.drawable.ic_progress)
                .into(imageViewEachNowPlayingMovie)

            tvTitleBookEachNowPlayingMovie.text = posData.title
            tvReleaseDateBookEachNowPlayingMovie.text =
                this.itemView.context.getString(R.string.txt_release_date, posData.release_date)

            // fetching whether the given movie is inside the bookmarks
            // list and handling the button clicks according to that
            var isBookmarked = exploreFragmentRVClickListener.getBookmarkedStatus(posData.id)
            if (isBookmarked) {
                buttonBookmarkEachNowPlayingMovie.setBackgroundResource(R.drawable.bg_bookmarked_button)
                buttonBookmarkEachNowPlayingMovie.setImageResource(R.drawable.ic_bookmarked)
                buttonBookmarkEachNowPlayingMovie.setOnClickListener {}
            } else {
                buttonBookmarkEachNowPlayingMovie.setOnClickListener {
                    exploreFragmentRVClickListener.bookmarkMovie(makeBookmarkItem(posData))
                    buttonBookmarkEachNowPlayingMovie.setBackgroundResource(R.drawable.bg_bookmarked_button)
                    buttonBookmarkEachNowPlayingMovie.setImageResource(R.drawable.ic_bookmarked)
                    buttonBookmarkEachNowPlayingMovie.setOnClickListener {}
                    isBookmarked = true
                }
            }

            buttonBookEachNowPlayingMovie.setOnClickListener {
                exploreFragmentRVClickListener.moveToMovieDetailsActivity(posData.id, posData.title, isBookmarked)
            }
        }
    }

    // Utility function which will be used to
    // convert the online fetched movieItem to bookmarkMovie
    // Item which takes less storage and thus better UX
    private fun makeBookmarkItem(givenMovie: ItemEachMovie): ItemEachBookmarkMovie =
        ItemEachBookmarkMovie(
            givenMovie.id, givenMovie.backdrop_path, givenMovie.title, givenMovie.release_date
        )
}