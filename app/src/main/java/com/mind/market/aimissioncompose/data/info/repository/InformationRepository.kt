package com.mind.market.aimissioncompose.data.info.repository

import com.mind.market.aimissioncompose.data.AUTHOR_NAME_LABEL
import com.mind.market.aimissioncompose.data.AUTHOR_NAME_VALUE
import com.mind.market.aimissioncompose.data.VERSION_NAME_LABEL
import com.mind.market.aimissioncompose.data.VERSION_NAME_VALUE
import com.mind.market.aimissioncompose.data.info.repository.IInformationRepository

class InformationRepository() : IInformationRepository {
    override fun getInformation(): Map<String, String> {
        val information = mutableMapOf<String, String>()
        information[AUTHOR_NAME_LABEL] = AUTHOR_NAME_VALUE
        information[VERSION_NAME_LABEL] = VERSION_NAME_VALUE
        return information
    }
}