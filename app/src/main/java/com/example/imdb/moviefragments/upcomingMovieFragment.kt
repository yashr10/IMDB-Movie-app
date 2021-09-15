package com.example.imdb.moviefragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class upcomingMovieFragment : Fragment() {

    private  val PAGE : Int = 1

    private val CATEGORY = "upcoming"
    lateinit var myAdapter: MyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_upcoming_movie, container, false)

        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerView_upcoming)

        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager


        val apiServices = MovieApiService.getInstance().create(ApiInterface::class.java)

        val call : Call<MovieResults> = apiServices.getAllMovies(CATEGORY,PAGE)



        call.enqueue(object : Callback<MovieResults> {
            override fun onResponse(call: Call<MovieResults>, response: Response<MovieResults>) {
                if (!response.isSuccessful){
                    Log.d("token",response.code().toString())}
                val responseBody = response.body()
                myAdapter = MyAdapter(context!!,responseBody)
                myAdapter.notifyDataSetChanged()
                recyclerView.adapter = myAdapter
            }

            override fun onFailure(call: Call<MovieResults?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        return view
    }


}