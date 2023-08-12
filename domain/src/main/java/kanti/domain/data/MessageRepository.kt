package kanti.domain.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MessageRepository private constructor() : ArrayList<Message>() {

    fun addNewMessage(userId: Int, text: String): Message {
        val last = lastOrNull()
        if (last == null) {
            val message = Message(0, text, userId)
            add(message)
            return message
        }
        val message = Message(last.id + 1, text, userId)
        return message
    }

    companion object {

        private lateinit var _instance: MessageRepository

        fun getInstance(dispatcher: CoroutineContext): MessageRepository {
            if (!::_instance.isInitialized)
                _instance = MessageRepository().apply {
                    add(Message(0, "Хаааай", 0))
                    add(Message(1, "Я отойду?", 1))
                    add(Message(2, "Даб даб я", 0))
                    add(Message(3, "Aboba", 3))
                }
            return _instance
        }

    }

}