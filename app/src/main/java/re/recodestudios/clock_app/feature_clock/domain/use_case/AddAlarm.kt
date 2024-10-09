package re.recodestudios.clock_app.feature_clock.domain.use_case

import re.recodestudios.clock_app.feature_clock.domain.model.Alarm
import re.recodestudios.clock_app.feature_clock.domain.respository.AlarmRepository

class AddAlarm(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(alarm: Alarm) {
        repository.insertAlarm(alarm)
    }
}