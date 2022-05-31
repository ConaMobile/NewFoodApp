package com.conamobile.newfoodapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.conamobile.newfoodapp.R
import com.conamobile.newfoodapp.adapter.HomeAdapter
import com.conamobile.newfoodapp.databinding.FragmentHomeBinding
import com.conamobile.newfoodapp.model.FoodModel
import com.conamobile.newfoodapp.networking.RetrofitHttp
import com.conamobile.newfoodapp.utils.viewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private var foodList = ArrayList<FoodModel>()
    private val adapter by lazy { HomeAdapter() }
    private val binding by viewBinding { FragmentHomeBinding.bind(it) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFood()
        installRecycler()
        recyclerItemTouch()
    }

    private fun recyclerItemTouch() {
        adapter.itemClick = {
            startDetail()
        }
    }

    private fun installRecycler() {
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
                        Log.d("@@@", "${response.body()}")
                    }
                }

                override fun onFailure(call: Call<ArrayList<FoodModel>>, t: Throwable) {
                    "${t.message}".toaster()
                    Log.d("@@@", "${t.message}")
                }
            })
    }

}