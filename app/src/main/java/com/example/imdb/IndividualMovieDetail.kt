package com.example.imdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.imdb.databinding.ActivityIndividualMovieDetailBinding
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.imdb.API.ApiInterface
import com.example.imdb.API.Detail
import com.example.imdb.API.Video
import com.google.firebase.database.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates


class IndividualMovieDetail : AppCompatActivity() {

    private lateinit var binding: ActivityIndividualMovieDetailBinding
    private lateinit var  title : TextView
    private lateinit var plot : TextView
    private lateinit var rating: TextView
    private lateinit var poster : ImageView
    lateinit var releaseDate : TextView
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"
    private lateinit var playTrailer : ImageButton
    private var movieId by Delegates.notNull<Int>()
    var trailer_id : String? = null
    private  val BASE_URL = "https://api.themoviedb.org"
    private lateinit var retrofit: Retrofit

    private  var database : FirebaseDatabase= FirebaseDatabase.getInstance()
    private lateinit var favouriteRef : DatabaseReference
    private lateinit var favouriteListRef : DatabaseReference
    var favouriteChecker : Boolean = false
    private lateinit var favouriteMovieList : ArrayList<Detail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIndividualMovieDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        title = binding.titleIndiviual
        plot = binding.plotIndividual
        rating=binding.ratingIndividual
        poster=binding.posterIndividual
        releaseDate=binding.releaseDateIndividual
        playTrailer =binding.trailerButtonIndividual2


        val detail : Detail = intent.getParcelableExtra("obj")!!
        favouriteMovieList = arrayListOf()


        title.text = intent.getStringExtra("title")
        plot.text = intent.getStringExtra("plot")
        rating.text = intent.getDoubleExtra("rating", 0.0).toString()
        releaseDate.text = intent.getStringExtra("releaseDate")

        Glide.with(this).load(IMAGE_BASE + intent.getStringExtra("poster")).into(poster)


        movieId = intent.getIntExtra("id",0)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiServices = retrofit.create(ApiInterface::class.java)
        val call : Call<Video> = apiServices.getTrailer(movieId)

        call.enqueue(object : Callback<Video?> {
            override fun onResponse(call: Call<Video?>, response: Response<Video?>) {

                val responseBody = response.body()
                if (!response.isSuccessful){
                    Log.d("token",response.code().toString())}
                for (i in responseBody!!.results){

                    if (i.official) {
                        trailer_id = i.key
                        break
                    }
                }
                if (trailer_id.isNullOrEmpty()){

                    trailer_id = responseBody.results[0].key
                }

            }

            override fun onFailure(call: Call<Video?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        playTrailer.setOnClickListener(){

            if (trailer_id.isNullOrEmpty()){

                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()

            }else{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://www.youtube.com/watch?v=$trailer_id")
                startActivity(intent)
                Log.d("id","https://www.youtube.com/watch?v=$trailer_id")
            }


        }


        favouriteListRef = database.getReference("Favourite Movies List")
        favouriteListRef.child("List").setValue(favouriteMovieList)

        favouriteRef = database.getReference("Favourite Movies")

        binding.saveForLaterIndividual.setOnClickListener(){

            favouriteChecker = true

            favouriteRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                   if (favouriteChecker){

                       if (snapshot.hasChild(title.text.toString())){

                           favouriteRef.child(title.text.toString()).removeValue()
                           favouriteMovieList.remove(detail)
                           val movie = mapOf<String,ArrayList<Detail>>(
                               "favouriteMovieList" to favouriteMovieList
                           )
                           favouriteListRef.child("List").updateChildren(movie)


                           favouriteChecker = false

                       }else{
                           favouriteRef.child(title.text.toString()).setValue(detail)
                           favouriteMovieList.add(detail)

                           val movie = mapOf<String,ArrayList<Detail>>(
                               "favouriteMovieList" to favouriteMovieList
                           )
                           favouriteListRef.child("List").updateChildren(movie)
                           favouriteChecker = false



                       }

                   }

                }

                override fun onCancelled(error: DatabaseError) {

                }


            })



        }
        favouriteRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(title.text.toString())){
                    binding.saveForLaterIndividual.setImageResource(R.drawable.ic_baseline_turned_in_24)
                }else{
                    binding.saveForLaterIndividual.setImageResource(R.drawable.ic_baseline_turned_in_not_24)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })





    }
}