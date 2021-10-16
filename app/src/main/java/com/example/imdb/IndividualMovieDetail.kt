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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.properties.Delegates


class IndividualMovieDetail : AppCompatActivity() {

    private lateinit var binding: ActivityIndividualMovieDetailBinding
    private lateinit var title: TextView
    private lateinit var plot: TextView
    private lateinit var rating: TextView
    private lateinit var poster: ImageView
    lateinit var releaseDate: TextView
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"
    private lateinit var playTrailer: ImageButton
    private var movieId by Delegates.notNull<Int>()
    var trailer_id: String? = null
    private val BASE_URL = "https://api.themoviedb.org"
    private lateinit var retrofit: Retrofit

    private lateinit var mAuth: FirebaseAuth


    val db = Firebase.firestore
    /*   private  var database : FirebaseDatabase= FirebaseDatabase.getInstance()
    private lateinit var favouriteRef : DatabaseReference
    private lateinit var favouriteListRef : DatabaseReference
    var favouriteChecker : Boolean = false
    private lateinit var favouriteMovieList : ArrayList<Detail>*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIndividualMovieDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        title = binding.titleIndiviual
        plot = binding.plotIndividual
        rating = binding.ratingIndividual
        poster = binding.posterIndividual
        releaseDate = binding.releaseDateIndividual
        playTrailer = binding.trailerButtonIndividual2


        val detail: Detail = intent.getParcelableExtra("obj")!!
        Log.d("a", detail.toString())
        // favouriteMovieList = arrayListOf()


        title.text = intent.getStringExtra("title")
        plot.text = intent.getStringExtra("plot")
        rating.text = intent.getDoubleExtra("rating", 0.0).toString()
        releaseDate.text = intent.getStringExtra("releaseDate")

        Glide.with(this).load(IMAGE_BASE + intent.getStringExtra("poster")).into(poster)


        movieId = intent.getIntExtra("id", 0)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiServices = retrofit.create(ApiInterface::class.java)
        val call: Call<Video> = apiServices.getTrailer(movieId)

        call.enqueue(object : Callback<Video?> {
            override fun onResponse(call: Call<Video?>, response: Response<Video?>) {

                val responseBody = response.body()
                if (!response.isSuccessful) {
                    Log.d("token", response.code().toString())
                }
                else{
                    for (i in responseBody!!.results) {
                        Log.d("token", "reached")
                        if (i.official && i.type == "Trailer") {
                            trailer_id = i.key
                            Log.d("if loop", "reached")
                            break
                        }
                        if (i.type == "Trailer") {
                            trailer_id = i.key
                        }
                    }

                }


            }

            override fun onFailure(call: Call<Video?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        playTrailer.setOnClickListener() {


            if (trailer_id == null) {

                Log.d("trailer id", trailer_id.toString())
                Toast.makeText(this, "no trailer available", Toast.LENGTH_LONG).show()
                Log.d("trailer id", trailer_id.toString())
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://www.youtube.com/watch?v=$trailer_id")
                startActivity(intent)
                Log.d("id", "https://www.youtube.com/watch?v=$trailer_id")
            }


        }

        mAuth = FirebaseAuth.getInstance()

        val currentUser = mAuth.currentUser

        binding.saveForLaterIndividual.setOnClickListener {

            if (currentUser == null) {

                Toast.makeText(this, "Log In for this feaure", Toast.LENGTH_SHORT).show()
            } else {
                val docRef = db.collection("users").document(currentUser.uid)

                docRef.get().addOnSuccessListener {

                    if (it != null && it.exists()) {

                      //  val a = it.toObject(ListDetail().javaClass)!!.listOfDetail
                        val a = it.get("favourite") as ArrayList <Map<String,*>>?
                        Log.d("array", a.toString())

                        var contains = false

                        if (a != null) {
                            for (i in a) {
                                val id = i.getValue("id").toString()
                                Log.d("imp",id)
                                if ( id == detail.id.toString()){
                                    contains = true
                                    break
                                }
                            }
                        }


                        Log.d("contains", contains.toString())
                        if (contains) {
                            docRef.update("favourite", FieldValue.arrayRemove(detail))
                            binding.saveForLaterIndividual.setImageResource(R.drawable.ic_baseline_turned_in_not_24)
                            Log.d("removed", "remove")

                        } else {
                            docRef.update("favourite", FieldValue.arrayUnion(detail))
                            binding.saveForLaterIndividual.setImageResource(R.drawable.ic_baseline_turned_in_24)
                            Log.d("added", "added")
                        }

                    } else {
                        Log.d("snapshot", " is null")
                        Toast.makeText(this, "complete profile to use this feature", Toast.LENGTH_SHORT).show()

                    }


                }
            }
        }


        if (currentUser != null){
            val docRef = db.collection("users").document(currentUser.uid)

            docRef.get().addOnSuccessListener {

                if (it != null) {

                    val a = it.get("favourite") as ArrayList <Map<String,*>>?


                    var contains = false

                    if (a != null) {
                        for (i in a) {
                            val id = i.getValue("id").toString()
                            Log.d("imp",id)
                            if ( id == detail.id.toString()){
                                contains = true
                                break
                            }

                        }
                    }

                    if (contains) {
                        binding.saveForLaterIndividual.setImageResource(R.drawable.ic_baseline_turned_in_24)
                    } else {
                        binding.saveForLaterIndividual.setImageResource(R.drawable.ic_baseline_turned_in_not_24)
                    }

                } else {
                    Log.d("snapshot", " is null")
                    return@addOnSuccessListener
                }


            }
        }



        /*docRef.addSnapshotListener { snapshot, error ->

                      if (error != null){
                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                      }

                        if (snapshot != null){

                            val a : ArrayList<Detail> = snapshot.data?.get("favourite") as ArrayList<Detail>
                            Log.d("array", a.toString())

                            if (a.contains(detail)){
                                docRef.update("favourite" , FieldValue.arrayRemove(detail))

                                Log.d("removed", "to favourites")
                            }else{
                                docRef.update("favourite" , FieldValue.arrayUnion(detail))
                                binding.saveForLaterIndividual.setImageResource(R.drawable.ic_baseline_turned_in_24)
                                Log.d("added", "to favourites")
                            }
                        }else{
                            Log.d("snapshot", " is null")
                        }


                    }*/

    }

    inner class ListDetail(){
        var listOfDetail: ArrayList<Detail>? = null



    }
}
