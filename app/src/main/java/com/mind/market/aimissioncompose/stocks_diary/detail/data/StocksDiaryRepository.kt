package com.mind.market.aimissioncompose.stocks_diary.detail.data

import androidx.compose.material.contentColorFor
import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper.addUniqueId
import com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper.toStocksDiaryDto
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.IStocksDiaryRepository
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class StocksDiaryRepository(
    private val remoteDataSource: IStocksDiaryRemoteDataSource,
    private val localDataSource: IStocksDiaryLocalDataSource,
    private val authRemoteDataSource: IAuthenticationRemoteDataSource
) : IStocksDiaryRepository {
    override suspend fun addDiary(diary: StocksDiaryData): Throwable? {
        val throwable = remoteDataSource.addDiary(
            getFirebaseUserId(),
            diary.addUniqueId()
        )

        withContext(Dispatchers.IO) {
            addEntryToLocalDatabase(diary)
        }

        return throwable
    }

    override suspend fun getDiaries() = remoteDataSource.getDiaries(getFirebaseUserId())

    override suspend fun getStocksDiaryOfToday() =
        remoteDataSource.getDiaryForToday(getFirebaseUserId())


    override suspend fun removeStocksDiary(diary: StocksDiaryData) =
        remoteDataSource.removeDiary(getFirebaseUserId(), diary)

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

    private fun removeEntry(diary: StocksDiaryData) {
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.removeDiary(diary.toStocksDiaryDto())
        }
    }
}


data class StocksDiaryResponse(
    val diaries: List<StocksDiaryData>? = null,
    val error: Throwable? = null
)