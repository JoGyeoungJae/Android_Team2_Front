package com.example.frontend.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.frontend.R
import com.example.frontend.databinding.FragmentMainOneBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainOneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainOneFragment : Fragment() {
    lateinit var binding : FragmentMainOneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainOneBinding.inflate(inflater, container, false)

        return binding.root
    }

}