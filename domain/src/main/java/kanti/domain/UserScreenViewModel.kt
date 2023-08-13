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
import kanti.domain.domain.GetAllUserMessagesUseCase
import kanti.domain.domain.GetUserWithMessagesUseCase
import kanti.domain.domain.UserWithMessages
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserScreenViewModel(
    users: UserRepository,
    private val messages: MessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val getAllUserMessagesUseCase = GetAllUserMessagesUseCase(messages, dispatcher)
    private val getUserWithMessagesUseCase = GetUserWithMessagesUseCase(
        users,
        getAllUserMessagesUseCase,
        dispatcher
    )

    private var userId: Int? = null

    private val _userWithMessages = MutableLiveData<UserWithMessagesUiState>()
    val userWithMessages: LiveData<UserWithMessagesUiState> = _userWithMessages

    fun showUser(userId: Int) {
        this.userId = userId
        viewModelScope.launch {
            updateUserWithMessages(userId)
        }
    }

    fun addMessage(text: String) {
        if (userId == null)
            return
        viewModelScope.launch {
            withContext(dispatcher) {
                messages.addNewMessage(userId!!, text)
            }
            updateUserWithMessages(userId!!)
        }
    }

    fun removeMessage(id: Int) {
        if (userId == null)
            return
        viewModelScope.launch {
            var result: Boolean
            withContext(dispatcher) {
                result = messages.removeMessage(id)
            }
            if (result)
                updateUserWithMessages(userId!!)
        }
    }

    fun notFoundUser() {
        _userWithMessages.value = _userWithMessages.value?.copy(error = "Not found user!")
            ?: UserWithMessagesUiState("", listOf(), "Not found user!")
    }

    private suspend fun updateUserWithMessages(userId: Int) {
        val userWithMessages = getUserWithMessagesUseCase(userId)
        if (userWithMessages == null) {
            notFoundUser()
            return
        }
        val userWithMessagesUiState = userDataToUiState(userWithMessages)
        _userWithMessages.value = userWithMessagesUiState
    }

    private suspend fun userDataToUiState(
        uwm: UserWithMessages
    ): UserWithMessagesUiState = withContext(dispatcher) {
        val messageUiStates = ArrayList<MessageUiState>()
        for (message in uwm.messages)
            messageUiStates.add(MessageUiState(message.id, message.text))
        UserWithMessagesUiState(uwm.user.name, messageUiStates, null)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                UserScreenViewModel(
                    UserRepository.getInstance(),
                    MessageRepository.getInstance()
                )
            }
        }
    }

}