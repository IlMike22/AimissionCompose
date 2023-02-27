package com.mind.market.aimissioncompose.auth.data.model

data class User(
    val id: String = "",
    val tenantId: String = "",
    val name: String = "",
    val email: String = ""
) {
    companion object {
        val EMPTY = User()
    }
}
