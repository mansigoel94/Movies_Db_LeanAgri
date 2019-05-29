package com.example.mansi_goel_test.view

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mansi_goel_test.R
import com.example.mansi_goel_test.adapter.MoviesAdapter
import com.example.mansi_goel_test.database.MovieDatabase
import com.example.mansi_goel_test.database.MovieDetailEntity
import com.example.mansi_goel_test.model.detail.DetailMainModel
import com.example.mansi_goel_test.network.ApiCall
import com.example.mansi_goel_test.network.ApiCall.Companion.API_KEY
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.progress_circular
import kotlinx.android.synthetic.main.activity_detail.toolbar
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity(), AnkoLogger {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        setTitle(intent.getStringExtra("title"))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movieId = intent.getIntExtra("id", 0)
        if (movieId != 0) {
            if (isNetworkAvailable()) {
                fetchDataFromNetwork(movieId.toString())
            } else {
                fetchDataFromDatabase(movieId.toString())
            }
        }
    }

    private fun fetchDataFromNetwork(movieId: String) {
        //fetch data from network and store it locally
        val call = ApiCall.getService().getMovieDetails(movieId, API_KEY)

        call.enqueue(object : Callback<DetailMainModel> {
            override fun onFailure(call: Call<DetailMainModel>, t: Throwable) {
                progress_circular.visibility = View.GONE
                info { "failure " + t.localizedMessage }
            }

            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<DetailMainModel>, response: Response<DetailMainModel>) {
                info { "response " + response.code() }
                progress_circular.visibility = View.GONE

                if (response.code() == 200) {
                    val moviesMainModel = response.body()
                    updateUI(getEntityFromResponse(moviesMainModel!!))
                    updateDatabase(moviesMainModel)
                }
            }
        })
    }

    private fun updateUI(movieDetailEntity: MovieDetailEntity) {
        divider.visibility = View.VISIBLE
        Picasso.get().load("http://image.tmdb.org/t/p/w500/" + movieDetailEntity?.posterPath)
            .into(detail_poster)
        detail_release_date.text = movieDetailEntity.releaseDate
        detail_duration.text = movieDetailEntity.runTime.toString() + " mins"
        detail_ratings.text = movieDetailEntity.voteAverage.toString() + "/10"
        detail_synopsis.text = movieDetailEntity.overview
    }

    private fun updateDatabase(detailMainModel: DetailMainModel?) {
        Thread {
            MovieDatabase.getAppDatabase(this@DetailActivity).movieDao().deleteMovieDetails(
                detailMainModel?.id.toString()
            )
            info { "data deleted from database" }
            val movieDetailEntity = MovieDetailEntity(
                detailMainModel?.id,
                detailMainModel?.title,
                detailMainModel?.voteAverage,
                detailMainModel?.runtime,
                detailMainModel?.posterPath,
                detailMainModel?.releaseDate,
                detailMainModel?.overview
            );

            MovieDatabase.getAppDatabase(this@DetailActivity).movieDao().insertMovieDetails(movieDetailEntity)
            info { "data saved into database" }
        }.start()
    }

    @SuppressLint("WrongConstant")
    private fun fetchDataFromDatabase(movieId: String) {
        Thread({
            val movieDetailEntity = MovieDatabase.getAppDatabase(this@DetailActivity).movieDao()
                .getMovieDetails(movieId)
            if (movieDetailEntity != null) {
                runOnUiThread {
                    // toast("Data fetched from database")
                    progress_circular.visibility = View.GONE
                    updateUI(movieDetailEntity)
                }
            } else {
                runOnUiThread {
                    toast("Movie not stored locally")
                    progress_circular.visibility = View.GONE
                }
            }
        }).start()
    }

    fun getEntityFromResponse(mainModel: DetailMainModel): MovieDetailEntity {
        val detailEntity = MovieDetailEntity(
            mainModel.id, mainModel.title,
            mainModel.voteAverage, mainModel.runtime, mainModel.posterPath,
            mainModel.releaseDate, mainModel.overview
        )
        return detailEntity
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    @SuppressLint("WrongConstant")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        when (id) {
            android.R.id.home -> {
                super.onBackPressed()
                return true;
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
