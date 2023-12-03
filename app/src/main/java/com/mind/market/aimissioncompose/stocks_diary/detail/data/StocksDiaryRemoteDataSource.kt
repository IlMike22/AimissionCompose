package com.mind.market.aimissioncompose.stocks_diary.detail.data

import com.google.firebase.database.DatabaseReference
import com.mind.market.aimissioncompose.data.common.FIREBASE_TABLE_STOCKS_DIARY
import com.mind.market.aimissioncompose.data.common.FIREBASE_TABLE_USER
import com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper.toDomain
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import java.time.LocalDate

class StocksDiaryRemoteDataSource(
    private val firebaseDatabase: DatabaseReference
) : IStocksDiaryRemoteDataSource {
    override suspend fun addDiary(
        userId: String?,
        diary: StocksDiaryData,
        onResult: (Throwable?) -> Unit
    ) {
        if (userId == null) {
            onResult(Throwable("Cannot add diary. UserId is null."))
            return
        }

        firebaseDatabase
            .child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(FIREBASE_TABLE_STOCKS_DIARY)
            .child(diary.id.toString())
            .setValue(diary)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(null)
                } else {
                    onResult(Throwable(it.exception?.message))
                }
            }
    }

    override fun getDiaries(
        userId: String,
        onResult: (Throwable?, List<StocksDiaryDomain>) -> Unit
    ) {
        val diaries = mutableListOf<StocksDiaryData>()
        firebaseDatabase
            .child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(FIREBASE_TABLE_STOCKS_DIARY)
            .get()
            .addOnSuccessListener { data ->
                for (singleDataSet in data.children) {
                    singleDataSet.getValue(StocksDiaryData::class.java)?.apply {
                        diaries.add(this)
                    }
                }
                onResult(null, diaries.map { it.toDomain() })
            }.addOnFailureListener { error ->
                onResult(
                    Throwable(message = "Unable to load diaries. Details: ${error.message}"),
                    emptyList()
                )
            }
    }

    override suspend fun getDiaryForToday(userId: String, onResult: (StocksDiaryDomain?) -> Unit) {
        var itemOfToday: StocksDiaryDomain? = null
        firebaseDatabase
            .child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(FIREBASE_TABLE_STOCKS_DIARY)
            .get()
            .addOnSuccessListener { data ->
                for (singleDataSet in data.children) {
                    singleDataSet.getValue(StocksDiaryData::class.java)?.apply {
                        val domainItem = this.toDomain()
                        if (domainItem.createdDate == LocalDate.now()) { // TODO MIC check this condition if it works
                            itemOfToday = domainItem
                        }
                    }
                }
                onResult(itemOfToday)
            }
    }
}