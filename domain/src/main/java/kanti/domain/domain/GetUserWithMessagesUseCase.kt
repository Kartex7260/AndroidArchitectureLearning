package kanti.domain.domain

import kanti.domain.data.Message
import kanti.domain.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class GetUserWithMessagesUseCase(
    private val users: UserRepository,
    private val getAllUserMessagesUseCase: GetAllUserMessagesUseCase,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) {

    suspend operator fun invoke(userId: Int): UserWithMessages? = withContext(dispatcher) {
        val user = users.firstOrNull { it.id == userId } ?: return@withContext null
        val userMessages = getAllUserMessagesUseCase(userId)
        UserWithMessages(user, userMessages)
    }

}