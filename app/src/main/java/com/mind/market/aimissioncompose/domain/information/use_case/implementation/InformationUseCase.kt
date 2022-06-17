package com.example.aimissionlite.domain.information.use_case.implementation

import com.example.aimissionlite.domain.information.use_case.IInformationUseCase
import com.mind.market.aimissioncompose.data.info.repository.IInformationRepository

class InformationUseCase(
    val repository: IInformationRepository
) : IInformationUseCase {
    override fun getInformation(): Map<String, String> = repository.getInformation()
}