package com.example.batteryapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

class PowerConnectionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: return
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: return
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1).toFloat();
        val batteryPercentage = level * 100 / scale;
        val isCharging =
            status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
        val charging = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        val usbCharging = charging == BatteryManager.BATTERY_PLUGGED_USB
        val acCharge = charging == BatteryManager.BATTERY_PLUGGED_AC
        val batteryStatus =
            if (usbCharging) "Battery status : $batteryPercentage %\n charging via usb"
            else if (acCharge) "Battery status : $batteryPercentage %\n charging via ac"
            else "Battery status : $batteryPercentage % and not charging"
        if(context is MainActivity){
            context.batteryTV.text = batteryStatus
        }
    }
}