package sanjarbek.uz.messages.core.dto

import java.io.Serializable

data class ChatModel(
    val message: String? = null,
    val sendMessageTime:String?=null,
    val friendToken: String? = null,
    val myToken: String? = null,
) : Serializable
