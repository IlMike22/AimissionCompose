package com.mind.market.aimissioncompose.auth.data.model

data class User(
    val name: String = ""
) {
    companion object {
        val EMPTY =  User(name = "")
    }
}
