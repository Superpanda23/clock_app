package re.recodestudios.clock_app.feature_clock.presentation.alarm.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HeaderComponent() {
    Box(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(text = "Alarm", style = MaterialTheme.typography.titleLarge)
    }
}