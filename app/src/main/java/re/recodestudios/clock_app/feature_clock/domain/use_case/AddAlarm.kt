package re.recodestudios.clock_app.feature_clock.domain.use_case

import re.recodestudios.clock_app.feature_clock.domain.model.Alarm
import re.recodestudios.clock_app.feature_clock.domain.model.InvalidAlarmException
import re.recodestudios.clock_app.feature_clock.domain.respository.AlarmRepository

class AddAlarm(
    private val repository: AlarmRepository
) {
    @Throws(InvalidAlarmException::class)
    suspend operator fun invoke(alarm: Alarm) {
        if(alarm.daysOfWeek.isEmpty()) {
            throw InvalidAlarmException("The alarm must have at least one day of the week.")
        }
        repository.insertAlarm(alarm)
    }
}