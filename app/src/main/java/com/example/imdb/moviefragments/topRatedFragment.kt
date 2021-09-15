package com.example.imdb.moviefragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TopRatedFragment : Fragment() {

    private  val PAGE : Int = 1
    private val LIMIT : Int = 20

    private val CATEGORY = "top_rated"
    lateinit var myAdapter: MyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var nestedScrollView: NestedScrollView
   // https://api.themoviedb.org/3/movie/top_rated?api_key=77214769cb25b5a3df5ee023d2c48666&language=en-US&page=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_top_rated, container, false)

        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerView_topRated)
        nestedScrollView = view.findViewById(R.id.ns)

        nestedScrollView.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (v != null) {
                    if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight){

                    }

                }
            }

        })



        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager


        val apiServices = MovieApiService.getInstance().create(ApiInterface::class.java)

        val call : Call<MovieResults> = apiServices.getAllMovies(CATEGORY,PAGE)



        call.enqueue(object : Callback<MovieResults> {
            override fun onResponse(call: Call<MovieResults>, response: Response<MovieResults>) {
                if (!response.isSuccessful){Log.d("token",response.code().toString())}
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
