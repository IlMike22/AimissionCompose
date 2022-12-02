package com.mind.market.aimissioncompose.data.info.repository

import com.mind.market.aimissioncompose.data.APP_NAME
import com.mind.market.aimissioncompose.data.AUTHOR_NAME
import com.mind.market.aimissioncompose.data.USED_TECHNOLOGIES
import com.mind.market.aimissioncompose.data.VERSION

class InformationRepository : IInformationRepository {
    override suspend fun getInformation(): List<String> {
        return listOf(AUTHOR_NAME, APP_NAME, VERSION, USED_TECHNOLOGIES)
    }
}