package com.conamobile.newfoodapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.conamobile.newfoodapp.R
import com.conamobile.newfoodapp.adapter.HomeAdapter
import com.conamobile.newfoodapp.databinding.FragmentOrdersBinding
import com.conamobile.newfoodapp.model.FoodModel
import com.conamobile.newfoodapp.networking.RetrofitHttp
import com.conamobile.newfoodapp.utils.viewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersFragment : BaseFragment(R.layout.fragment_orders) {
    private var foodList = ArrayList<FoodModel>()
    private val adapter by lazy { HomeAdapter() }
    private val binding by viewBinding { FragmentOrdersBinding.bind(it) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        loadFood()
        installRecycler()
        recyclerItemTouch()
    }

    private fun Boolean.goToFragment(fragment: Fragment) {
        val transaction: FragmentTransaction =
            requireActivity().supportFragmentManager.beginTransaction()
        if (this) {
            transaction.addToBackStack(null)
        }
        transaction.add(R.id.container, fragment).commit()
    }

    private fun installRecycler() {
        binding.apply {
            backBtn.setOnClickListener {
                false.goToFragment(HomeFragment())
            }
        }
        binding.recyclerView.adapter = adapter
        adapter.submitList(foodList)
    }

    private fun loadFood() {
        RetrofitHttp.posterService.loadAllFood()
            .enqueue(object : Callback<ArrayList<FoodModel>> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ArrayList<FoodModel>>,
                    response: Response<ArrayList<FoodModel>>,
                ) {
                    if (response.body() != null) {
                        foodList.addAll(response.body()!!)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<ArrayList<FoodModel>>, t: Throwable) {
                    "${t.message}".toaster()
                }
            })
    }

    private fun recyclerItemTouch() {
        adapter.itemClick = {
            startDetail()
        }
    }
}