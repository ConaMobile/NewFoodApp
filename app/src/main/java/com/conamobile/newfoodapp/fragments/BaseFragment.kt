package com.conamobile.newfoodapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.conamobile.newfoodapp.activity.DetailActivity

abstract class BaseFragment(private val layoutRes: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(layoutRes, container, false)
    }

    fun String.toaster() {
        Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT).show()
    }

    fun startDetail() {
        startActivity(Intent(context, DetailActivity::class.java))
    }
}