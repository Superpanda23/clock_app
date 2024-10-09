package re.recodestudios.clock_app.feature_clock.data.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import re.recodestudios.clock_app.feature_clock.data.scheduler.receiver.AlarmReceiver
import re.recodestudios.clock_app.feature_clock.domain.model.Alarm
import re.recodestudios.clock_app.feature_clock.domain.respository.AlarmScheduler
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class AndroidAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: Alarm) {
        if (!item.isEnabled) {
            cancel(item)
            Log.d("AndroidAlarmScheduler", "Alarm is disabled and will not be scheduled.")
            return
        }

        val intent = createIntent(item)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val now = ZonedDateTime.now(ZoneId.systemDefault())
        val alarmTime = calculateNextAlarmTime(now, item.time, item.daysOfWeek)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )

        Log.d("AndroidAlarmScheduler", "Alarm scheduled for: ${item.time}, Epoch millis: $alarmTime, Days: ${item.daysOfWeek}")
    }

    private fun calculateNextAlarmTime(
        now: ZonedDateTime,
        alarmTime: LocalTime,
        daysOfWeek: Set<DayOfWeek>
    ): Long {
        // Ensure seconds are zero in the alarm time
        val adjustedAlarmTime = alarmTime.withSecond(0).withNano(0)

        if (daysOfWeek.isEmpty()) {
            // If no days are selected, schedule for the next occurrence of the time (default behavior)
            var nextAlarmTime = now.with(adjustedAlarmTime) // Set alarm time for today, with seconds set to 0
            if (nextAlarmTime.isBefore(now)) {
                nextAlarmTime = nextAlarmTime.plusDays(1) // Move to the next day if the time has passed
            }
            return nextAlarmTime.toInstant().toEpochMilli()
        }

        // Find the next matching day for the alarm
        var daysUntilNextAlarm = Int.MAX_VALUE // Start with a high value

        daysOfWeek.forEach { dayOfWeek ->
            val currentDay = now.dayOfWeek
            var daysUntilThisDay = (dayOfWeek.value - currentDay.value + 7) % 7

            // If it's the same day but the time has passed, move to the next occurrence
            if (daysUntilThisDay == 0 && adjustedAlarmTime.isBefore(now.toLocalTime())) {
                daysUntilThisDay = 7
            }

            if (daysUntilThisDay < daysUntilNextAlarm) {
                daysUntilNextAlarm = daysUntilThisDay
            }
        }

        // Set the next alarm day and time, ensuring seconds are zero
        val nextAlarmDay = now.plusDays(daysUntilNextAlarm.toLong()).with(adjustedAlarmTime)
        return nextAlarmDay.toInstant().toEpochMilli()
    }




    override fun cancel(item: Alarm) {
        val intent = createIntent(item)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.hashCode(),  // Use the same ID for both schedule and cancel
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Ensure flag consistency
        )

        alarmManager.cancel(pendingIntent) // Cancel the exact same PendingIntent

        Log.d("AndroidAlarmScheduler", "Alarm canceled for id: ${item.id}")
    }

    private fun createIntent(item: Alarm): Intent {
        return Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", "Alarm triggered")
        }
    }
}
