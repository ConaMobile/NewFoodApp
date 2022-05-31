package com.conamobile.newfoodapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.PagerSnapHelper
import com.conamobile.newfoodapp.adapter.DetailImageAdapter
import com.conamobile.newfoodapp.databinding.ActivityDetailBinding
import com.conamobile.newfoodapp.model.DetailModel
import com.conamobile.newfoodapp.utils.Transfer

class DetailActivity : AppCompatActivity() {
    private val detailImageAdapter by lazy { DetailImageAdapter() }
    private var photosList = ArrayList<DetailModel>()
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initViews()
    }

    private fun initViews() {
        backManager()
        installRecyclerView()
        carouselRecyclerManager()
    }

    private fun backManager() {
        binding.apply {
            backBtn.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun installRecyclerView() {
        binding.apply {
            detailRecyclerView.adapter = detailImageAdapter
            PagerSnapHelper().attachToRecyclerView(detailRecyclerView)
        }
    }

    private fun carouselRecyclerManager() {
        photosList.add(DetailModel(Transfer.foodImage))
        photosList.add(DetailModel("https://realfood.tesco.com/media/images/RFO-1400x919-classic-chocolate-mousse-69ef9c9c-5bfb-4750-80e1-31aafbd80821-0-1400x919.jpg"))
        photosList.add(DetailModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRr0pBKhy3S3Ad_JS7sUDuUWrPOqMpJk-nFSA&usqp=CAU"))
        detailImageAdapter.submitList(photosList)
        loadNext()
    }

    private fun loadNext() {
        binding.apply {
            foodNameTxt.text = Transfer.foodName
            foodDescriptionTxt.text = Transfer.foodDescription
        }
    }
}