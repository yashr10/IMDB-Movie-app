package com.example.imdb

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.imdb.moviefragments.latestMovieFragment
import com.example.imdb.moviefragments.TopRatedFragment
import com.example.imdb.moviefragments.upcomingMovieFragment

class ViewPagerAdapter(fragmentManager : FragmentManager, lifecycle : Lifecycle ) : FragmentStateAdapter(fragmentManager , lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {

      return  when (position){

            0-> latestMovieFragment()
            1-> TopRatedFragment()
            2-> upcomingMovieFragment()

          else -> Fragment()
      }
    }
}