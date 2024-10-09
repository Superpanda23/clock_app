package re.recodestudios.clock_app.feature_clock.domain.use_case

import re.recodestudios.clock_app.feature_clock.domain.model.Alarm
import re.recodestudios.clock_app.feature_clock.domain.respository.AlarmRepository

class DeleteAlarm(
    private val repository: AlarmRepository

) {
    suspend operator fun invoke(alarm: Alarm) {
        repository.deleteAlarm(alarm)
    }

}