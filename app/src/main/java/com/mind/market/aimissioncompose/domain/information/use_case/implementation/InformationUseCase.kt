package com.mind.market.aimissioncompose.domain.information.use_case.implementation

import com.mind.market.aimissioncompose.data.info.repository.IInformationRepository
import com.mind.market.aimissioncompose.domain.information.use_case.IInformationUseCase

class InformationUseCase(
    val repository: IInformationRepository
) : IInformationUseCase {
    override suspend fun getInformation(): List<String> = repository.getInformation()
}