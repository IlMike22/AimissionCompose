package com.mind.market.aimissioncompose.di

import com.google.firebase.database.DatabaseReference
import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.data.GoalRoomDatabase
import com.mind.market.aimissioncompose.statistics.data.IStatisticsRemoteDataSource
import com.mind.market.aimissioncompose.statistics.data.StatisticsRepository
import com.mind.market.aimissioncompose.statistics.data.implementation.StatisticsRemoteDataSource
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import com.mind.market.aimissioncompose.statistics.domain.use_case.implementation.CreateStatisticsGradeUseCase
import com.mind.market.aimissioncompose.statistics.domain.use_case.implementation.DoesStatisticExistsUseCase
import com.mind.market.aimissioncompose.statistics.domain.use_case.implementation.GetStatisticsUseCase
import com.mind.market.aimissioncompose.statistics.domain.use_case.implementation.InsertStatisticUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StatisticsModule {
    @Provides
    @Singleton
    fun provideRemoteDataSource(database: DatabaseReference): IStatisticsRemoteDataSource {
        return StatisticsRemoteDataSource(database)
    }

    @Provides
    @Singleton
    fun provideStatisticsRepository(
        database: GoalRoomDatabase,
        remoteDataSource: IStatisticsRemoteDataSource,
        authRemoteDataSource: IAuthenticationRemoteDataSource
    ): IStatisticsRepository {
        return StatisticsRepository(
            localDataSource = database.statisticsDao(),
            remoteDataSource = remoteDataSource,
            authRemoteDataSource = authRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideGetStatisticsUseCase(repository: IStatisticsRepository): GetStatisticsUseCase {
        return GetStatisticsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDoesStatisticExistsUseCase(
        repository: IStatisticsRepository
    ) = DoesStatisticExistsUseCase(repository)

    @Provides
    @Singleton
    fun provideInsertStatisticUseCase(
        repository: IStatisticsRepository,
    ) = InsertStatisticUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateStatisticsGradeUseCase() = CreateStatisticsGradeUseCase()
}