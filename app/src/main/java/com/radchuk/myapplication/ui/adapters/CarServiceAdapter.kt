package com.radchuk.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.radchuk.myapplication.R
import com.radchuk.myapplication.data.CarService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CarServiceAdapter(private val listener: OnCarServiceInteractionListener) : RecyclerView.Adapter<CarServiceAdapter.CarServiceViewHolder>() {

    private var carServices: List<CarService> = emptyList()
    fun updateData(newCarService: List<CarService>) {
        carServices = newCarService
        notifyDataSetChanged()
    }

    inner class CarServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val date: TextView = itemView.findViewById(R.id.textViewDateService)
        private val typeService: TextView = itemView.findViewById(R.id.textViewTypeServise)
        private val registrationNumber: TextView = itemView.findViewById(R.id.textViewVehicle)

        private val editButton: ImageButton = itemView.findViewById(R.id.imageButtonEdit)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.imageButtonDelete)
        fun bind(carService: CarService) {
            date.text = convertDateTimeFormat(carService.serviceDate!!)
            typeService.text = carService.serviceType
            registrationNumber.text = carService.vehicle?.registrationNumber

            editButton.setOnClickListener {
                listener.onEditClicked(carService)
            }

            deleteButton.setOnClickListener {
                listener.onDeleteClicked(carService)
            }
        }
    }

    interface OnCarServiceInteractionListener {
        fun onEditClicked(carService: CarService)
        fun onDeleteClicked(carService: CarService)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car_service, parent, false)
        return CarServiceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return carServices.size
    }

    override fun onBindViewHolder(holder: CarServiceViewHolder, position: Int) {
        val carService = carServices[position]
        holder.bind(carService)
    }

    fun convertDateTimeFormat(inputDateTime: String): String? {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val date = inputFormat.parse(inputDateTime)
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
