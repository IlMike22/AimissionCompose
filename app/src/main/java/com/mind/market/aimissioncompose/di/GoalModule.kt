package com.mind.market.aimissioncompose.di

import GoalRepository
import android.app.Application
import androidx.room.Room
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.data.GoalRoomDatabase
import com.mind.market.aimissioncompose.data.common.data_source.local.implementation.GoalLocalDataSource
import com.mind.market.aimissioncompose.data.common.data_source.remote.implementation.GoalRemoteDataSource
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.goal.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoalModule {

    @Provides // TODO MIC move out of goal module since it is also needed in statistics
    @Singleton
    fun provideFirebaseDatabase(): DatabaseReference {
        return Firebase.database.reference
    }

    @Provides
    @Singleton
    fun provideGoalLocalDataSource(
        roomDatabase: GoalRoomDatabase
    ) = GoalLocalDataSource(
        goalDao = roomDatabase.goalDao()
    )

    @Provides
    @Singleton
    fun provideGoalRemoteDataSource(
        database: DatabaseReference
    ) = GoalRemoteDataSource(
        firebaseDatabase = database
    )

    @Provides
    @Singleton
    fun provideGoalRepository(
        authRemoteDataSource: IAuthenticationRemoteDataSource,
        localDataSource: GoalLocalDataSource,
        remoteDataSource: GoalRemoteDataSource
    ): IGoalRepository {
        return GoalRepository(
            authRemoteDataSource = authRemoteDataSource,
            goalRemoteDataSource = remoteDataSource,
            goalLocalDataSource = localDataSource
        )
    }

    @Provides
    @Singleton
    fun provideGoalDatabase(app: Application): GoalRoomDatabase {
        return Room.databaseBuilder(
            app,
            GoalRoomDatabase::class.java,
            "goal_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideInsertGoalUseCase(repo: IGoalRepository): InsertGoalUseCase =
        InsertGoalUseCase(repo)

    @Provides
    @Singleton
    fun provideDeleteGoalUseCase(
        repository: IGoalRepository
    ) = DeleteGoalUseCase(repository)

    @Provides
    @Singleton
    fun provideGetGoalUseCase(
        repository: IGoalRepository
    ) = GetGoalUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateGoalStatusUseCase(
        repository: IGoalRepository
    ) = UpdateGoalStatusUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateGoalUseCase(
        repository: IGoalRepository
    ) = UpdateGoalUseCase(repository)

    @Provides
    @Singleton
    fun provideGetGoalsUseCase(
        repository: IGoalRepository
    ) = GetGoalsUseCase(repository)

    @Provides
    @Singleton
    fun provideIsGoalOverdueUseCase(
    ) = IsGoalOverdueUseCase()
}