package com.example.assignment3

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class ReminderDetailActivity : AppCompatActivity() {

    companion object {
        private const val CHANNEL_ID = "ReminderChannel"
        private const val NOTIFICATION_ID = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_detail)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val category = intent.getStringExtra("category") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val date = intent.getStringExtra("date") ?: ""
        val time = intent.getStringExtra("time") ?: ""

        findViewById<TextView>(R.id.textViewDetailCategory).text = "Category: $category"
        findViewById<TextView>(R.id.textViewDetailDescription).text = "Description: $description"
        findViewById<TextView>(R.id.textViewDetailDate).text = "Date: $date"
        findViewById<TextView>(R.id.textViewDetailTime).text = "Time: $time"

        createNotificationChannel()
        showNotification(category, description)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Handle back button press
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val soundUri = Uri.parse("android.resource://${packageName}/${R.raw.custom}")
            val attributes = android.media.AudioAttributes.Builder()
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                setSound(soundUri, attributes) // Set custom sound with attributes
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(category: String, description: String) {
        val soundUri = Uri.parse("android.resource://${packageName}/${R.raw.custom}")

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_pets_24) // Make sure this drawable exists
            .setContentTitle(category)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(soundUri) // Set the custom sound for the notification
            .setAutoCancel(true)

        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}