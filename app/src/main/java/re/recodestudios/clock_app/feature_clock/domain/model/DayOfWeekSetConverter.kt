package re.recodestudios.clock_app.feature_clock.domain.model

import androidx.room.TypeConverter
import java.time.DayOfWeek

class DayOfWeekSetConverter {
    @TypeConverter
    fun fromDayOfWeekSet(daysOfWeek: Set<DayOfWeek>): String {
        return daysOfWeek.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toDayOfWeekSet(daysOfWeekString: String): Set<DayOfWeek> {
        return daysOfWeekString.split(",")
            .mapNotNull { DayOfWeek.valueOf(it) }
            .toSet()
    }
}