package com.example.imdb.API

data class Genre (
    val genre : ArrayList<GenreDetail>
        )

data class GenreDetail ( val id: Int,
                         val name: String)














