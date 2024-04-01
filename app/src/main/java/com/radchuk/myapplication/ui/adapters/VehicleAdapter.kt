package com.radchuk.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.radchuk.myapplication.R
import com.radchuk.myapplication.data.Vehicle

class VehicleAdapter(private val listener: OnVehicleInteractionListener) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    private var vehicles: List<Vehicle> = emptyList()

    fun updateData(newVehicles: List<Vehicle>) {
        vehicles = newVehicles
        notifyDataSetChanged()
    }

    inner class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val makeTextView: TextView = itemView.findViewById(R.id.textViewMake)
        private val modelTextView: TextView = itemView.findViewById(R.id.textViewModel)
        private val yearTextView: TextView = itemView.findViewById(R.id.textViewYear)
        private val registrationNumberTextView: TextView = itemView.findViewById(R.id.textViewRegistrationNumber)
        private val currentLocationTextView: TextView = itemView.findViewById(R.id.textViewCurrentLocation)
        private val fuelTypeTextView: TextView = itemView.findViewById(R.id.textViewTypeFuel)
        private val tankVolumeTextView: TextView = itemView.findViewById(R.id.textViewTankVolume)
        private val engineVolumeTextView: TextView = itemView.findViewById(R.id.textViewEngineVolume)
        private val currentStateTextView: TextView = itemView.findViewById(R.id.textViewCurrentState)
        private val categotyNameTextView: TextView = itemView.findViewById(R.id.textViewCaterotyName)
        private val editButton: ImageButton = itemView.findViewById(R.id.imageButtonEdit)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.imageButtonDelete)

        fun bind(vehicle: Vehicle) {
            makeTextView.text = vehicle.make
            modelTextView.text = vehicle.model
            yearTextView.text = vehicle.year.toString()
            registrationNumberTextView.text = vehicle.registrationNumber
            currentLocationTextView.text = vehicle.currentLocation
            fuelTypeTextView.text = vehicle.fuelType
            tankVolumeTextView.text = vehicle.tankVolume.toString()
            engineVolumeTextView.text = vehicle.engineCapacity.toString()
            currentStateTextView.text = vehicle.currentStatus
            categotyNameTextView.text = vehicle.categoryId?.name

            editButton.setOnClickListener {
                listener.onEditClicked(vehicle)
            }

            deleteButton.setOnClickListener {
                listener.onDeleteClicked(vehicle)
            }
        }
    }

    interface OnVehicleInteractionListener {
        fun onEditClicked(vehicle: Vehicle)
        fun onDeleteClicked(vehicle: Vehicle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicles[position]
        holder.bind(vehicle)
    }

    override fun getItemCount(): Int {
        return vehicles.size
    }
}
