package com.arjun.cubewealth.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arjun.cubewealth.R

class NowPlayingMovieLoadStateAdapter :
    LoadStateAdapter<NowPlayingMovieLoadStateAdapter.NowPlayingMovieLoadStateViewHolder>() {
    class NowPlayingMovieLoadStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBarLoadState: ProgressBar =
            itemView.findViewById(R.id.progressBar_NowPlayingMoviesPager)

        fun bind(givenLoadState: LoadState) {
            progressBarLoadState.visibility =
                if (givenLoadState is LoadState.NotLoading) View.GONE else View.VISIBLE
        }
    }

    override fun onBindViewHolder(
        holder: NowPlayingMovieLoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NowPlayingMovieLoadStateViewHolder {
        return NowPlayingMovieLoadStateViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_progress_bar,
                parent,
                false
            )
        )
    }
}