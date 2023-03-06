package com.mind.market.aimissioncompose.di

import GoalRepository
import android.app.Application
import androidx.room.Room
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.data.GoalRoomDatabase
import com.mind.market.aimissioncompose.data.common.data_source.GoalRemoteDataSource
import com.mind.market.aimissioncompose.data.common.data_source.IGoalRemoteDataSource
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
    fun provideGoalRemoteDataSource(
        database: DatabaseReference,
        roomDatabase: GoalRoomDatabase
    ): IGoalRemoteDataSource = GoalRemoteDataSource(database, roomDatabase.goalDao())

    @Provides
    @Singleton
    fun provideGoalRepository(
        database: GoalRoomDatabase,
        firebaseDatabaseReference: DatabaseReference,
        authRemoteDataSource: IAuthenticationRemoteDataSource,
        remoteDataSource: IGoalRemoteDataSource
    ): IGoalRepository {
        return GoalRepository(
            goalDao = database.goalDao(),
            firebaseDatabase = firebaseDatabaseReference,
            authRemoteDataSource = authRemoteDataSource,
            goalRemoteDataSource = remoteDataSource
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
        repository: IGoalRepository
    ) = IsGoalOverdueUseCase()
}