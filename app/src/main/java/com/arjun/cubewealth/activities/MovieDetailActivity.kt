package com.arjun.cubewealth.activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arjun.cubewealth.R
import com.arjun.cubewealth.adapters.AdapterMovieReviews
import com.arjun.cubewealth.adapters.AdapterRoundImageWithLabel
import com.arjun.cubewealth.adapters.AdapterSimilarMovies
import com.arjun.cubewealth.dataModels.ItemImageWithLabelDisplay
import com.arjun.cubewealth.dataModels.ItemMovieGenre
import com.arjun.cubewealth.localDatabase.DatabaseBookmarkMovies
import com.arjun.cubewealth.repository.MainRepository
import com.arjun.cubewealth.utills.APIResponseStateClass
import com.arjun.cubewealth.utills.MovieDBPathToImageLink
import com.arjun.cubewealth.viewModel.MainViewModel
import com.bumptech.glide.Glide

class MovieDetailActivity : AppCompatActivity() {
    private val givenArgs: MovieDetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        // fetching the arguments passed to the Movie Details
        // activity i.e. movieId, movie Title and if the movie
        /// is bookmarked or not
        val givenMovieId = givenArgs.movieId
        val givenMovieTitle = givenArgs.movieName
        val givenMovieIsBookmarked = givenArgs.isBookmarked

        // initialising the viewModel variable
        val myViewModel = MainViewModel(MainRepository(DatabaseBookmarkMovies(this)))

        // setting up the back button and the title of movie textView
        // title is being set up by using the arguments fetched
        findViewById<TextView>(R.id.textView_movieTitle_movieDetailActivity).text = givenMovieTitle
        findViewById<ImageButton>(R.id.button_backButton_movieDetailActivity).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Intialising the view variables which will be used to
        // display the information releated to the movie Synopsis
        val textViewMovieGenre: TextView = findViewById(R.id.textView_genreText_movieDetailActivity)
        val textViewReleaseDate: TextView =
            findViewById(R.id.textView_releaseDate_movieDetailActivity)
        val textViewMovieRating: TextView = findViewById(R.id.textView_rating_movieDetailActivity)
        val textViewMovieTagline: TextView =
            findViewById(R.id.textView_taglineText_movieDetailActivity)
        val textViewMovieOverview: TextView =
            findViewById(R.id.textView_overviewText_movieDetailActivity)
        val imageViewMoviePoster: ImageView =
            findViewById(R.id.imageView_moviePoster_movieDetailActivity)
        val imageViewMovieBackdrop: ImageView =
            findViewById(R.id.imageView_movieBackdrop_movieDetailActivity)

        // initializing all the adapters for the respective recyclerView
        // which will display the respective information related to the movie
        val movieCastAdapter = AdapterRoundImageWithLabel()
        val movieCrewAdapter = AdapterRoundImageWithLabel()
        val productionCompanyAdapter = AdapterRoundImageWithLabel()
        val similarMoviesAdapter = AdapterSimilarMovies()
        val reviewsAdapter = AdapterMovieReviews()

        // setting up the view variables to denote the respective
        // recyclerView to display the respective information
        val movieProductionRecyclerView: RecyclerView =
            findViewById(R.id.rv_productionCompanies_movieDetailActivity)
        val movieCastRecyclerView: RecyclerView = findViewById(R.id.rv_cast_movieDetailActivity)
        val movieCrewRecyclerView: RecyclerView = findViewById(R.id.rv_crew_movieDetailActivity)
        val similarMoviesRecyclerView: RecyclerView =
            findViewById(R.id.rv_similarMovies_movieDetailActivity)
        val movieReviewsRecyclerView: RecyclerView =
            findViewById(R.id.rv_reviews_movieDetailActivity)

        // setting the respective recyclerViews using the adapter initialized
        // above and respective layout managers
        movieProductionRecyclerView.apply {
            adapter = productionCompanyAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        }
        movieCastRecyclerView.apply {
            adapter = movieCastAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        }
        movieCrewRecyclerView.apply {
            adapter = movieCrewAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        }
        similarMoviesRecyclerView.apply {
            adapter = similarMoviesAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        }
        movieReviewsRecyclerView.apply {
            adapter = reviewsAdapter; layoutManager = LinearLayoutManager(this.context)
        }

        // getting respective data using the functions defined in the viewModel
        myViewModel.getMovieSynopsis(givenMovieId); myViewModel.getMovieCredits(givenMovieId)
        myViewModel.getSimilarMovie(givenMovieId); myViewModel.getMovieReviews(givenMovieId)

        // listening to the respective liveData variables to regularly update the
        // UI in accordance to the data fetched
        myViewModel.liveDataMovieSynopsisList.observe(this) {
            when (it) {
                is APIResponseStateClass.SuccessResponseClass -> {
                    Glide.with(imageViewMovieBackdrop)
                        .load(MovieDBPathToImageLink.convertPathToImage(it.successResponseData!!.backdrop_path))
                        .into(imageViewMovieBackdrop)

                    Glide.with(imageViewMoviePoster)
                        .load(MovieDBPathToImageLink.convertPathToImage(it.successResponseData.poster_path))
                        .into(imageViewMoviePoster)

                    textViewMovieGenre.text = getGenreText(it.successResponseData.genres)
                    textViewReleaseDate.text = it.successResponseData.release_date
                    textViewMovieRating.text =
                        getString(
                            R.string.txt_rating_text,
                            "%.1f".format(it.successResponseData.vote_average)
                        )
                    textViewMovieOverview.text = it.successResponseData.overview
                    textViewMovieTagline.text = it.successResponseData.tagline

                    productionCompanyAdapter.differList.submitList(buildList {
                        it.successResponseData.production_companies.mapTo(this) { prodCompany ->
                            ItemImageWithLabelDisplay(prodCompany.logo_path, prodCompany.name)
                        }
                    })
                }

                else -> {

                }
            }
        }

        myViewModel.liveDataMoviesCredits.observe(this) {
            movieCastAdapter.differList.submitList(it.successResponseData?.movieCastList)
            movieCrewAdapter.differList.submitList(it.successResponseData?.movieCrewList)
        }

        myViewModel.liveDataSimilarMoviesList.observe(this) {
            similarMoviesAdapter.differList.submitList(it.successResponseData?.similarMoviesList)
        }

        myViewModel.liveDataMovieReviewsList.observe(this) {
            reviewsAdapter.differList.submitList(it.successResponseData?.movieReviewsList)
        }
    }

    // Utility function to get the Genre text to display in the UI
    // from the DATA fetched
    private fun getGenreText(genres: List<ItemMovieGenre>): String {
        var genreString = ""
        if (genres.isNotEmpty()) genreString = genreString.plus(genres[0].name)
        if (genres.size > 1) genreString = genreString.plus(" | ${genres[1].name}")
        if (genres.size > 2) genreString = genreString.plus(" | ${genres[2].name}")

        return genreString
    }
}