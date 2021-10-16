package com.example.imdb.moviefragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.*
import com.example.imdb.API.ApiInterface
import com.example.imdb.API.Detail
import com.example.imdb.API.MovieApiService
import com.example.imdb.API.MovieResults
import com.example.imdb.Adapters.MovieListAdapter
import com.example.imdb.databinding.FragmentTopRatedBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class TopRatedFragment : Fragment() , onMovieClickListener {

    private var _binding : FragmentTopRatedBinding? = null
    private var PageStart = 1
    private var PageEnd = 3
    private val CATEGORY = "top_rated"
    lateinit var myAdapter: MovieListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var nestedScrollView: NestedScrollView
    lateinit var recyclerView : RecyclerView
   lateinit var   fetchedList : ArrayList <Detail>
   lateinit var tempList : ArrayList<Detail>
   lateinit var finalList : ArrayList<Detail>

    private val binding get() = _binding!!

   // https://api.themoviedb.org/3/movie/top_rated?api_key=77214769cb25b5a3df5ee023d2c48666&language=en-US&page=1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopRatedBinding.inflate(inflater, container, false)

        val root: View = binding.root
        setHasOptionsMenu(true)


        recyclerView = binding.recyclerViewTopRated
        nestedScrollView = binding.ns
        tempList = arrayListOf()
        finalList = arrayListOf()

       nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v != null) {
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight){
                    PageStart+= 1
                    PageEnd+= 1
                    work()
                }
            }
        })

        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager



        for (i in PageStart..PageEnd){

            val apiServices = MovieApiService.getInstance().create(ApiInterface::class.java)
            val call : Call<MovieResults> = apiServices.getAllMovies(CATEGORY,i)

            call.enqueue(object : Callback<MovieResults> {
                override fun onResponse(call: Call<MovieResults>, response: Response<MovieResults>) {
                    if (!response.isSuccessful){Log.d("token",response.code().toString())}
                    val responseBody = response.body()
                    fetchedList  = responseBody!!.detail
                    tempList.addAll(fetchedList)
                    finalList.addAll(fetchedList)

                    myAdapter = MovieListAdapter(context!!,finalList,this@TopRatedFragment)
                       myAdapter.notifyDataSetChanged()
                    recyclerView.adapter = myAdapter
                }

                override fun onFailure(call: Call<MovieResults?>, t: Throwable) {
                    Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
                }
            })

        }



       /* myAdapter = activity?.let { MyAdapter(it,tempList) }!!
        myAdapter.notifyDataSetChanged()
        recyclerView.adapter = myAdapter*/
        /*val call : Call<MovieResults> = apiServices.getAllMovies(CATEGORY,PAGE)

        call.enqueue(object : Callback<MovieResults> {
            override fun onResponse(call: Call<MovieResults>, response: Response<MovieResults>) {
                if (!response.isSuccessful){Log.d("token",response.code().toString())}
                val responseBody = response.body()
                fetchedList  = responseBody!!.detail

                tempList.addAll(fetchedList)

                myAdapter = MyAdapter(context!!,tempList)
                myAdapter.notifyDataSetChanged()
                recyclerView.adapter = myAdapter
            }

            override fun onFailure(call: Call<MovieResults?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })*/


      /*  val apiServices = MovieApiService.getInstance().create(ApiInterface::class.java)

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
        })*/

        return root
    }

    private fun work() {
        for (i in PageStart..PageEnd){

            val apiServices = MovieApiService.getInstance().create(ApiInterface::class.java)
            val call : Call<MovieResults> = apiServices.getAllMovies(CATEGORY,i)

            call.enqueue(object : Callback<MovieResults> {
                override fun onResponse(call: Call<MovieResults>, response: Response<MovieResults>) {
                    if (!response.isSuccessful){Log.d("token",response.code().toString())}
                    val responseBody = response.body()
                    fetchedList  = responseBody!!.detail
                    tempList.addAll(fetchedList)
                    finalList.addAll(fetchedList)
                    myAdapter = MovieListAdapter(context!!,finalList,this@TopRatedFragment)
                    myAdapter.notifyDataSetChanged()
                    recyclerView.adapter = myAdapter
                }

                override fun onFailure(call: Call<MovieResults?>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

        }

    }


    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.title_menu -> {
                if (!item.isChecked) {
                    item.isChecked
                    tempList.sortBy {
                        it.title
                    }
                }else {
                    item.isChecked = false

                    recyclerView.adapter!!.notifyDataSetChanged()
                    nestedScrollView.fullScroll(View.FOCUS_UP)

                }


            }
            R.id.rating_menu -> {
                if (!item.isChecked) {
                    item.isChecked
                    tempList.sortBy {
                        it.vote_average
                    }
                }else {
                    item.isChecked = false
                    tempList = fetchedList

                    recyclerView.adapter!!.notifyDataSetChanged()
                    nestedScrollView.fullScroll(View.FOCUS_UP)

                }


            }
            R.id.releaseDate_menu -> {

                if (!item.isChecked) {
                    item.isChecked
                    tempList.sortBy {
                        it.release_date
                    }
                }else {
                    item.isChecked = false
                    tempList = fetchedList

                    recyclerView.adapter!!.notifyDataSetChanged()
                    nestedScrollView.fullScroll(View.FOCUS_UP)

                }

            }
            else ->
            return super.onOptionsItemSelected(item)

        }
        return true
    }
*/

    private fun filtering (menuItem: MenuItem, filter : String,menu: Menu) {

        if (!menuItem.isChecked) {
            clearSelectedMenu(menu)
            menuItem.isChecked = true

            if (filter == "title") {
                tempList.sortBy {
                    it.title
                }
            }
            if (filter == "release_date") {

                tempList.sortBy {
                    it.release_date
                }
            }
            if (filter == "vote_average") {
                tempList.sortBy {
                    it.vote_average
                }
            }

            myAdapter = MovieListAdapter(requireContext(), tempList,this)
            recyclerView.adapter!!.notifyDataSetChanged()

            recyclerView.adapter = myAdapter
            nestedScrollView.fullScroll(View.FOCUS_UP)

        } else {
            menuItem.isChecked = false

            /* recyclerView.adapter!!.notifyDataSetChanged()*/

            tempList = finalList
            myAdapter = MovieListAdapter(requireContext(), finalList,this)
            recyclerView.adapter!!.notifyDataSetChanged()
            recyclerView.adapter = myAdapter
            nestedScrollView.fullScroll(View.FOCUS_UP)

        }


    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_item,menu)

       // val filter = menu.findItem(R.id.filter)

        /*filter.setOnMenuItemClickListener {
            Log.d("hi", "hi")
            recyclerView.adapter!!.notifyDataSetChanged()
            //  recyclerView.smoothScrollToPosition(0)
            nestedScrollView.fullScroll(View.FOCUS_UP)
            true
        }*/

       val titleFilter =  menu.findItem(R.id.title_menu)
           titleFilter.setOnMenuItemClickListener {

               filtering(it, "title",menu)

            true }

        val ratingFilter =  menu.findItem(R.id.rating_menu)
        ratingFilter.setOnMenuItemClickListener {

            filtering(it,"release_date",menu)

            true }

        val releaseDateFilter =  menu.findItem(R.id.releaseDate_menu)
        releaseDateFilter.setOnMenuItemClickListener {

            filtering(it,"vote_average",menu)

            true }



        //  val item = menu.findItem(R.id.app_bar_search)
     //   val searchView = item?.actionView as SearchView

       /* searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                tempList.clear()
                val searchText = newText!!.lowercase()
                if (searchText.isNotEmpty()){

                    fetchedList.forEach{

                        if (it.title.lowercase().contains(searchText)){
                            tempList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()

                }else{
                    tempList.clear()
                    tempList.addAll(fetchedList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }
        })*/

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun clearSelectedMenu(menu: Menu) {

        menu.findItem(R.id.title_menu).isChecked = false
        menu.findItem(R.id.rating_menu).isChecked = false
        menu.findItem(R.id.releaseDate_menu).isChecked = false


    }

    override fun OnMovieItemClicked(position: Int) {
        val intent = Intent(context,IndividualMovieDetail::class.java)

        /*val json = Json.encodeToString(finalList[position])
        intent.putExtra("obj",json)*/

        intent.putExtra("title", tempList[position].title)
        intent.putExtra("poster",tempList[position].poster_path)
        intent.putExtra("rating", tempList[position].vote_average)
        intent.putExtra("releaseDate", tempList[position].release_date)
        intent.putExtra("plot", tempList[position].overview)
        intent.putExtra("id",tempList[position].id)
        intent.putExtra("obj",tempList[position])

        startActivity(intent)

    }

    fun backtoNormalList(){
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        myAdapter = MovieListAdapter(requireContext(), finalList,this)
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.adapter = myAdapter
    }
    override fun onPause() {
        Log.d("OnPause","reached")
        backtoNormalList()
        super.onPause()
    }


}
