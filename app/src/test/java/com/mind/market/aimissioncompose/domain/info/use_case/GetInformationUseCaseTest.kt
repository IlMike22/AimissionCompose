package com.mind.market.aimissioncompose.domain.info.use_case

import com.google.common.truth.Truth.assertThat
import com.mind.market.aimissioncompose.data.APP_NAME
import com.mind.market.aimissioncompose.data.AUTHOR_NAME
import com.mind.market.aimissioncompose.data.USED_TECHNOLOGIES
import com.mind.market.aimissioncompose.data.VERSION
import com.mind.market.aimissioncompose.data.info.repository.FakeInfoRepository
import com.mind.market.aimissioncompose.domain.information.use_case.GetInformationUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetInformationUseCaseTest {
    private lateinit var getInformation: GetInformationUseCase
    private lateinit var fakeInfoRepository: FakeInfoRepository

    @Before
    fun setup() {
        fakeInfoRepository = FakeInfoRepository()
        getInformation = GetInformationUseCase(fakeInfoRepository)

//        val goalsToInsert =
//            mutableListOf<Goal>() // how you can create some random goals for repo later..
//        ('a'..'z').forEach {
//            goalsToInsert.add(Goal.EMPTY.copy(title = it.toString()))
//        }
//        goalsToInsert.shuffle()
//        goalsToInsert.forEach { fakeInfoRepository.insert(it) }
    }

    @Test
    fun `Get information gets expected information`() = runTest {
        val result = getInformation()
        assertThat(result).isEqualTo(listOf(AUTHOR_NAME, APP_NAME, VERSION, USED_TECHNOLOGIES))
    }
}