package com.radchuk.myapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.radchuk.myapplication.data.CarCategory
import com.radchuk.myapplication.databinding.FragmentCategotiesCarBinding
import com.radchuk.myapplication.local.ApiClient
import com.radchuk.myapplication.ui.adapters.CarCategoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategotiesCarFragment : Fragment() {

    private lateinit var binding: FragmentCategotiesCarBinding
    private lateinit var adapter: CarCategoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategotiesCarBinding.inflate(inflater,container,false)

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = CarCategoryAdapter()
        binding.recyclerView.adapter = adapter


        ApiClient.apiService.getCarCategories().enqueue(object : Callback<List<CarCategory>> {
            override fun onResponse(call: Call<List<CarCategory>>, response: Response<List<CarCategory>>) {
                if (response.isSuccessful) {
                    val categories = response.body()
                    adapter.updateData(categories ?: emptyList())
                } else {
                    Log.i("MyLog", "Failed to fetch car categories: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<CarCategory>>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")

            }
        })



        return binding.root
    }



}