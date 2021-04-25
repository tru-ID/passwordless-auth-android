package com.example.tru_phonecheck.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.tru_phonecheck.R
import com.example.tru_phonecheck.fragments.onboarding.screens.ScreenTwo
import kotlinx.android.synthetic.main.fragment_view_pager.view.*

// This is responsible for moving between onboarding screens
class ViewPagerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_view_pager, container, false)
val fragmentList = arrayListOf<Fragment>(
    ScreenTwo()
)

   val adapter = ViewPagerAdapter(
       fragmentList,
       requireActivity().supportFragmentManager,
       lifecycle
   )
        view.viewPager.adapter = adapter
        return view
    }


  }
