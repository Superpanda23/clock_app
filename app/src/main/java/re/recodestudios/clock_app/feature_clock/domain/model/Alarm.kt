package re.recodestudios.clock_app.feature_clock.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID


@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val time : LocalTime,
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
    var isEnabled: Boolean = true
)

class InvalidAlarmException(message: String): Exception(message)

