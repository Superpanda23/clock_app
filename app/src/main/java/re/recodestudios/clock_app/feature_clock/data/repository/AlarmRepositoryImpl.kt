package re.recodestudios.clock_app.feature_clock.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import re.recodestudios.clock_app.feature_clock.data.data_source.AlarmDao
import re.recodestudios.clock_app.feature_clock.domain.model.Alarm
import re.recodestudios.clock_app.feature_clock.domain.respository.AlarmRepository

class AlarmRepositoryImpl(
    private val dao: AlarmDao
):AlarmRepository {

    override suspend fun getAllAlarms(): Flow<List<Alarm>> {
        return dao.getAllAlarms()
    }

    override suspend fun getAlarmById(id: String): Alarm? {
        return dao.getAlarmById(id)
    }

    override suspend fun insertAlarm(alarm: Alarm) {
        dao.insert(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        dao.delete(alarm) //Pass the entire Alarm object
    }

        override suspend fun updateAlarm(alarm: Alarm) {
        dao.update(alarm.id, alarm.isEnabled)  // Update only isEnabled, or more if needed
    }
}