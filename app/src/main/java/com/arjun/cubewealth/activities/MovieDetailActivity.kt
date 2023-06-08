package com.arjun.cubewealth.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arjun.cubewealth.R
import com.arjun.cubewealth.adapters.AdapterMovieReviews
import com.arjun.cubewealth.adapters.AdapterRoundImageWithLabel
import com.arjun.cubewealth.adapters.AdapterSimilarMovies
import com.arjun.cubewealth.dataModels.ItemEachBookmarkMovie
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
    private lateinit var myViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        // fetching the arguments passed to the Movie Details
        // activity i.e. movieId, movie Title and if the movie
        /// is bookmarked or not
        val givenMovieId = givenArgs.movieId
        val givenMovieTitle = givenArgs.movieName
        val givenMovieIsBookmarked = givenArgs.isBookmarked
        val givenMovieRelease = givenArgs.releaseDate
        val givenMovieBackdropPath = givenArgs.backdropPath

        // initialising the viewModel variable
        myViewModel = MainViewModel(MainRepository(DatabaseBookmarkMovies(this)))

        // setting up the back button and the title of movie textView
        // title is being set up by using the arguments fetched
        findViewById<TextView>(R.id.textView_movieTitle_movieDetailActivity).text = givenMovieTitle
        findViewById<ImageButton>(R.id.button_backButton_movieDetailActivity).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // setting up the bookmark Button
        setUpBookmarkButton(
            givenMovieIsBookmarked,
            givenMovieId,
            givenMovieBackdropPath,
            givenMovieTitle,
            givenMovieRelease
        )

        myViewModel.getAllBookmarkedMovieIds().observe(this) {
            myViewModel.updateBookmarkedIdsList(it)
        }

        // Initialising the view variables which will be used to
        // display the information related to the movie Synopsis
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
        val progressBarActivityMovieDetail: ProgressBar =
            findViewById(R.id.progressBar_activityMovieDetail)
        val contentViewActivityMovieDetail: NestedScrollView =
            findViewById(R.id.contentView_activityMovieDetail)

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
        val textViewTitleProductionCompany: TextView =
            findViewById(R.id.textView_title_movieProduction)
        myViewModel.liveDataMovieSynopsisList.observe(this) {
            when (it) {
                is APIResponseStateClass.SuccessResponseClass -> {
                    progressBarActivityMovieDetail.visibility = View.GONE
                    contentViewActivityMovieDetail.visibility = View.VISIBLE
                    Glide.with(imageViewMovieBackdrop)
                        .load(MovieDBPathToImageLink.convertPathToImage(it.successResponseData!!.backdrop_path))
                        .into(imageViewMovieBackdrop)

                    Glide.with(imageViewMoviePoster)
                        .load(MovieDBPathToImageLink.convertPathToImage(it.successResponseData.poster_path))
                        .into(imageViewMoviePoster)

                    textViewMovieGenre.text =
                        getGenreText(it.successResponseData.genres).ifEmpty { "Genre Unavailable" }
                    textViewReleaseDate.text =
                        it.successResponseData.release_date.ifEmpty { "Release Date Unavailable" }
                    textViewMovieRating.text =
                        getString(
                            R.string.txt_rating_text,
                            "%.1f".format(it.successResponseData.vote_average)
                        ).ifEmpty { "Rating Unavailable" }
                    textViewMovieOverview.text =
                        it.successResponseData.overview.ifEmpty { "Overview Unavailable" }
                    textViewMovieTagline.text =
                        it.successResponseData.tagline.ifEmpty { "Tagline Unavailable" }

                    if (it.successResponseData.production_companies.isNullOrEmpty()) {
                        textViewTitleProductionCompany.visibility = View.GONE
                        movieProductionRecyclerView.visibility = View.GONE
                    } else {
                        textViewTitleProductionCompany.visibility = View.VISIBLE
                        movieProductionRecyclerView.visibility = View.VISIBLE
                        productionCompanyAdapter.differList.submitList(buildList {
                            it.successResponseData.production_companies.mapTo(this) { prodCompany ->
                                ItemImageWithLabelDisplay(prodCompany.logo_path, prodCompany.name)
                            }
                        })
                    }
                }

                is APIResponseStateClass.LoadingResponseClass -> {
                    progressBarActivityMovieDetail.visibility = View.VISIBLE
                    contentViewActivityMovieDetail.visibility = View.GONE
                }

                else -> {
                    progressBarActivityMovieDetail.visibility = View.GONE
                    contentViewActivityMovieDetail.visibility = View.GONE
                    Toast.makeText(this, "ERROR OCCURRED", Toast.LENGTH_LONG).show()
                }
            }
        }

        val textViewTitleCast: TextView = findViewById(R.id.textView_title_movieCasts)
        val textViewTitleCrew: TextView = findViewById(R.id.textView_title_movieCrews)
        myViewModel.liveDataMoviesCredits.observe(this) {
            when (it) {
                is APIResponseStateClass.SuccessResponseClass -> {
                    textViewTitleCrew.visibility = View.VISIBLE; textViewTitleCast.visibility =
                        View.VISIBLE
                    movieCrewRecyclerView.visibility =
                        View.VISIBLE; movieCastRecyclerView.visibility = View.VISIBLE
                    movieCastAdapter.differList.submitList(it.successResponseData?.movieCastList)
                    movieCrewAdapter.differList.submitList(it.successResponseData?.movieCrewList)
                }

                else -> {
                    textViewTitleCrew.visibility = View.GONE; textViewTitleCast.visibility =
                        View.GONE
                    movieCrewRecyclerView.visibility = View.GONE; movieCastRecyclerView.visibility =
                        View.GONE
                }
            }
        }

        val textViewTitleSimilarMovies: TextView = findViewById(R.id.textView_title_similarMovies)
        myViewModel.liveDataSimilarMoviesList.observe(this) {
            if (it is APIResponseStateClass.SuccessResponseClass) {
                textViewTitleSimilarMovies.visibility = View.VISIBLE
                similarMoviesRecyclerView.visibility = View.VISIBLE
                similarMoviesAdapter.differList.submitList(it.successResponseData?.similarMoviesList)
            } else {
                textViewTitleSimilarMovies.visibility = View.GONE
                similarMoviesRecyclerView.visibility = View.GONE
            }

        }

        val textViewTitleReview: TextView = findViewById(R.id.textView_title_movieReviews)
        myViewModel.liveDataMovieReviewsList.observe(this) {
            if (it is APIResponseStateClass.SuccessResponseClass && it.successResponseData!!.movieReviewsList.isNotEmpty()) {
                textViewTitleReview.visibility = View.VISIBLE
                movieReviewsRecyclerView.visibility = View.VISIBLE
                reviewsAdapter.differList.submitList(it.successResponseData.movieReviewsList)
            } else {
                textViewTitleReview.visibility = View.GONE
                movieReviewsRecyclerView.visibility = View.GONE
            }

        }
    }

    private fun setUpBookmarkButton(
        givenMovieIsBookmarked: Boolean,
        givenMovieId: Int,
        givenMovieBackdropPath: String?,
        givenMovieTitle: String?,
        givenMovieRelease: String?
    ) {
        val buttonBookmarkMovie: ImageButton =
            findViewById(R.id.button_addBookmark_activityMovieDetail)
        if (givenMovieIsBookmarked) {
            // Setting up an empty function as an onClickListener for
            // bookmark button as the movie is already bookmarked
            buttonBookmarkMovie.setOnClickListener {}
        } else {
            // Adding the movie as a bookmark in the ROOM Database
            // and then assigning the FAB a new background and icon
            // to denote that the movie is bookmarked
            buttonBookmarkMovie.setBackgroundResource(R.drawable.bg_bookmark_movie_button)
            buttonBookmarkMovie.setImageResource(R.drawable.ic_bookmark)

            buttonBookmarkMovie.setOnClickListener {
                myViewModel.bookmarkMovie(
                    getBookmarkMovieItem(
                        givenMovieId,
                        givenMovieBackdropPath,
                        givenMovieTitle,
                        givenMovieRelease
                    )
                )
                buttonBookmarkMovie.setBackgroundResource(R.drawable.bg_bookmarked_movie_button)
                buttonBookmarkMovie.setImageResource(R.drawable.ic_bookmarked)
                // Setting up an empty function as an onClickListener for
                // bookmark button as the movie is already bookmarked
                buttonBookmarkMovie.setOnClickListener {}
            }
        }
    }

    // Utility function to get the BookmarkMovie Item
    // from the given values
    private fun getBookmarkMovieItem(
        id: Int,
        backdropPicPath: String?,
        movieTitle: String?,
        releaseDate: String?
    ): ItemEachBookmarkMovie =
        ItemEachBookmarkMovie(id, backdropPicPath ?: "", movieTitle ?: "", releaseDate ?: "")

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