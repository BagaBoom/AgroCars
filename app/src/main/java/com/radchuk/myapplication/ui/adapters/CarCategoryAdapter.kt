package com.radchuk.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.radchuk.myapplication.R
import com.radchuk.myapplication.data.CarCategory

class CarCategoryAdapter : RecyclerView.Adapter<CarCategoryAdapter.CategoryViewHolder>() {

    private var categories: List<CarCategory> = emptyList()

    fun updateData(newCategories: List<CarCategory>) {
        categories = newCategories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryNameTextView: TextView = itemView.findViewById(R.id.category_name)

        fun bind(category: CarCategory) {
            categoryNameTextView.text = category.name
        }
    }
}
