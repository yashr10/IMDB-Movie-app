package com.example.imdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.imdb.databinding.ActivityIndividualMovieDetailBinding

class IndividualMovieDetail : AppCompatActivity() {

    private lateinit var binding: ActivityIndividualMovieDetailBinding
    lateinit var  title : TextView
    // lateinit var genre : TextView
    lateinit var plot : TextView
    lateinit var rating: TextView
    lateinit var poster : ImageView
    lateinit var releaseDate : TextView
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIndividualMovieDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        title = binding.titleIndiviual
        plot = binding.plotIndividual
        rating=binding.ratingIndividual
        poster=binding.posterIndividual
        releaseDate=binding.releaseDateIndividual


        title.text = intent.getStringExtra("title")
        plot.text = intent.getStringExtra("plot")
        rating.text = intent.getStringExtra("rating")
        releaseDate.text = intent.getStringExtra("releaseDate")

        Glide.with(this).load(IMAGE_BASE + intent.getStringExtra("poster")).into(poster)





    }
}