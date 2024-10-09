package re.recodestudios.clock_app.feature_clock.presentation.alarm.components

import re.recodestudios.clock_app.feature_clock.domain.model.Alarm

sealed class AlarmsEvent {
    data class DeleteAlarm(val alarm: Alarm): AlarmsEvent()
    data class UpdateAlarm(val alarm: Alarm): AlarmsEvent()
}