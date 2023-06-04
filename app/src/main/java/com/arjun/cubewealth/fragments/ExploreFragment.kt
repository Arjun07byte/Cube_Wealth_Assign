package com.arjun.cubewealth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arjun.cubewealth.R
import com.arjun.cubewealth.activities.HomeActivity
import com.arjun.cubewealth.adapters.AdapterNowPlayingMovies
import com.arjun.cubewealth.dataModels.ItemEachBookmarkMovie
import com.arjun.cubewealth.utills.APIResponseStateClass
import com.arjun.cubewealth.viewModel.MainViewModel

class ExploreFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = (activity as HomeActivity).myViewModel
        val rv: RecyclerView = view.findViewById(R.id.recyclerView_moviesRV_exploreFrag)
        val adapterIns = AdapterNowPlayingMovies(ExploreFragmentRVClickListener())
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar_exploreFragment)

        rv.apply {
            adapter = adapterIns; layoutManager = LinearLayoutManager(this@ExploreFragment.context)
        }

        mainViewModel.getNowPlayingMovies(1)
        mainViewModel.liveDataNowPlayingMovieList.observe(this.viewLifecycleOwner) {
            when (it) {
                is APIResponseStateClass.LoadingResponseClass -> {
                    progressBar.visibility = View.VISIBLE
                    rv.visibility = View.GONE
                }

                is APIResponseStateClass.SuccessResponseClass -> {
                    progressBar.visibility = View.GONE
                    rv.visibility = View.VISIBLE

                    adapterIns.differList.submitList(it.successResponseData!!.results)
                }

                else -> {
                    progressBar.visibility = View.VISIBLE
                    rv.visibility = View.GONE
                }
            }
        }

        mainViewModel.getAllBookmarkedMovieIds().observe(this.viewLifecycleOwner) {
            mainViewModel.updateBookmarkedIdsList(it)
        }
    }

    inner class ExploreFragmentRVClickListener {
        fun bookmarkMovie(givenMovieItem: ItemEachBookmarkMovie) {
            mainViewModel.bookmarkMovie(givenMovieItem)
        }

        fun getBookmarkedStatus(givenMovieId: Int): Boolean {
            return givenMovieId in mainViewModel.listBookmarkedMoviesIds
        }
    }
}