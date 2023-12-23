package com.mind.market.aimissioncompose.stocks_diary.detail.data

import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper.addUniqueId
import com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper.toStocksDiaryData
import com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper.toStocksDiaryDto
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.IStocksDiaryRepository
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StocksDiaryRepository(
    private val remoteDataSource: IStocksDiaryRemoteDataSource,
    private val localDataSource: IStocksDiaryLocalDataSource,
    private val authRemoteDataSource: IAuthenticationRemoteDataSource
) : IStocksDiaryRepository {
    override suspend fun addDiary(diary: StocksDiaryData, onResult: (Throwable?) -> Unit) {
        remoteDataSource.addDiary(
            getFirebaseUserId(),
            diary.addUniqueId(),
            onResult = onResult
        )

        withContext(Dispatchers.IO) {
            addEntryToLocalDatabase(diary)
        }
    }

    override suspend fun getDiaries(onResult: (Throwable?, List<StocksDiaryData>) -> Unit) {
        remoteDataSource.getDiaries(getFirebaseUserId()) { error, stocks ->
            if (error == null) {
                addAllEntriesToLocalDatabaseIfNotExist(stocks)
                onResult(null, stocks)
            } else {
                onResult(error, emptyList())
            }
        }
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

    private fun addEntryToLocalDatabase(diary: StocksDiaryData) {
        localDataSource.addDiary(diary.toStocksDiaryDto())
    }

    private fun addAllEntriesToLocalDatabaseIfNotExist(diaries: List<StocksDiaryData>) {
        CoroutineScope(Dispatchers.IO).launch {
            if (localDataSource.getDiaries().isEmpty()) {
                localDataSource.addDiaries(diaries.map { it.toStocksDiaryDto() })
            }
        }
    }
}