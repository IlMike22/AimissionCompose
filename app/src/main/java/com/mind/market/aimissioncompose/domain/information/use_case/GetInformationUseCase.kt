package com.mind.market.aimissioncompose.domain.information.use_case

import com.mind.market.aimissioncompose.data.info.repository.IInformationRepository

class GetInformationUseCase(
    val repository: IInformationRepository
) {
    suspend operator fun invoke(): List<String> =
        repository.getInformation()
}