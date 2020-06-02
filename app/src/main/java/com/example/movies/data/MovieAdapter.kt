package com.example.movies.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.model.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(private val context: Context, private val movies: ArrayList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val posterImage: ImageView = itemView.findViewById(R.id.posterImage)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val yearTextView: TextView = itemView.findViewById(R.id.yearTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movia_item,parent,false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
       return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie = movies[position]
        val posterUrl = currentMovie.poster

        Picasso.get().load(posterUrl).fit().centerInside().into(holder.posterImage)
        holder.titleTextView.text = currentMovie.title
        holder.yearTextView.text = currentMovie.year

    }
}