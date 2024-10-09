package re.recodestudios.clock_app.feature_clock.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import re.recodestudios.clock_app.feature_clock.domain.model.Alarm
import re.recodestudios.clock_app.feature_clock.domain.model.DayOfWeekSetConverter
import re.recodestudios.clock_app.feature_clock.domain.model.TimeConverter

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
@TypeConverters(TimeConverter::class, DayOfWeekSetConverter::class)
abstract class AlarmDatabase:RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    companion object {
        const val DATABASE_NAME = "alarm_db"
    }

}