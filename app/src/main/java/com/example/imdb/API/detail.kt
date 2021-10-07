package com.example.imdb.API


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class Detail (
    @SerializedName("poster_path")
    val poster_path : String,
    @SerializedName("title")
    val title : String,
    @SerializedName("overview")
    val overview : String,
    @SerializedName("vote_average")
    val vote_average : Double,
    @SerializedName("genre_ids")
    val genre_ids : List<Int>,

    val release_date : String,

    val id : Int
) : Parcelable