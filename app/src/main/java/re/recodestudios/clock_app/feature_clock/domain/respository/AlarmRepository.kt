package re.recodestudios.clock_app.feature_clock.domain.respository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import re.recodestudios.clock_app.feature_clock.domain.model.Alarm

interface AlarmRepository {

    suspend fun getAllAlarms(): Flow<List<Alarm>>

    suspend fun getAlarmById(id: String): Alarm?

    suspend fun insertAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)
    suspend fun updateAlarm(alarm: Alarm)
}