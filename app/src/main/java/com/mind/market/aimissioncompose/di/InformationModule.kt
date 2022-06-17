package com.mind.market.aimissioncompose.di

import com.mind.market.aimissioncompose.data.info.repository.InformationRepository
import com.example.aimissionlite.domain.information.use_case.IInformationUseCase
import com.example.aimissionlite.domain.information.use_case.implementation.InformationUseCase
import com.mind.market.aimissioncompose.data.info.repository.IInformationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InformationModule {
    @Provides
    @Singleton
    fun provideUseCase(repository: IInformationRepository): IInformationUseCase {
        return InformationUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRepository(): IInformationRepository {
        return InformationRepository()
    }
}