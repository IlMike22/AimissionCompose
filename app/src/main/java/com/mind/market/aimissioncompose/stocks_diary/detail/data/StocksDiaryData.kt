package com.mind.market.aimissioncompose.stocks_diary.detail.data

import java.math.BigDecimal

data class StocksDiaryData(
    val id: Int = -1,
    val title: String = "",
    val description: String = "",
    val mood: Int = 0,
    val createdDate: String = "",
//    val stocksSold: StocksInformationData,
//    val stocksBought: StocksInformationData
)

data class StocksInformationData(
    val name: String = "",
    val amount: Int = -1,
    val pricePerStock: BigDecimal = BigDecimal(0),
    val reason:String = ""
)
