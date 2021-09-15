package com.example.imdb

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MovieApiService {

    companion object{
        private const val BASE_URL = "https://api.themoviedb.org"
        private const val PAGE = "1"
        private const val API_KEY = "77214769cb25b5a3df5ee023d2c48666"
        private const val LANGUAGE = "en-US"
     //   https://api.themoviedb.org/3/movie/top_rated?api_key=77214769cb25b5a3df5ee023d2c48666&language=en-US&page=1
        private var retrofit : Retrofit? = null

        fun getInstance(): Retrofit{

            if (retrofit== null ){
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }



    }

}