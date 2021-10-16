package com.example.imdb.API

data  class Video (
    val id : Int,
    val results: List<VideoDetails>
        )

data class VideoDetails (
    val key : String,
    val official : Boolean ,
    val type : String
        )