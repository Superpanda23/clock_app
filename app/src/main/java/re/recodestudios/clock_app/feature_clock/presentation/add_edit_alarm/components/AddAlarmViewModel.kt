package re.recodestudios.clock_app.feature_clock.presentation.add_edit_alarm.components

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import re.recodestudios.clock_app.feature_clock.domain.respository.AlarmScheduler
import re.recodestudios.clock_app.feature_clock.domain.model.Alarm
import re.recodestudios.clock_app.feature_clock.domain.model.InvalidAlarmException
import re.recodestudios.clock_app.feature_clock.domain.use_case.AlarmUseCases
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddAlarmViewModel @Inject constructor(
    private val alarmUseCases: AlarmUseCases,
    private val alarmScheduler: AlarmScheduler
): ViewModel() {
    // State for list of alarms
    private val _alarms = mutableStateOf(listOf<Alarm>())
    val alarms: State<List<Alarm>> = _alarms

    private val _pickedTime = MutableStateFlow(LocalTime.now())
    val pickedTime: StateFlow<LocalTime> = _pickedTime.asStateFlow()

    // State for selected days
    private val _selectedDays = MutableStateFlow(emptySet<DayOfWeek>())
    val selectedDays: StateFlow<Set<DayOfWeek>> = _selectedDays.asStateFlow()


    // State for selected time
    private val _time = MutableStateFlow(LocalTime.now())
    val time: StateFlow<LocalTime> = _time.asStateFlow()


    // Initialize ViewModel by loading alarms
    init {
        getAlarms() // Fetch and observe alarms
    }

    private fun getAlarms() {
        viewModelScope.launch {
            alarmUseCases.getAlarms().collect { alarms ->
                _alarms.value = alarms
                // Schedule each enabled alarm
                alarms.filter { it.isEnabled }.forEach { alarm ->
                    scheduleAlarm(alarm) // Schedule each alarm if enabled
                }
            }
        }
    }

    // Handle events such as saving or updating alarms
    fun onEvent(event: AddAlarmEvent) {
        when(event) {
            is AddAlarmEvent.ChosenTime -> {
                // Update the time chosen for the alarm
                _pickedTime.value = event.time
            }
            is AddAlarmEvent.ChosenDays -> {
                _selectedDays.value = event.daysOfWeek // Update selectedDays
            }

            AddAlarmEvent.SaveAlarm -> {
                viewModelScope.launch {
                    try {
                        val newAlarm = Alarm(
                            id = UUID.randomUUID().toString(),
                            daysOfWeek = _selectedDays.value,
                            time = pickedTime.value,
                            isEnabled = true // You can modify based on default logic
                        )
                        alarmUseCases.addAlarm(newAlarm)
                    } catch (e: InvalidAlarmException) {
                        _eventFlow.emit(UiEvent.ShowSnackbar(e.message ?: "Couldn't save alarm"))
                    }
                }
            }
            is AddAlarmEvent.ToggleEnabled -> {
                viewModelScope.launch {
                    val updatedAlarm = event.alarm.copy(isEnabled = !event.alarm.isEnabled)
                    alarmUseCases.updateAlarm(updatedAlarm)

                    if (updatedAlarm.isEnabled) {
                        scheduleAlarm(updatedAlarm)
                    } else {
                        cancelAlarm(updatedAlarm)
                    }
                }
            }
        }
    }

    // Schedule an alarm using the AlarmScheduler
   private fun scheduleAlarm(alarm: Alarm) {
        val alarmItem = Alarm(
            id = alarm.id,
            time = alarm.time,
            daysOfWeek = alarm.daysOfWeek,
            isEnabled = alarm.isEnabled
        )
        alarmScheduler.schedule(alarmItem) // Schedule the alarm
    }

    // Cancel an alarm using the AlarmScheduler
    private fun cancelAlarm(alarm: Alarm) {
        val alarmItem = Alarm(
            id = alarm.id,
            time = alarm.time,
            daysOfWeek = alarm.daysOfWeek,
            isEnabled = alarm.isEnabled
        )
        alarmScheduler.cancel(alarmItem) // Cancel the alarm
    }

    // Flow for UI events like showing a snackbar
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveAlarm : UiEvent()
    }
}

