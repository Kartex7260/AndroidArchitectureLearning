package kanti.domain.domain

import kanti.domain.data.Message
import kanti.domain.data.User

data class UserWithMessages(val user: User, val messages: List<Message>)
