package com.example.imdb.API

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
 data class MovieResults(

    @SerializedName("page")
     val page : Int = 0,
    @SerializedName("total_pages")
     val totalPage : Int = -1,
    @SerializedName("results")
     val detail : ArrayList <Detail> = ArrayList()
) : Parcelable





