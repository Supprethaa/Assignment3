package com.example.assignment3

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import java.util.*

class AddReminderDialogFragment : DialogFragment() {
    private lateinit var listener: AddReminderDialogListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_reminder, container, false)

        val spinnerCategory: Spinner = view.findViewById(R.id.spinnerCategory)
        val editTextDescription: EditText = view.findViewById(R.id.editTextDescription)
        val editTextDate: EditText = view.findViewById(R.id.editTextDate)
        val editTextTime: EditText = view.findViewById(R.id.editTextTime)
        val buttonAdd: Button = view.findViewById(R.id.addButton)
        val buttonCancel: Button = view.findViewById(R.id.cancelButton)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.reminder_categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter
        }

        editTextDate.setOnClickListener { showDatePicker(editTextDate) }
        editTextTime.setOnClickListener { showTimePicker(editTextTime) }

        buttonAdd.setOnClickListener {
            val category = spinnerCategory.selectedItem.toString()
            val description = editTextDescription.text.toString()
            val selectedDate = editTextDate.text.toString()
            val selectedTime = editTextTime.text.toString()

            if (category.isNotEmpty() && description.isNotEmpty() && selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                val reminder = Reminder(category, description, selectedDate, selectedTime)
                listener.onAddReminder(reminder)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        buttonCancel.setOnClickListener { dismiss() }

        return view
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val selectedDate = "$day/${month + 1}/$year"
                editText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                val selectedTime = String.format("%02d:%02d", hour, minute)
                editText.setText(selectedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // 24-hour format
        ).show()
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        listener = context as? AddReminderDialogListener
            ?: throw ClassCastException("$context must implement AddReminderDialogListener")
    }
}