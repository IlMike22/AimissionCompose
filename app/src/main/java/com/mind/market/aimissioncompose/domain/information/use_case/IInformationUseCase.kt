package com.example.aimissionlite.domain.information.use_case

interface IInformationUseCase {
    fun getInformation(): Map<String, String>
}