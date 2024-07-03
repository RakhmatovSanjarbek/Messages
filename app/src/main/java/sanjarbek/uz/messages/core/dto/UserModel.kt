package sanjarbek.uz.messages.core.dto

import java.io.Serializable

data class UserModel(
    val userToken: String? = null,
    val userName: String? = null,
    val userSecondName: String? = null,
    val userImage: String? = null
) : Serializable