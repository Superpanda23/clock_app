package re.recodestudios.clock_app.feature_clock.presentation.alarm.components

import re.recodestudios.clock_app.feature_clock.domain.model.Alarm

data class AlarmState(
    val alarms: List<Alarm> = emptyList()
)
