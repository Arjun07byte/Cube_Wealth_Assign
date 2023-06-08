package com.arjun.cubewealth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arjun.cubewealth.R
import com.arjun.cubewealth.activities.HomeActivity
import com.arjun.cubewealth.adapters.AdapterNowPlayingMovies
import com.arjun.cubewealth.dataModels.ItemEachBookmarkMovie
import com.arjun.cubewealth.paging.NowPlayingMovieLoadStateAdapter
import com.arjun.cubewealth.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

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
        val sortButton: ImageButton = view.findViewById(R.id.button_sortMovies_exploreFrag)
        val alertDialog = BottomSheetDialog(this.requireContext())
        alertDialog.setContentView(R.layout.layout_sorting_dialog)
        val alertDialogRadioGroup: RadioGroup? =
            alertDialog.findViewById(R.id.radioGroup_sortDialog)

        mainViewModel.getAllBookmarkedMovieIds().observe(this.viewLifecycleOwner) {
            mainViewModel.updateBookmarkedIdsList(it)
        }
        sortButton.visibility = View.INVISIBLE

        rv.apply {
            adapter = adapterIns.withLoadStateHeaderAndFooter(
                NowPlayingMovieLoadStateAdapter(),
                NowPlayingMovieLoadStateAdapter()
            )
            layoutManager = LinearLayoutManager(this@ExploreFragment.context)
        }

        mainViewModel.getNowPlayingMovies().observe(this.viewLifecycleOwner) {
            progressBar.visibility = View.GONE; rv.visibility = View.VISIBLE
            adapterIns.submitData(this.lifecycle, it)
        }
    }

    inner class ExploreFragmentRVClickListener {
        fun bookmarkMovie(givenMovieItem: ItemEachBookmarkMovie) {
            mainViewModel.bookmarkMovie(givenMovieItem)
        }

        fun moveToMovieDetailsActivity(
            movie_id: Int,
            movie_name: String?,
            is_bookmarked: Boolean,
            releaseDate: String?,
            backdropPath: String?
        ) {
            val action = ExploreFragmentDirections.actionExploreFragmentToMovieDetailActivity(
                movie_id, is_bookmarked, movie_name, releaseDate, backdropPath
            )
            findNavController().navigate(action)
        }

        fun getBookmarkedStatus(givenMovieId: Int): Boolean {
            return givenMovieId in mainViewModel.listBookmarkedMoviesIds
        }
    }
}