package com.example.imdb.ui.notifications

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imdb.API.Detail
import com.example.imdb.Adapters.FavMovieListAdapter
import com.example.imdb.IndividualMovieDetail
import com.example.imdb.R
import com.example.imdb.databinding.FragmentNotificationsBinding
import com.example.imdb.onMovieClickListener
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class NotificationsFragment : Fragment() , onMovieClickListener {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var mAuth : FirebaseAuth
    val db = Firebase.firestore

    lateinit var myAdapter: FavMovieListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var listOfMap : ArrayList <Map<String,*>>



    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        linearLayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = linearLayoutManager

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        if (currentUser == null){
            binding.recyclerView.isVisible = false
            binding.textView.text = "Login to Add movies to favourites"
        }
        else{


            val docRef = db.collection("users").document(currentUser.uid)

            listOfMap = arrayListOf()
            docRef.get().addOnSuccessListener {

                if (it.exists() && it != null){

                    listOfMap = it.get("favourite") as ArrayList <Map<String,*>>

                    Log.d("notif","snapshot is not null")
                }else{
                    Log.d("failure", "to add data")
                }
                if (listOfMap.isEmpty()){

                    Log.d("notif","map is empty")
                    binding.recyclerView.isVisible = false
                    binding.textView.text = "You ave not added any movies to favourites"
                }else{
                    binding.textView.isVisible = false

                    myAdapter = context?.let { FavMovieListAdapter(it,listOfMap,this) }!!
                    binding.recyclerView.adapter = myAdapter

                }

            }

            Log.d("map",listOfMap.toString())


        }


        return root
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun OnMovieItemClicked(position: Int) {
        val intent = Intent(context, IndividualMovieDetail::class.java)


       /* intent.putExtra("title", listOfMap[position]["title"].toString())
        intent.putExtra("poster",listOfMap[position]["poster_path"].toString())
        intent.putExtra("rating", listOfMap[position]["vote_average"].toString())
        intent.putExtra("releaseDate", listOfMap[position]["title"].toString())
        intent.putExtra("plot",listOfMap[position]["title"].toString())
        intent.putExtra("id",listOfMap[position]["id"].toString())
        //intent.putExtra("obj",listOfMap[position])


        startActivity(intent)*/
    }
}

