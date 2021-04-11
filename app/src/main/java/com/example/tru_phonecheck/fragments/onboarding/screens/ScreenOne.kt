package com.example.tru_phonecheck.fragments.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2

import com.example.tru_phonecheck.R
import kotlinx.android.synthetic.main.fragment_screen_one.view.*


class ScreenOne : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_screen_one, container, false)
        // navigate to the next onboarding screen onClick
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        view.screenone_button.setOnClickListener {
            viewPager?.currentItem = 1
        }

        return view
    }


}
