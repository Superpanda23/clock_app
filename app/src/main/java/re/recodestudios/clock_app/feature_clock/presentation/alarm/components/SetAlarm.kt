package re.recodestudios.clock_app.feature_clock.presentation.alarm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import re.recodestudios.clock_app.feature_clock.domain.model.Alarm
import re.recodestudios.clock_app.feature_clock.presentation.add_edit_alarm.components.AddAlarmEvent
import re.recodestudios.clock_app.feature_clock.presentation.add_edit_alarm.components.AddAlarmViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SetAlarm(
    viewModel: AlarmsViewModel = hiltViewModel(),
    addViewModel: AddAlarmViewModel = hiltViewModel()
) {
    val timeDialogState = rememberMaterialDialogState()
    val daysDialogState = rememberMaterialDialogState() // State for days dialog
    var selectedDays by remember { mutableStateOf(emptySet<DayOfWeek>()) } // Store the picked date
    var pickedTime by remember { mutableStateOf(LocalTime.now()) }   // Store the picked time

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Set Alarm", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {
                   daysDialogState.show() // First, show the date picker
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Alarm")
                }
            }
        }
        AlarmList(viewModel = viewModel)
    }

    // Date Picker Dialog
    MaterialDialog(
        dialogState = daysDialogState,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        buttons = {
            positiveButton(text = "Next") {
                daysDialogState.hide()  // Close date picker
                timeDialogState.show()  // Show the time picker next
            }
            negativeButton(text = "Cancel") { }
        }
    ) {
        Column {
            DayOfWeek.entries.forEach { dayOfWeek ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = selectedDays.contains(dayOfWeek),
                        onCheckedChange = { isChecked ->
                            selectedDays = if (isChecked) {
                                selectedDays + dayOfWeek
                            } else {
                                selectedDays - dayOfWeek
                            }
                        }
                    )
                    Text(text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()))
                }
            }
    }
}

    // Time Picker Dialog
    MaterialDialog(
        dialogState = timeDialogState,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        buttons = {
            positiveButton(text = "Set Alarm") {
                // Combine the picked date and time into LocalDateTime
                addViewModel.onEvent(AddAlarmEvent.ChosenDays(selectedDays))
                addViewModel.onEvent(AddAlarmEvent.ChosenTime(pickedTime))  // Save alarm time
                addViewModel.onEvent(AddAlarmEvent.SaveAlarm)  // Trigger save
                timeDialogState.hide()
            }
            negativeButton(text = "Cancel") { }
        }
    ) {
        timepicker(
            initialTime = LocalTime.now(),
            title = "Select Alarm Time"
        ) { time ->
            pickedTime = time // Store selected time
        }
    }


}

@Composable
fun  AlarmList(
    viewModel: AlarmsViewModel
){
    val alarms by viewModel.alarms.collectAsState()
    // Display list of alarms
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        alarms.forEach { alarm ->
            AlarmItem(
                alarm = alarm,
                onDeleteClick = {
                    viewModel.onEvent(AlarmsEvent.DeleteAlarm(alarm))
                },
                onToggle = { isEnabled ->
                    viewModel.toggleAlarm(isEnabled)
                }
            )
        }
    }
}

@Composable
fun AlarmItem(
    alarm: Alarm,
    onDeleteClick: () -> Unit,
    onToggle: (Alarm) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Switch to toggle the alarm
        Switch(
            checked = alarm.isEnabled,
            onCheckedChange = {
                onToggle(alarm)
            }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            Text(
                text = DateTimeFormatter.ofPattern("hh:mm a").format(alarm.time),
                fontSize = 24.sp
            )
            Text(
                text = alarm.daysOfWeek.sortedBy { it.value }.joinToString { it.getDisplayName(TextStyle.SHORT, Locale.getDefault()) },
                fontSize = 12.sp
            )
        }

        IconButton(
            onClick = { onDeleteClick() },
            content = {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        )
    }
}


