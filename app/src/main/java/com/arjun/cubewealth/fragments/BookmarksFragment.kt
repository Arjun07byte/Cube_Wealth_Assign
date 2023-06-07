package com.arjun.cubewealth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arjun.cubewealth.R
import com.arjun.cubewealth.activities.HomeActivity
import com.arjun.cubewealth.adapters.AdapterBookmarkedMovies
import com.arjun.cubewealth.dataModels.ItemEachBookmarkMovie
import com.arjun.cubewealth.viewModel.MainViewModel

class BookmarksFragment : Fragment() {
    private lateinit var myViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myViewModel = (activity as HomeActivity).myViewModel
        val rvBookmarkMovies: RecyclerView =
            view.findViewById(R.id.recyclerView_moviesRV_bookmarksFrag)
        val adapterInstance = AdapterBookmarkedMovies(BookmarkFragmentRVClickListener())
        val noBookmarksText: TextView = view.findViewById(R.id.textView_noBookmarks_bookmarsFrag)
        rvBookmarkMovies.apply {
            adapter = adapterInstance; layoutManager =
            LinearLayoutManager(this@BookmarksFragment.context)
        }

        myViewModel.getAllBookmarkedMovies().observe(this.viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                noBookmarksText.visibility = View.GONE
                rvBookmarkMovies.visibility = View.VISIBLE

                adapterInstance.differList.submitList(it.reversed())
            } else {
                noBookmarksText.visibility = View.VISIBLE
                rvBookmarkMovies.visibility = View.GONE
            }
        }

        myViewModel.getAllBookmarkedMovieIds().observe(this.viewLifecycleOwner) {
            myViewModel.updateBookmarkedIdsList(it)
        }
    }

    inner class BookmarkFragmentRVClickListener {
        fun removeBookmarkedMovie(givenMovieItem: ItemEachBookmarkMovie) {
            myViewModel.removeBookmarkedMovie(givenMovieItem)
        }

        fun moveToMovieDetailsActivity(movie_id: Int, movie_name: String?, is_bookmarked: Boolean) {
            val action = BookmarksFragmentDirections.actionBookmarksFragmentToMovieDetailActivity(
                movie_id,
                movieName = movie_name,
                isBookmarked = is_bookmarked
            )
            findNavController().navigate(action)
        }
    }
}