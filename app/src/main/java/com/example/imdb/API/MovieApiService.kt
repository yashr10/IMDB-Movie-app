package com.example.imdb.API

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.HashMap

class MovieApiService {

    companion object{
        private const val BASE_URL = "https://api.themoviedb.org"

     //   https://api.themoviedb.org/3/movie/top_rated?api_key=77214769cb25b5a3df5ee023d2c48666&language=en-US&page=1
        private var retrofit : Retrofit? = null

        val genreList : HashMap<Int, String> = HashMap()

       fun getInstance(): Retrofit{

            if (retrofit== null ){
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

        private var retrofits : Retrofit? = null
        /*fun createList() : HashMap<Int, String> {

                retrofits = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()



                val a = retrofits!!.create(ApiInterface::class.java)
                val call: Call<Genre> = a.getGenre()
                call.enqueue(object : Callback<Genre> {
                    override fun onResponse(
                        call: Call<Genre>,
                        response: Response<Genre>
                    ) {
                        val responseBody = response.body()

                        if(!response.isSuccessful){
                            Log.d("responseser","unsucessful")
                        }else {
                            Log.d("responseser","sucessful")
                        }

                       val aa = responseBody!!.genre

                        Log.d("code code", responseBody.toString())
                        if (aa.isNullOrEmpty()){
                            Log.d("code code", "null")
                        }
                        else{
                            for (i in aa) {
                                genreList.put(i.id, i.name)
                                Log.d("added", i.name)
                            }

                        }

                    }

                    override fun onFailure(call: Call<Genre>, t: Throwable) {
                        Log.d("fail", t.message.toString())
                    }
                })

            return genreList
        }*/



    }

}