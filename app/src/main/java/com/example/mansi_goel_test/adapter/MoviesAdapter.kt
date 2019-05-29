package com.example.mansi_goel_test.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mansi_goel_test.R
import com.example.mansi_goel_test.database.MovieEntity
import com.example.mansi_goel_test.view.DetailActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movies.view.*

class MoviesAdapter(
    val context: Context,
    val movieModelList: List<MovieEntity>
) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPoster = itemView.home_poster;
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val rootView = LayoutInflater.from(p0.context)
            .inflate(R.layout.item_movies, p0, false);
        return ViewHolder(rootView);
    }

    override fun getItemCount(): Int {
        return movieModelList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val mainModel = movieModelList.get(p1)
        Picasso.get().load("http://image.tmdb.org/t/p/w500/" + mainModel.posterPath).into(p0.ivPoster);

        p0.ivPoster.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("title", mainModel.title)
            intent.putExtra("id", mainModel.movieId)
            context.startActivity(intent)
        }
    }
}