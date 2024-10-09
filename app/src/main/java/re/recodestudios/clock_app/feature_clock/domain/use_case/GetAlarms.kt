package re.recodestudios.clock_app.feature_clock.domain.use_case

import kotlinx.coroutines.flow.Flow
import re.recodestudios.clock_app.feature_clock.domain.model.Alarm
import re.recodestudios.clock_app.feature_clock.domain.respository.AlarmRepository

class GetAlarms(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(): Flow<List<Alarm>> {
        return repository.getAllAlarms()
    }

}