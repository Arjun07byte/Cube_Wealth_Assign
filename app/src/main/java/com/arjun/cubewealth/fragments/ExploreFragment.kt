package com.arjun.cubewealth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arjun.cubewealth.R
import com.arjun.cubewealth.adapters.AdapterNowPlayingMovies

class ExploreFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nestedScrollView: NestedScrollView =
            view.findViewById(R.id.nestedScrollView_exploreFragment)
        val rv: RecyclerView = view.findViewById(R.id.recyclerView_moviesRV_exploreFrag)
        val adapterIns = AdapterNowPlayingMovies()
        rv.apply {
            adapter = adapterIns; layoutManager = LinearLayoutManager(this@ExploreFragment.context)
        }

        // Using Nested Scroll View to implement the pagination feature to the recycler view
        // being used to show movies list
        nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == ((v as NestedScrollView).getChildAt(0).measuredHeight) - v.measuredHeight) {
                adapterIns.getNewItems()
            }
        }
    }
}