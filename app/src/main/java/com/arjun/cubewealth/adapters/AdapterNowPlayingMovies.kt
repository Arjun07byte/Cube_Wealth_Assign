package com.arjun.cubewealth.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arjun.cubewealth.R

class AdapterNowPlayingMovies :
    RecyclerView.Adapter<AdapterNowPlayingMovies.ViewHolderNowPlayingMovies>() {
    inner class ViewHolderNowPlayingMovies(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    private var currListSize = 10

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
        return currListSize
    }

    fun getNewItems() {
        if(currListSize >= 50) return

        currListSize += 10; notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderNowPlayingMovies, position: Int) {

    }
}