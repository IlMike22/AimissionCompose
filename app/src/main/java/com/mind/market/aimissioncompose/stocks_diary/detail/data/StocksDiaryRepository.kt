package com.mind.market.aimissioncompose.stocks_diary.detail.data

import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper.addUniqueId
import com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper.toStocksDiaryData
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.IStocksDiaryRepository
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain

class StocksDiaryRepository(
    private val remoteDataSource: IStocksDiaryRemoteDataSource,
    private val authRemoteDataSource: IAuthenticationRemoteDataSource
) : IStocksDiaryRepository {
    override suspend fun addDiary(diary: StocksDiaryDomain, onResult: (Throwable?) -> Unit) {
        remoteDataSource.addDiary(
            getFirebaseUserId(),
            diary.addUniqueId().toStocksDiaryData(),
            onResult = onResult
        )
    }

    override suspend fun getDiaries(onResult: (Throwable?, List<StocksDiaryDomain>) -> Unit) {
        remoteDataSource.getDiaries(
            getFirebaseUserId(),
            onResult = onResult
        )
    }

    override suspend fun getStocksDiaryOfToday(onResult: (StocksDiaryDomain?) -> Unit) {
        remoteDataSource.getDiaryForToday(getFirebaseUserId(), onResult = onResult)
    }

    override suspend fun removeStocksDiary(
        diary: StocksDiaryDomain,
        onResult: (Throwable?) -> Unit
    ) {
        remoteDataSource.removeDiary(getFirebaseUserId(), diary.toStocksDiaryData(), onResult)
    }

    private fun getFirebaseUserId(): String =
        authRemoteDataSource.getUserData().id
}