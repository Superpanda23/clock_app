package re.recodestudios.clock_app.feature_clock.presentation.alarm.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import re.recodestudios.clock_app.ui.theme.AnalogClockHourHandColor
import re.recodestudios.clock_app.ui.theme.AnalogClockInnerBoxColor
import re.recodestudios.clock_app.ui.theme.AnalogClockMinuteHandColor
import re.recodestudios.clock_app.ui.theme.AnalogClockOuterBoxColor
import re.recodestudios.clock_app.ui.theme.AnalogClockSecondHandColor
import kotlin.math.min

@Composable
fun AnalogClockComponent(
    hour: Int,
    minute: Int,
    second: Int,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(fraction = 0.6f)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(AnalogClockOuterBoxColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(fraction = 0.78f)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(AnalogClockInnerBoxColor)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val diameter = min(size.width, size.height) * 0.9f
                val radius = diameter / 2

                repeat(4) {
                    val start = center - Offset(0f, radius)
                    val end = start + Offset(0f, radius / 40f)
                    rotate(it / 4f * 360) {
                        drawLine(
                            color = Color.White,
                            start = start,
                            end = end,
                            strokeWidth = 5.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }

                val secondRatio = second / 60f
                val minuteRatio = minute / 60f
                val hourRatio = hour / 12f

                rotate(hourRatio * 360, center) {
                    drawLine(
                        color = AnalogClockHourHandColor,
                        start = center - Offset(0f, radius * 0.4f),
                        end = center + Offset(0f, radius * 0.1f),
                        strokeWidth = 3.8.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                rotate(minuteRatio * 360, center) {
                    drawLine(
                        color = AnalogClockMinuteHandColor,
                        start = center - Offset(0f, radius * 0.6f),
                        end = center + Offset(0f, radius * 0.1f),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                rotate(secondRatio * 360, center) {
                    drawLine(
                        color = AnalogClockSecondHandColor,
                        start = center - Offset(0f, radius * 0.7f),
                        end = center + Offset(0f, radius * 0.1f),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                drawCircle(
                    color = AnalogClockSecondHandColor,
                    radius = 5.dp.toPx(),
                    center = center
                )
            }
        }
    }
}