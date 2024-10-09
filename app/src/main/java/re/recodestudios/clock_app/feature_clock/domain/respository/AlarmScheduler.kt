package re.recodestudios.clock_app.feature_clock.domain.respository

import re.recodestudios.clock_app.feature_clock.domain.model.Alarm


interface AlarmScheduler {
    fun schedule(item: Alarm)
    fun cancel(item: Alarm)
}