package com.radchuk.myapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.radchuk.myapplication.data.CarCategory
import com.radchuk.myapplication.databinding.FragmentCategotiesCarBinding
import com.radchuk.myapplication.local.ApiClient
import com.radchuk.myapplication.ui.activity.LoginActivity
import com.radchuk.myapplication.ui.adapters.CarCategoryAdapter
import com.radchuk.myapplication.ui.dialogfragment.DeleteConfirmationDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.app.AlertDialog

class CategotiesCarFragment : Fragment(),CarCategoryAdapter.OnCategoryInteractionListener {

    private lateinit var binding: FragmentCategotiesCarBinding
    private lateinit var adapter: CarCategoryAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategotiesCarBinding.inflate(inflater,container,false)



        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = CarCategoryAdapter(this)
        binding.recyclerView.adapter = adapter


        updateListCarCategory()



        return binding.root
    }

    override fun onEditClicked(category: CarCategory) {
        showEditDialog(category)
    }
    private fun showEditDialog(category: CarCategory) {
        val editText = EditText(requireContext())
        editText.setText(category.name)

        AlertDialog.Builder(requireContext())
            .setTitle("Name Category")
            .setView(editText)

            .setPositiveButton("Save") { dialog, _ ->
                val newName = editText.text.toString()
                if (newName.isNotBlank()) {
                    editCategory(category.id!!, newName)
                    dialog.dismiss()
                } else {
                    editText.error = "Category name cannot be empty"
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun editCategory(categoryId: Long, newName: String) {
        val updatedCategory = CarCategory(categoryId, newName)
        ApiClient.apiService.updateCategory(categoryId, updatedCategory).enqueue(object : Callback<CarCategory> {
            override fun onResponse(call: Call<CarCategory>, response: Response<CarCategory>) {
                if (response.isSuccessful) {
                    // Обработайте успешное редактирование
                    updateListCarCategory()
                } else {
                    Log.i("MyLog", "Failed to edit category: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<CarCategory>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")
            }
        })
    }

    override fun onDeleteClicked(category: CarCategory) {
        //showDeleteConfirmationDialog(category)
        val dialog = DeleteConfirmationDialogFragment(category, object : DeleteConfirmationDialogFragment.OnDeleteConfirmationListener {
            override fun onConfirmDelete(category: CarCategory) {
                Log.d("MyLog", "id = ${category.id!!}")
                deleteCategory(category.id)
            }
        })
        dialog.show(requireActivity().supportFragmentManager, "DeleteConfirmationDialog")

    }
    private fun updateListCarCategory(){
        ApiClient.apiService.getCarCategories().enqueue(object : Callback<List<CarCategory>> {
            override fun onResponse(call: Call<List<CarCategory>>, response: Response<List<CarCategory>>) {
                if (response.isSuccessful) {
                    val categories = response.body()
                    adapter.updateData(categories ?: emptyList())
                } else {
                    startActivity(Intent(requireActivity(),LoginActivity::class.java))
                    requireActivity().finish()
                    Log.i("MyLog", "Failed to fetch car categories: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<CarCategory>>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")

            }
        })
    }
    private fun deleteCategory(categoryId: Long) {
        ApiClient.apiService.deleteCategory(categoryId).enqueue(object : Callback<CarCategory> {
            override fun onResponse(call: Call<CarCategory>, response: Response<CarCategory>) {
                if (response.isSuccessful) {
                    updateListCarCategory()
                } else {
                    Log.i("MyLog", "Failed to delete category: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<CarCategory>, t: Throwable) {
                Log.i("MyLog", "Network request failed: ${t.message}")
            }
        })
    }







}