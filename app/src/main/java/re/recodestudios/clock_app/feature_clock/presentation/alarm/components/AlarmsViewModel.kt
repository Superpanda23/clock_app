package re.recodestudios.clock_app.feature_clock.presentation.alarm.components

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import re.recodestudios.clock_app.feature_clock.domain.respository.AlarmScheduler
import re.recodestudios.clock_app.feature_clock.domain.model.Alarm
import re.recodestudios.clock_app.feature_clock.domain.use_case.AlarmUseCases
import javax.inject.Inject

@HiltViewModel
class AlarmsViewModel @Inject constructor(
    private val alarmUseCases: AlarmUseCases,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _state = mutableStateOf(AlarmState())
    val state: State<AlarmState> = _state

    val _alarms = MutableStateFlow<List<Alarm>>(emptyList())
    val alarms: StateFlow<List<Alarm>> = _alarms.asStateFlow()

    init {
        getAlarms()
    }

    fun onEvent(event: AlarmsEvent) {
        when (event) {
            is AlarmsEvent.DeleteAlarm -> {
                viewModelScope.launch {
                    // Cancel the alarm if it's scheduled
                    alarmUseCases.deleteAlarm(event.alarm)
                    alarmScheduler.cancel(event.alarm)
                    getAlarms()
                }
            }

            is AlarmsEvent.UpdateAlarm -> {
                viewModelScope.launch {
                    alarmUseCases.updateAlarm(event.alarm)
                }
            }
        }
    }

    fun toggleAlarm(alarm: Alarm) {
        viewModelScope.launch {
            val updatedAlarm = alarm.copy(isEnabled = !alarm.isEnabled)
            alarmUseCases.updateAlarm(updatedAlarm)

            val alarmItem = Alarm(
                id = updatedAlarm.id,
                time = updatedAlarm.time,
                daysOfWeek = updatedAlarm.daysOfWeek,
                isEnabled = true
            )

            if (updatedAlarm.isEnabled) {
                // Only schedule if the alarm is enabled
                alarmScheduler.schedule(alarmItem)
            } else {
                // Cancel the alarm when it is disabled
                alarmScheduler.cancel(alarmItem)
            }
        }
    }



    private fun getAlarms() {
        viewModelScope.launch {
            alarmUseCases.getAlarms().collect { alarms ->
                _alarms.value = alarms
            }
        }
    }
}
