package com.example.assignment3

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Define a data class for Reminder (assuming you have a similar model)
data class Reminder(
    val category: String,
    val description: String,
    val date: String,
    val time: String
)

class ReminderAdapter(private val reminderList: MutableList<Reminder>) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    class ReminderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryTextView: TextView = view.findViewById(R.id.textViewCategory)
        val dateTextView: TextView = view.findViewById(R.id.textViewDate)
        val timeTextView: TextView = view.findViewById(R.id.textViewTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminderList[position]
        holder.categoryTextView.text = "Category: ${reminder.category}"
        holder.dateTextView.text = "Date: ${reminder.date}"
        holder.timeTextView.text = "Time: ${reminder.time}"

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ReminderDetailActivity::class.java).apply {
                putExtra("category", reminder.category)
                putExtra("description", reminder.description)
                putExtra("date", reminder.date)
                putExtra("time", reminder.time)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }

    fun addReminder(reminder: Reminder) {
        reminderList.add(reminder)
        notifyItemInserted(reminderList.size - 1)
    }
}