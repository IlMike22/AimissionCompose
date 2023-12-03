package com.mind.market.aimissioncompose.di

import com.google.firebase.database.DatabaseReference
import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.stocks_diary.detail.data.IStocksDiaryRemoteDataSource
import com.mind.market.aimissioncompose.stocks_diary.detail.data.StocksDiaryRepository
import com.mind.market.aimissioncompose.stocks_diary.detail.data.StocksDiaryRemoteDataSource
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
    fun provideStocksDiaryDetailRepository(
        remoteDataSource: IStocksDiaryRemoteDataSource,
        authDataSource: IAuthenticationRemoteDataSource
    ): IStocksDiaryRepository = StocksDiaryRepository(
        remoteDataSource,
        authDataSource
    )

    @Provides
    @Singleton
    fun provideStocksDiaryDetailRemoteDataSource(
        database: DatabaseReference
    ): IStocksDiaryRemoteDataSource = StocksDiaryRemoteDataSource(
        firebaseDatabase = database
    )
}