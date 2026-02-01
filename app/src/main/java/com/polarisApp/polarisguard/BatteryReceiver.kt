package com.polarisApp.polarisguard
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

class BatteryReceiver (
    private val onLowBattery: (level: Int) -> Unit

) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

            val batteryPct = (level * 100) / scale
            if (batteryPct <= 15) {
                onLowBattery(batteryPct)
            }
        }
    }
}




