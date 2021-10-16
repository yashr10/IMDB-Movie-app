package com.example.imdb.API


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Detail (

    val genre_ids : List<Int>,

    val id : Int,

    val overview : String,

    val poster_path : String,

    val release_date : String,

    val title : String,

    val vote_average : Double

) : Parcelable