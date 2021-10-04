package com.example.imdb.API

import androidx.versionedparcelable.VersionedParcelize
import com.google.gson.annotations.SerializedName

@VersionedParcelize
 data class MovieResults(

    @SerializedName("page")
     val page : Int = 0,
    @SerializedName("total_pages")
     val totalPage : Int = -1,
    @SerializedName("results")
     val detail : ArrayList <Detail> = ArrayList()
)





