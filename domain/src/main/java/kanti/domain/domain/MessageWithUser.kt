package kanti.domain.domain

import kanti.domain.data.Message
import kanti.domain.data.User

data class MessageWithUser(
    val message: Message,
    val user: User
)
