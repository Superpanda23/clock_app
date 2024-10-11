package re.recodestudios.clock_app.feature_clock.presentation.alarm.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import re.recodestudios.clock_app.feature_clock.presentation.add_edit_alarm.components.AddAlarmViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    addViewModel: AddAlarmViewModel = hiltViewModel()
) {
    val verticalScroll = rememberScrollState()

    var hour by remember { mutableStateOf("0") }
    var minute by remember { mutableStateOf("0") }
    var second by remember { mutableStateOf("0") }
    var amOrPm by remember { mutableStateOf("0") }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        addViewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddAlarmViewModel.UiEvent.SaveAlarm -> {

                }
                is AddAlarmViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            val cal = Calendar.getInstance()
            hour = cal.get(Calendar.HOUR).run {
                // Change this to return "12" instead of "0" when it's noon
                if (this == 0) "12" else if (this.toString().length == 1) "0$this" else "$this"
            }
            minute = cal.get(Calendar.MINUTE).run {
                if (this.toString().length == 1) "0$this" else "$this"
            }
            second = cal.get(Calendar.SECOND).run {
                if (this.toString().length == 1) "0$this" else "$this"
            }
            amOrPm = cal.get(Calendar.AM_PM).run {
                if (this == Calendar.AM) "AM" else "PM"
            }

            delay(1000)
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {  HeaderComponent() }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize(),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(verticalScroll)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxHeight(fraction = 0.8f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AnalogClockComponent(
                            hour = hour.toInt(),
                            minute = minute.toInt(),
                            second = second.toInt()
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        DigitalClockComponent(
                            hour = hour,
                            minute = minute,
                            amOrPm = amOrPm,
                        )
                    }
                }
                SetAlarm()
            }
        }
    }
}