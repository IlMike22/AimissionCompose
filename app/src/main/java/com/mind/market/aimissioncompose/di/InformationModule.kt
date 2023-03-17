package com.mind.market.aimissioncompose.di

import com.mind.market.aimissioncompose.data.info.repository.InformationRepository
import com.mind.market.aimissioncompose.data.info.repository.IInformationRepository
import com.mind.market.aimissioncompose.domain.information.use_case.GetInformationUseCase
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
    fun provideRepository(): IInformationRepository {
        return InformationRepository()
    }

    @Provides
    @Singleton
    fun provideGetInformationUseCase(repository: IInformationRepository): GetInformationUseCase {
        return GetInformationUseCase(repository)
    }
}