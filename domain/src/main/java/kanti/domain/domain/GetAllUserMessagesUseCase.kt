package kanti.domain.domain

import kanti.domain.data.Message
import kanti.domain.data.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllUserMessagesUseCase(
    private val messages: MessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend operator fun invoke(userId: Int): List<Message> = withContext(dispatcher) {
        val userMessages = ArrayList<Message>()
        for (message in messages) {
            if (message.userId == userId)
                userMessages.add(message)
        }
        userMessages
    }
}