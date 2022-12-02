package com.mind.market.aimissioncompose.data.info.repository

interface IInformationRepository {
    suspend fun getInformation(): List<String>
}