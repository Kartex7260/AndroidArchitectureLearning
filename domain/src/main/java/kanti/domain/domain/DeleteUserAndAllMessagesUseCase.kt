package kanti.domain.domain

import kanti.domain.data.MessageRepository
import kanti.domain.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class DeleteUserAndAllMessagesUseCase(
    private val users: UserRepository,
    private val messages: MessageRepository,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) {

    suspend operator fun invoke(id: Int): Boolean = withContext(dispatcher) {
        val userDeleted = users.deleteUser(id)
        if (!userDeleted)
            return@withContext false
        deleteAllMessages(id)
        true
    }

    private fun deleteAllMessages(userId: Int) {
        messages.removeIf { it.userId == userId }
    }

}