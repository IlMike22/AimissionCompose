package com.mind.market.aimissioncompose.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.data.GoalRoomDatabase
import com.mind.market.aimissioncompose.data.common.repository.GoalRepository
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.detail.use_case.InsertGoalUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoalModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): DatabaseReference {
        return Firebase.database.reference
    }

    @Provides
    @Singleton
    fun provideGoalRepository(
        database: GoalRoomDatabase,
        firebaseDatabaseReference: DatabaseReference,
        authRemoteDataSource: IAuthenticationRemoteDataSource
    ): IGoalRepository {
        return GoalRepository(
            goalDao = database.goalDao(),
            firebaseDatabase = firebaseDatabaseReference,
            authRemoteDatasource = authRemoteDataSource
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
}