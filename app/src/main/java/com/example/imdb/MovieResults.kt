package com.example.imdb

import androidx.versionedparcelable.VersionedParcelize
import com.google.gson.annotations.SerializedName

@VersionedParcelize
 data class MovieResults(

    @SerializedName("page")
     val page : Int,
    @SerializedName("total_pages")
     val totalPage : Int,
    @SerializedName("results")
     val detail : List <Detail>
)





