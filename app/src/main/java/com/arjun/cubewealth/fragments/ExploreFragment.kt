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
import com.arjun.cubewealth.utills.APIResponseStateClass
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

        // When user has changed the selection of the radio button
        // then we will again call for the nowPlaying movies
        sortButton.setOnClickListener { alertDialog.show() }
        alertDialogRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            alertDialog.dismiss(); mainViewModel.getNowPlayingMovies(1)
        }

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

                    // Checking the current choice of the user and
                    // sorting the fetched movie list accordingly
                    when (alertDialogRadioGroup?.checkedRadioButtonId) {
                        R.id.radioButton_default_sortDialog -> {
                            adapterIns.differList.submitList(it.successResponseData!!.results)
                            rv.smoothScrollToPosition(0)
                        }
                        R.id.radioButton_releaseDown_sortDialog -> {
                            adapterIns.differList.submitList(
                                it.successResponseData!!.results.sortedBy { eachMovie ->
                                    eachMovie.release_date
                                })
                            rv.smoothScrollToPosition(0)
                        }

                        R.id.radioButton_releaseUp_sortDialog -> {
                            adapterIns.differList.submitList(it.successResponseData!!.results.sortedByDescending { eachMovie ->
                                eachMovie.release_date
                            })

                            rv.smoothScrollToPosition(0)
                        }
                    }
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

        fun moveToMovieDetailsActivity(movie_id: Int, movie_name: String?, is_bookmarked: Boolean) {
            val action = ExploreFragmentDirections.actionExploreFragmentToMovieDetailActivity(
                movie_id,
                movieName = movie_name,
                isBookmarked = is_bookmarked
            )
            findNavController().navigate(action)
        }

        fun getBookmarkedStatus(givenMovieId: Int): Boolean {
            return givenMovieId in mainViewModel.listBookmarkedMoviesIds
        }
    }
}