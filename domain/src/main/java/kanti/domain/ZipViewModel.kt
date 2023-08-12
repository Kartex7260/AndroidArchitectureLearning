package kanti.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kanti.domain.data.MessageRepository
import kanti.domain.data.UserRepository
import kanti.domain.domain.GetMessageWithUserUseCase
import kanti.domain.domain.MessageWithUser
import kotlinx.coroutines.launch

class ZipViewModel(
    private val user: UserRepository,
    private val messageRepository: MessageRepository,
) : ViewModel() {

    private val getMessageWithUserUseCase = GetMessageWithUserUseCase(user, messageRepository)

    private val _messages: MutableLiveData<MessageWithUserListUiState> = MutableLiveData()
    val messages: LiveData<MessageWithUserListUiState> = _messages

    fun getMessages() {
        viewModelScope.launch {
            val messagesWithUser = getMessageWithUserUseCase()
            val messagesUiState = messageWithUserToUiState(messagesWithUser)
            _messages.postValue(messagesUiState)
        }
    }

    private fun messageWithUserToUiState(messagesWithUser: List<MessageWithUser>): MessageWithUserListUiState {
        val messages = ArrayList<MessageWithUserUiState>()
        for (messageWithUser in messagesWithUser) {
            messages.add(MessageWithUserUiState(messageWithUser.message.text, messageWithUser.user))
        }
        return MessageWithUserListUiState(messages)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ZipViewModel(
                    UserRepository.getInstance(),
                    MessageRepository.getInstance()
                )
            }
        }
    }

}