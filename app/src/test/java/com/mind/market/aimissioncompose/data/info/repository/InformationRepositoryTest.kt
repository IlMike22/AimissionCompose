package com.mind.market.aimissioncompose.data.info.repository

import com.google.common.truth.Truth.assertThat
import com.mind.market.aimissioncompose.data.APP_NAME
import com.mind.market.aimissioncompose.data.AUTHOR_NAME
import com.mind.market.aimissioncompose.data.USED_TECHNOLOGIES
import com.mind.market.aimissioncompose.data.VERSION
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InformationRepositoryTest {
    lateinit var results: List<String>

    @Before
    fun setup() {
        results = listOf(AUTHOR_NAME, APP_NAME, VERSION, USED_TECHNOLOGIES)
    }

    @Test
    fun `Get information returns expected list with information`() = runTest {
        val repo = InformationRepository()
        val information = repo.getInformation()
        assertThat(information).isEqualTo(results)
    }
}