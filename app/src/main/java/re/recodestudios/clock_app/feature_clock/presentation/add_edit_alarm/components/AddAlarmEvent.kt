package re.recodestudios.clock_app.feature_clock.presentation.add_edit_alarm.components

import re.recodestudios.clock_app.feature_clock.domain.model.Alarm
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

sealed class AddAlarmEvent {
    data class ChosenTime(val time: LocalTime) : AddAlarmEvent()
    data class ChosenDays(val daysOfWeek: Set<DayOfWeek>) : AddAlarmEvent()
    data class ToggleEnabled(val alarm: Alarm) : AddAlarmEvent()
    object SaveAlarm: AddAlarmEvent()
}