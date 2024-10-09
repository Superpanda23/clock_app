package re.recodestudios.clock_app.feature_clock.data.scheduler.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import re.recodestudios.clock_app.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        context?.let { ctx ->
            createNotificationChannel(ctx) // Create the channel if it doesn't exist

            // Build the notification
            val builder = NotificationCompat.Builder(ctx, "alarm_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Alarm Triggered")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI) // Set the alarm sound
                .setAutoCancel(true) // Dismiss notification on click

            // Display the notification only if permission is granted
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(ctx,android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                NotificationManagerCompat.from(ctx).notify(1, builder.build())
            } else {
                Log.e("AlarmReceiver", "Notification permission not granted.")
            }

            Log.d("AlarmReceiver", "Alarm triggered with message: $message")
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "alarm_id"
            val channelName = "Alarm Notifications"
            val channelDescription = "Channel for alarm notifications"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = channelDescription
                enableLights(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 1000, 500, 1000)
                setSound(android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI, Notification.AUDIO_ATTRIBUTES_DEFAULT)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}



