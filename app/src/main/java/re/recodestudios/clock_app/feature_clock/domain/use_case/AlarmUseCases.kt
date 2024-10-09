package re.recodestudios.clock_app.feature_clock.domain.use_case

data class AlarmUseCases(
    val getAlarms: GetAlarms,
    val deleteAlarm: DeleteAlarm,
    val addAlarm: AddAlarm,
    val getAlarm: GetAlarm,
    val updateAlarm: UpdateAlarm
)
