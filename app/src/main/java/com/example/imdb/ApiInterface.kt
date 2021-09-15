package com.example.imdb

import retrofit2.Call
import retrofit2.Response
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

    @GET("/3/movie/{category}?api_key=e21eca73e69a05bfca866350af46980c&language=en-US")
    fun getAllMovies(
        @Path("category") category : String,
        @Query("page") page :Int
      /*  @Query("api key")  apiKey : String,
        @Query("language") language : String,
        @Query("page") page : Int*/
    ) : Call <MovieResults>


}