package com.example.mansi_goel_test.view

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mansi_goel_test.database.MovieDatabase
import com.example.mansi_goel_test.database.MovieEntity
import com.example.mansi_goel_test.model.main.MoviesMainModel
import com.example.mansi_goel_test.model.main.ResultModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.Menu
import android.view.MenuItem
import com.example.mansi_goel_test.adapter.MoviesAdapter
import com.example.mansi_goel_test.R
import com.example.mansi_goel_test.network.ApiCall
import com.example.mansi_goel_test.network.ApiCall.Companion.API_KEY
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.progress_circular
import kotlinx.android.synthetic.main.content_main.rv_movies
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var adapter: MoviesAdapter
    var movieEntityList = ArrayList<MovieEntity>()


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        setTitle("Movies")

        if (isNetworkAvailable()) {
            fetchDataFromNetwork()
        } else {
            fetchDataFromDatabase()
        }
    }

    @SuppressLint("WrongConstant")
    private fun fetchDataFromDatabase() {
        Thread({
            val movieEntityList = MovieDatabase.getAppDatabase(this@MainActivity).movieDao().getMoviesList()
            if (movieEntityList != null) {
                runOnUiThread {
                    // toast("Data fetched from database")
                    progress_circular.visibility = View.GONE
                    this.movieEntityList.addAll(movieEntityList)
                    setupRecyclerView(this.movieEntityList)
                }
            } else {
                runOnUiThread {
                    toast("No movies stored locally")
                }
            }
        }).start()
    }

    private fun fetchDataFromNetwork() {
        //fetch data from network and store it locally

        val call = ApiCall.getService().getMovies(API_KEY)

        call.enqueue(object : Callback<MoviesMainModel> {
            override fun onFailure(call: Call<MoviesMainModel>, t: Throwable) {
                progress_circular.visibility = View.GONE
                info { "failure " + t.localizedMessage }
            }

            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<MoviesMainModel>, response: Response<MoviesMainModel>) {
                info { "response " + response.code() }
                progress_circular.visibility = View.GONE

                if (response.code() == 200) {
                    val moviesMainModel = response.body();
                    createEntityFromResponseList(moviesMainModel?.results!!)
                    setupRecyclerView(movieEntityList)
                    updateDatabase(moviesMainModel)
                }
            }

        })
    }

    @SuppressLint("WrongConstant")
    private fun setupRecyclerView(moviesMainModel: List<MovieEntity>) {
        rv_movies.layoutManager = GridLayoutManager(
            this@MainActivity, 2, GridLayoutManager.VERTICAL, false
        )
        adapter = MoviesAdapter(this@MainActivity, moviesMainModel)
        rv_movies.adapter = adapter
    }

    private fun updateDatabase(moviesMainModel: MoviesMainModel?) {
        Thread({
            if (MovieDatabase.getAppDatabase(this@MainActivity).movieDao().getMoviesList() != null) {
                MovieDatabase.getAppDatabase(this@MainActivity).movieDao().deleteAll()
                info { "data deleted from database" }
            }
            createEntityFromResponseList(moviesMainModel?.results!!)
            MovieDatabase.getAppDatabase(this@MainActivity).movieDao().insertMoviesList(movieEntityList)
            info { "data saved into database" }
        }).start()
    }

    fun createEntityFromResponseList(moviesMainModelList: List<ResultModel>) {
        for (mainModel in moviesMainModelList) {
            movieEntityList.add(
                MovieEntity(
                    mainModel.id,
                    mainModel.title,
                    mainModel.releaseDate,
                    mainModel.voteAverage,
                    mainModel.posterPath,
                    mainModel.overview
                )
            )
        }
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    @SuppressLint("WrongConstant")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        when (id) {
            R.id.no_filter -> {
                adapter = MoviesAdapter(this@MainActivity, movieEntityList)
                rv_movies.adapter = adapter
                return true
            }
            R.id.rating -> {
                adapter = MoviesAdapter(this@MainActivity, movieEntityList.sortedWith(compareBy {
                    it.voteAverage
                }).asReversed())
                rv_movies.adapter = adapter
                return true
            }
            R.id.release_date -> {
                adapter = MoviesAdapter(this@MainActivity, movieEntityList.sortedWith(compareBy {
                    it.releaseDate
                }).asReversed())
                rv_movies.adapter = adapter
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
