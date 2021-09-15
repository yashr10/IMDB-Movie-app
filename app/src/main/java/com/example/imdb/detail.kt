package com.example.imdb

import com.google.gson.annotations.SerializedName

data class Detail(
    @SerializedName("poster_path")
    val poster_path : String,
    @SerializedName("title")
    val title : String,
    @SerializedName("overview")
    val overview : String,
    @SerializedName("vote_average")
    val vote_average : Double,
    @SerializedName("genre_ids")
    val genre_ids : List<Int>
)