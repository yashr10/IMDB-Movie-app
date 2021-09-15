package com.example.imdb

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(private val context: Context,  private val list : MovieResults? ) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    /*private val context = context
    private val list : MovieResults = list!!*/


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        val title : TextView = itemView.findViewById(R.id.movieTitle)
        val genre : TextView = itemView.findViewById(R.id.genre)
        val plot : TextView = itemView.findViewById(R.id.plot)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val poster : ImageView = itemView.findViewById(R.id.poster)

        fun insertMovie (position : Int){

            if (list != null) {
                Glide.with(itemView).load(IMAGE_BASE + list.detail[position].poster_path).into(poster)
                title.text = list.detail[position].title
                plot.text = list.detail[position].overview
                rating.text = list.detail[position].vote_average.toString()
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.movie_cardview,parent,false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list != null) {
           /* holder.title.text = list.detail[position].title
            holder.plot.text = list.detail[position].overview
            holder.rating.text = list.detail[position].vote_average.toString()*/

            holder.insertMovie(position)
        }


    }

    override fun getItemCount(): Int {
        return if (list != null ){
            list.detail.size
        } else 0
    }
}
