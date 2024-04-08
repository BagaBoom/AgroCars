package com.radchuk.myapplication.ui.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.radchuk.myapplication.data.CarService
import com.radchuk.myapplication.data.InfoCountsObjest
import com.radchuk.myapplication.databinding.FragmentMainBinding
import com.radchuk.myapplication.local.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        getCountObject()
        return binding.root
    }


    private fun getCountObject(){
        ApiClient.apiService.getCountObject().enqueue(object : Callback<InfoCountsObjest> {
            override fun onResponse(call: Call<InfoCountsObjest>, response: Response<InfoCountsObjest>) {
                if (response.isSuccessful) {
                    val infoCountsObjest = response.body()
                    setUIFragment(infoCountsObjest!!)
                    Log.i("MyLog", "Vehicle = ${infoCountsObjest?.countVehcile}")
                    Log.i("MyLog", "Categoty = ${infoCountsObjest?.countCategoty}")
                    Log.i("MyLog", "Driver = ${infoCountsObjest?.countDriver}")
                }else{
                    Log.i("MyLog", "Nen")
                }
            }

            override fun onFailure(call: Call<InfoCountsObjest>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")
            }
        })
    }

    private fun setUIFragment(infoCountsObjest: InfoCountsObjest){
        binding.textViewVehicle.text = infoCountsObjest.countVehcile.toString()
        binding.textViewCateroties.text = infoCountsObjest.countCategoty.toString()
        binding.textViewDriver.text = infoCountsObjest.countDriver.toString()
    }





}