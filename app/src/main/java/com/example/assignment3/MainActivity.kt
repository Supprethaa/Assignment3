package com.example.assignment3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), AddReminderDialogListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewNoReminders: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var toolbar: Toolbar
    private lateinit var reminderAdapter: ReminderAdapter

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views using findViewById
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recyclerView)
        textViewNoReminders = findViewById(R.id.textViewNoReminders)
        fab = findViewById(R.id.fab)

        // Setup Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationIcon(R.drawable.baseline_pets_24) // Replace with your actual icon
        supportActionBar?.title = "Pet Pal"


        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        reminderAdapter = ReminderAdapter(mutableListOf()) // Initially empty list
        recyclerView.adapter = reminderAdapter

        // Check if there are any reminders
        checkReminders()

        // Handle FAB click
        fab.setOnClickListener {
            // Open DialogFragment to add a new reminder
            val dialog = AddReminderDialogFragment()
            dialog.show(supportFragmentManager, "AddReminderDialogFragment")
        }

        // Request notification permission
        requestNotificationPermission()
    }

    // Inflate the menu options in the toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_options, menu)
        return true
    }

    // Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.view_all_reminders -> {
                // Handle "View All Reminders" click

                Toast.makeText(this, "View All Reminders clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.view_pet_profile -> {
                // Handle "View Your Pet Profile" click
                val intent = Intent(this, PetProfile::class.java)
                startActivity(intent)
                Toast.makeText(this, "View Your Pet Profile clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Function to check reminders and toggle visibility
    private fun checkReminders() {
        if (reminderAdapter.itemCount == 0) {
            textViewNoReminders.visibility = android.view.View.VISIBLE
        } else {
            textViewNoReminders.visibility = android.view.View.GONE
        }
    }

    // Implement the AddReminderDialogListener interface method
    override fun onAddReminder(reminder: Reminder) {
        reminderAdapter.addReminder(reminder)
        checkReminders() // Check and update the "No recent reminders" message
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            NOTIFICATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can show notifications
                    Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    // Permission denied, you may want to inform the user that they won't receive notifications
                    Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}