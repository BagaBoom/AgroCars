package com.radchuk.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.radchuk.myapplication.R
import com.radchuk.myapplication.data.Driver

class DriverAdapter(private val listener: OnDriverInteractionListener) : RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    private var drivers: List<Driver> = emptyList()

    fun updateData(newVehicles: List<Driver>) {
        drivers = newVehicles
        notifyDataSetChanged()
    }

    inner class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val makeTextFirstName: TextView = itemView.findViewById(R.id.textViewFirstName)
        private val makeTextLastName: TextView = itemView.findViewById(R.id.textViewLastName)
        private val makeTextLicenseNumber: TextView = itemView.findViewById(R.id.textViewLicenseNumber)
        private val makeTextStatusDriver: TextView = itemView.findViewById(R.id.textViewStatusDriver)
        private val makeTextVehicle: TextView = itemView.findViewById(R.id.textViewVehicle)
        private val editButton: ImageButton = itemView.findViewById(R.id.imageButtonEdit)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.imageButtonDelete)

        fun bind(driver: Driver) {
            makeTextFirstName.text = driver.firstName
            makeTextLastName.text = driver.lastName
            makeTextLicenseNumber.text = driver.licenseNumber
            makeTextStatusDriver.text = driver.statusDriver
            val model = driver.vehicle?.registrationNumber
            if (model == null){
                makeTextVehicle.text = "Без транспорту"
            }else{
                makeTextVehicle.text = driver.vehicle.model
            }


            editButton.setOnClickListener {
                listener.onEditClicked(driver)
            }

            deleteButton.setOnClickListener {
                listener.onDeleteClicked(driver)
            }
        }
    }

    interface OnDriverInteractionListener {
        fun onEditClicked(driver: Driver)
        fun onDeleteClicked(driver: Driver)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_driver, parent, false)
        return DriverViewHolder(view)
    }

    override fun getItemCount(): Int {
        return drivers.size
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = drivers[position]
        holder.bind(driver)
    }


}
