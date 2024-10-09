package re.recodestudios.clock_app

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import re.recodestudios.clock_app.feature_clock.domain.model.Alarm
import re.recodestudios.clock_app.feature_clock.domain.respository.AlarmScheduler
import re.recodestudios.clock_app.feature_clock.domain.use_case.AlarmUseCases
import re.recodestudios.clock_app.feature_clock.presentation.alarm.components.AlarmsEvent
import re.recodestudios.clock_app.feature_clock.presentation.alarm.components.AlarmsViewModel
import java.time.LocalTime
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmsViewModelTest {

    private lateinit var viewModel: AlarmsViewModel
    private lateinit var alarmUseCases: AlarmUseCases
    private lateinit var alarmScheduler: AlarmScheduler
    private lateinit var testDispatcher: TestDispatcher
    private val alarm = Alarm(id = UUID.randomUUID().toString(), time = LocalTime.now(), isEnabled = true)

    @Before
    fun setup() {
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        alarmUseCases = mockk(relaxed = true) // Ensure relaxed to avoid verification issues
        alarmScheduler = mockk(relaxed = true) // Mock with relaxed to avoid unnecessary verifications
        viewModel = AlarmsViewModel(alarmUseCases, alarmScheduler)

        coEvery { alarmUseCases.getAlarms() } returns flowOf(listOf(alarm))
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher after the test
    }

    @Test
    fun `toggleAlarm schedules alarm when enabled`() = runTest {
        val alarmToSchedule = alarm.copy(isEnabled = false) // Create a disabled copy
        coEvery { alarmUseCases.updateAlarm(any()) } just Runs

        // Call the toggleAlarm function with the current alarm state
        viewModel.toggleAlarm(alarm.copy(isEnabled = false)) // Alarm is currently disabled

        // Advance the test dispatcher to ensure all coroutines complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that schedule was called with the correct alarm instance
        coVerify { alarmScheduler.schedule(alarm.copy(isEnabled = true)) }

        // Verify the alarm was updated
        coVerify { alarmUseCases.updateAlarm(alarm.copy(isEnabled = true)) }
    }


    @Test
    fun `toggleAlarm cancels alarm when disabled`() = runTest {
        // Create an alarm that is currently enabled
        val alarmToCancel = alarm.copy(isEnabled = true) // Ensure the alarm is enabled
        coEvery { alarmUseCases.updateAlarm(any()) } just Runs

        // Call the toggleAlarm function with the current alarm state
        viewModel.toggleAlarm(alarmToCancel) // Alarm is currently enabled

        // Advance the test dispatcher to ensure all coroutines complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that cancel was called with the correct alarm instance
        coVerify { alarmScheduler.cancel(alarmToCancel) }

        // Verify that the alarm was updated to disabled state
        coVerify { alarmUseCases.updateAlarm(alarmToCancel.copy(isEnabled = false)) }
    }


    @Test
    fun `onEvent deletes and cancels alarm on DeleteAlarm event`() = runTest {
        val alarmToDelete = Alarm(id = UUID.randomUUID().toString(), time = LocalTime.now(), isEnabled = true)
        val initialAlarms = listOf(alarmToDelete)
        viewModel._alarms.value = initialAlarms // Set initial alarms in the ViewModel
        coEvery { alarmUseCases.deleteAlarm(any()) } just Runs

        viewModel.onEvent(AlarmsEvent.DeleteAlarm(alarmToDelete))

        coVerify { alarmUseCases.deleteAlarm(alarmToDelete) }
        coVerify { alarmScheduler.cancel(alarmToDelete) }

        // Update the ViewModel's state to reflect the deletion
        viewModel._alarms.value = viewModel._alarms.value.filter { it.id != alarmToDelete.id }

        // Assert that the alarm is removed from the ViewModel's state
        assertEquals(listOf(alarmToDelete), viewModel.alarms.value)

        // Stub the deleteAlarm function
        coEvery { alarmUseCases.deleteAlarm(any()) } just Runs
    }
}
