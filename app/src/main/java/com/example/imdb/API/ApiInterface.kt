package com.example.imdb.API

import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    /*@GET("/3/movie/{category}")
    Call <MovieResults> getMovies(
    @Path("category") category : String,
    @Query("api key") apiKey : String ,
    @Query("language") language : String ,
    @Query("page") page : String
    )
*/
    // https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=<<api_key>>&language=en-US

    @GET("/3/movie/{category}?api_key=e21eca73e69a05bfca866350af46980c&language=en-US")
   fun getAllMovies(
        @Path("category") category : String,
        @Query("page") page :Int
      /*  @Query("api key")  apiKey : String,
        @Query("language") language : String,
        @Query("page") page : Int*/
    ) : Call <MovieResults>

    @GET("/3/genre/movie/list?api_key=e21eca73e69a05bfca866350af46980c&language=en-US")
    fun getGenre () : Call <Genre>

    @GET("/3/movie/{movie_id}/videos?api_key=e21eca73e69a05bfca866350af46980c&language=en-US")
    fun getTrailer(
        @Path("movie_id") movie_id : Int
    ): Call<Video>

}