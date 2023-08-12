package kanti.domain.domain

import android.window.OnBackInvokedDispatcher
import kanti.domain.data.MessageRepository
import kanti.domain.data.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class GetMessageWithUserUseCase(
    private val users: UserRepository,
    private val messages: MessageRepository,
    private val defDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend operator fun invoke(): List<MessageWithUser> = withContext(defDispatcher) {
        val messagesWithUser = ArrayList<MessageWithUser>()
        for (message in messages) {
            val user = users.first { message.userId == it.id }
            messagesWithUser.add(MessageWithUser(message, user))
        }
        messagesWithUser
    }

}