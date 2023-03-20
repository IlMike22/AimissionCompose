package com.mind.market.aimissioncompose.presentation.info

import MainDispatcherRule
import com.mind.market.aimissioncompose.data.APP_NAME
import com.mind.market.aimissioncompose.data.AUTHOR_NAME
import com.mind.market.aimissioncompose.data.USED_TECHNOLOGIES
import com.mind.market.aimissioncompose.data.VERSION
import com.mind.market.aimissioncompose.data.info.repository.FakeInfoRepository
import com.mind.market.aimissioncompose.domain.information.use_case.GetInformationUseCase
import com.mind.market.aimissioncompose.presentation.information.InformationViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class InformationViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    lateinit var fakeRepository: FakeInfoRepository
    lateinit var useCase: GetInformationUseCase
    lateinit var mockUseCase: GetInformationUseCase

    private lateinit var viewModel: InformationViewModel

    @Before
    fun setup() {
        fakeRepository = FakeInfoRepository()
        useCase = GetInformationUseCase(
            repository = fakeRepository
        )

        mockUseCase = mock()
    }

    @Test
    fun `Get information is called initially and gets expected data`() = runTest {
        whenever(mockUseCase.invoke()).thenReturn(
            listOf(
                AUTHOR_NAME,
                APP_NAME,
                VERSION,
                USED_TECHNOLOGIES
            )
        )

        viewModel = InformationViewModel(mockUseCase)
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.state.collect() }

        verify(mockUseCase).invoke()

        assertEquals(
            viewModel.state.value.information, listOf(
                AUTHOR_NAME,
                APP_NAME,
                VERSION,
                USED_TECHNOLOGIES
            )
        )

        collectJob.cancel()
    }
}