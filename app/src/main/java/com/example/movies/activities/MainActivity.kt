package com.example.movies.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.movies.R
import com.example.movies.data.MovieAdapter
import com.example.movies.model.Movie
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movies: ArrayList<Movie>
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(this)

        movies = arrayListOf()
        requestQueue = Volley.newRequestQueue(this)

        getMovies()
    }

    private fun getMovies() {
        val url = "http://www.omdbapi.com/?apikey=882da7f2&s=superman"

        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener<JSONObject> { response ->
                try {
                    val jsonArray = response?.getJSONArray("Search")
                    var i = 0
                    while (i < jsonArray!!.length()) {
                        val  jsonObject = jsonArray.getJSONObject(i)
                        val title = jsonObject.getString("Title")
                        val year = jsonObject.getString("Year")
                        val posterUrl = jsonObject.getString("Poster")

                        val movie = Movie(title,posterUrl,year)

                        movies.add(movie)

                        i++
                    }

                    val movieAdapter = MovieAdapter(applicationContext,movies)
                    recyclerView.adapter = movieAdapter

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> error.printStackTrace() })
        requestQueue.add(request)
    }
}