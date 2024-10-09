package re.recodestudios.clock_app.feature_clock.presentation.alarm.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DigitalClockComponent(
    hour: String,
    minute: String,
    amOrPm: String,
) {
    Text(
        text = "$hour:$minute $amOrPm", style = MaterialTheme.typography.titleLarge
    )
}
