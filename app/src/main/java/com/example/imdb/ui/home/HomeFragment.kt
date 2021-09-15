package com.example.imdb.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.imdb.LoginActivity
import com.example.imdb.R
import com.example.imdb.ViewPagerAdapter
import com.example.imdb.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


       val tabLayout = binding.tabLayout
        val viewPager2 = binding.viewPager

        val adapter = activity?.let { ViewPagerAdapter(it.supportFragmentManager, lifecycle) }

        viewPager2.adapter = adapter
        TabLayoutMediator(tabLayout,viewPager2){tab,position ->

            when(position){

                0-> tab.text = "Latest"
                1-> tab.text = "Top Rated"
                2-> tab.text = "Upcoming"
            }
        }.attach()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}