package kanti.domain

import android.util.Log
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
    messages: MessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val getAllUserMessagesUseCase = GetAllUserMessagesUseCase(messages, dispatcher)
    private val getUserWithMessagesUseCase = GetUserWithMessagesUseCase(
        users,
        getAllUserMessagesUseCase,
        dispatcher
    )

    private val _userWithMessages = MutableLiveData<UserWithMessagesUiState>()
    val userWithMessages: LiveData<UserWithMessagesUiState> = _userWithMessages

    fun showUser(userId: Int) {
        viewModelScope.launch {
            Log.d("usvmThread", Thread.currentThread().name)
            val userWithMessages = getUserWithMessagesUseCase(userId)
            if (userWithMessages == null) {
                notFoundUser()
                return@launch
            }
            val userWithMessagesUiState = withContext(dispatcher) {
                userDataToUiState(userWithMessages)
            }
            _userWithMessages.value = userWithMessagesUiState
        }
    }

    fun notFoundUser() {
        _userWithMessages.value = _userWithMessages.value?.copy(error = "Not found user!")
            ?: UserWithMessagesUiState("", listOf(), "Not found user!")
    }

    private fun userDataToUiState(uwm: UserWithMessages): UserWithMessagesUiState {
        val messageUiStates = ArrayList<MessageUiState>()
        for (message in uwm.messages)
            messageUiStates.add(MessageUiState(message.text))
        return UserWithMessagesUiState(uwm.user.name, messageUiStates, null)
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