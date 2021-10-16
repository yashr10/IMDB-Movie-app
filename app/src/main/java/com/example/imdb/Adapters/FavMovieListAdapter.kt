package com.example.imdb.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdb.R
import com.example.imdb.onMovieClickListener





class FavMovieListAdapter(private val context: Context, private val listOfMap : ArrayList <Map<String,*>>,private val onMovieClickListener: onMovieClickListener) :
    RecyclerView.Adapter<FavMovieListAdapter.ViewHolder>() {





    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        val title : TextView = itemView.findViewById(R.id.movieTitle)
        val genre : TextView = itemView.findViewById(R.id.genre)
        val plot : TextView = itemView.findViewById(R.id.plot)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val poster : ImageView = itemView.findViewById(R.id.poster)


        fun insertMovie (position : Int){

            Glide.with(itemView).load(IMAGE_BASE + listOfMap[position]["poster_path"]).into(poster)
            title.text = listOfMap[position]["title"].toString()
            plot.text = listOfMap[position]["overview"].toString()
            rating.text = listOfMap[position]["vote_average"].toString()
            //  genreList = MovieApiService.createList()
            //  Log.d("IMP",genreList.toString())

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.movie_cardview,parent,false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.insertMovie(position)


        holder.itemView.setOnClickListener() {
            onMovieClickListener.OnMovieItemClicked(position)
        }


        Log.d("item","clicked")



    }
    override fun getItemCount(): Int {
        return listOfMap.size
    }

}