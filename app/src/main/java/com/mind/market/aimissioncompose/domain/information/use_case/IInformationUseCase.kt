package com.mind.market.aimissioncompose.domain.information.use_case

interface IInformationUseCase {
    suspend fun getInformation(): List<String>
}