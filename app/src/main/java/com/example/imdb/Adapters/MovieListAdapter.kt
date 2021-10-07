package com.example.imdb.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imdb.API.Detail
import com.example.imdb.API.MovieResults
import com.example.imdb.IndividualMovieDetail
import com.example.imdb.R
import com.example.imdb.onMovieClickListener
import retrofit2.Callback

class MovieListAdapter(private val context: Context, private val list: ArrayList <Detail>?, private val onMovieClickListener: onMovieClickListener) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    /*private val context = context
    private val list : MovieResults = list!!*/

  //  var genreList : HashMap<Int, String> = HashMap()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        val title : TextView = itemView.findViewById(R.id.movieTitle)
        val genre : TextView = itemView.findViewById(R.id.genre)
        val plot : TextView = itemView.findViewById(R.id.plot)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val poster : ImageView = itemView.findViewById(R.id.poster)


        fun insertMovie (position : Int){

            if (list != null) {
                Glide.with(itemView).load(IMAGE_BASE + list[position].poster_path).into(poster)
                title.text = list[position].title
                plot.text = list[position].overview
                rating.text = list[position].vote_average.toString()
              //  genreList = MovieApiService.createList()
              //  Log.d("IMP",genreList.toString())
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.movie_cardview,parent,false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (list != null ) {
            holder.insertMovie(position)

           /* for (i in list[position].genre_ids){
                val genre = genreList.get(i)
                holder.genre.append("$genre, ")
            }*/

            holder.itemView.setOnClickListener() {
                onMovieClickListener.OnMovieItemClicked(position)
            }
        }


            Log.d("item","clicked")

               /* bundle.putString("title", list[position].title)
                bundle.putString("poster",list[position].poster_path)
                bundle.putDouble("rating", list[position].vote_average)
                bundle.putString("releaseDate", list[position].release_date)
                bundle.putString("plot", list[position].overview)*/


        }
    override fun getItemCount(): Int {
        return if (list != null ){
            list.size
        } else 0
    }

}






