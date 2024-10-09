package re.recodestudios.clock_app.feature_clock.data.data_source

import android.provider.ContactsContract
import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import re.recodestudios.clock_app.feature_clock.domain.model.Alarm

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarm: Alarm)

    @Query("UPDATE alarms SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun update(id: String, isEnabled: Boolean)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Query("SELECT * FROM alarms WHERE id = :id")
    suspend fun getAlarmById(id: String): Alarm?

    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): Flow<List<Alarm>>
}