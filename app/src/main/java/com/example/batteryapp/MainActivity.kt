package com.example.batteryapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var batteryTV : TextView
    private val connectionReceiver : PowerConnectionReceiver = PowerConnectionReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        batteryTV = findViewById(R.id.chargingTV)
    }

    override fun onStart() {
        super.onStart()
        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(connectionReceiver, iFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectionReceiver)
    }

}