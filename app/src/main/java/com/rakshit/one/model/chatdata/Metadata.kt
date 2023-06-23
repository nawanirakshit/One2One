package com.rakshit.one.model.chatdata

data class Metadata(
    val lastMessage: String,
    val receiverData: ReceiverData,
    val senderData: ReceiverData,
    var unread_me: Long = 0,
    var key: String
)