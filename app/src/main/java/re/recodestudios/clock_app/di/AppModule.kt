package re.recodestudios.clock_app.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import re.recodestudios.clock_app.feature_clock.domain.respository.AlarmScheduler
import re.recodestudios.clock_app.feature_clock.data.scheduler.AndroidAlarmScheduler
import re.recodestudios.clock_app.feature_clock.data.data_source.AlarmDatabase
import re.recodestudios.clock_app.feature_clock.data.repository.AlarmRepositoryImpl
import re.recodestudios.clock_app.feature_clock.domain.respository.AlarmRepository
import re.recodestudios.clock_app.feature_clock.domain.use_case.AddAlarm
import re.recodestudios.clock_app.feature_clock.domain.use_case.AlarmUseCases
import re.recodestudios.clock_app.feature_clock.domain.use_case.DeleteAlarm
import re.recodestudios.clock_app.feature_clock.domain.use_case.GetAlarm
import re.recodestudios.clock_app.feature_clock.domain.use_case.GetAlarms
import re.recodestudios.clock_app.feature_clock.domain.use_case.UpdateAlarm
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AlarmDatabase {
        return Room.databaseBuilder(
            app,
            AlarmDatabase::class.java,
            AlarmDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAlarmRepository(db: AlarmDatabase): AlarmRepository {
        return AlarmRepositoryImpl(db.alarmDao())
    }

    @Provides
    @Singleton
    fun provideAlarmUseCases(repository: AlarmRepository): AlarmUseCases {
        return AlarmUseCases(
            getAlarms = GetAlarms(repository),
            deleteAlarm = DeleteAlarm(repository),
            addAlarm = AddAlarm(repository),
            getAlarm = GetAlarm(repository),
            updateAlarm = UpdateAlarm(repository)
        )
    }

    @Provides
    @Singleton
    fun provideAlarmScheduler(@ApplicationContext context: Context): AlarmScheduler {
        return AndroidAlarmScheduler(context)
    }

}