package com.radchuk.myapplication.ui.dialogfragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.radchuk.myapplication.data.CarCategory
import com.radchuk.myapplication.data.Vehicle

class DeleteConfirmationDialogFragment(private val category: CarCategory, private val listener: OnDeleteConfirmationListener) : DialogFragment() {

    interface OnDeleteConfirmationListener {
        fun onConfirmDelete(category: CarCategory)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Delete Confirmation")
            .setMessage("Are you sure you want to delete ${category.name}?")
            .setPositiveButton("Yes") { _, _ ->
                listener.onConfirmDelete(category)
            }
            .setNegativeButton("No", null)
            .create()
    }


}
