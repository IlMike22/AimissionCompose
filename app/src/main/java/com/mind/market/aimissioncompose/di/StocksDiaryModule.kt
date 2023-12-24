package com.mind.market.aimissioncompose.di

import com.google.firebase.database.DatabaseReference
import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.data.GoalRoomDatabase
import com.mind.market.aimissioncompose.stocks_diary.chart.data.StocksDiaryChartRepository
import com.mind.market.aimissioncompose.stocks_diary.chart.domain.IStocksDiaryChartRepository
import com.mind.market.aimissioncompose.stocks_diary.detail.data.IStocksDiaryLocalDataSource
import com.mind.market.aimissioncompose.stocks_diary.detail.data.IStocksDiaryRemoteDataSource
import com.mind.market.aimissioncompose.stocks_diary.detail.data.StocksDiaryLocalDataSource
import com.mind.market.aimissioncompose.stocks_diary.detail.data.StocksDiaryRemoteDataSource
import com.mind.market.aimissioncompose.stocks_diary.detail.data.StocksDiaryRepository
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.IStocksDiaryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StocksDiaryModule {

    @Provides
    @Singleton
    fun provideStocksDiaryLocalDataSource(database: GoalRoomDatabase): IStocksDiaryLocalDataSource =
        StocksDiaryLocalDataSource(database.stocksDiaryDao())

    @Provides
    @Singleton
    fun provideStocksDiaryDetailRepository(
        remoteDataSource: IStocksDiaryRemoteDataSource,
        localDataSource: IStocksDiaryLocalDataSource,
        authDataSource: IAuthenticationRemoteDataSource
    ): IStocksDiaryRepository = StocksDiaryRepository(
        remoteDataSource,
        localDataSource,
        authDataSource
    )

    @Provides
    @Singleton
    fun provideStocksDiaryChartRepository(
        localDataSource: IStocksDiaryLocalDataSource
    ): IStocksDiaryChartRepository =
        StocksDiaryChartRepository(localDataSource)


    @Provides
    @Singleton
    fun provideStocksDiaryDetailRemoteDataSource(
        database: DatabaseReference
    ): IStocksDiaryRemoteDataSource = StocksDiaryRemoteDataSource(
        firebaseDatabase = database
    )
}