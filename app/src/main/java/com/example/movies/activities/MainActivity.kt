package com.example.movies.activities

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.ConnectivityManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.movies.R
import com.example.movies.data.MovieAdapter
import com.example.movies.model.Movie
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {


    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var messageTextView: TextView
    private lateinit var titleHumanImageView: ImageView

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var requestQueue: RequestQueue
    //  private lateinit var movies: ArrayList<Movie>

    private lateinit var url: String
    private var searchTitle = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        messageTextView = findViewById(R.id.messageTextView)
        titleHumanImageView = findViewById(R.id.titleHumanImageView)

        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(this)
        //movies = arrayListOf()
        requestQueue = Volley.newRequestQueue(this)

        val cm: ConnectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        searchButton.setOnClickListener {
            try {
                 activeNetwork.isConnectedOrConnecting
                searchTitle = searchEditText.text.toString()
                if (searchTitle.isEmpty()) {
                    setVisibleForNotFound()
                    messageTextView.text = "Please enter a movie name"
                } else {
                    url = "http://www.omdbapi.com/?apikey=882da7f2&s=${searchEditText.text}"
                    getMovies()
                }
            } catch (e: Exception) {
                setVisibleForNotFound()
                messageTextView.text = "need internet connection"
            }

        }
    }

    private fun setVisibleForNotFound() {
        recyclerView.visibility = View.INVISIBLE
        messageTextView.visibility = View.VISIBLE
        titleHumanImageView.visibility = View.VISIBLE

    }

    @SuppressLint("SetTextI18n")
    private fun getMovies() {
        val movies = arrayListOf<Movie>()
        Log.i("moviesQQQQQ", "${movies.size}")
        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener<JSONObject> { response ->
                try {
                    val jsonArray = response?.getJSONArray("Search")
                    var i = 0
                    while (i < jsonArray!!.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val title = jsonObject.getString("Title")
                        val year = jsonObject.getString("Year")
                        val posterUrl = jsonObject.getString("Poster")


                        val movie = Movie(title, posterUrl, year)

                        movies.add(movie)

                        i++
                    }
                    messageTextView.visibility = View.INVISIBLE
                    titleHumanImageView.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                    movieAdapter = MovieAdapter(applicationContext, movies)
                    recyclerView.adapter = movieAdapter
                } catch (e: JSONException) {
                    e.printStackTrace()
                    setVisibleForNotFound()
                    messageTextView.text = "Movie by name not found"

                }
            },
            Response.ErrorListener { error -> error.printStackTrace() })
        requestQueue.add(request)
    }
}