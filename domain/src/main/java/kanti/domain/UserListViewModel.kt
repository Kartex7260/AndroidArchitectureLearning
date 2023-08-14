package kanti.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kanti.domain.data.User
import kanti.domain.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class UserListViewModel(
    private val users: UserRepository,
    private val dispatcher: CoroutineContext = Dispatchers.Default
) : ViewModel() {

    private val _userList: MutableLiveData<UserListUiState> = MutableLiveData()
    val userList: LiveData<UserListUiState> = _userList

    fun getUsers() {
        viewModelScope.launch {
            var userListUiState: UserListUiState
            withContext(dispatcher) {
                userListUiState = userListToUiState(users)
            }
            _userList.value = userListUiState
        }
    }

    private fun userListToUiState(userList: List<User>): UserListUiState {
        val userUiStateList = ArrayList<UserUiState>()
        for (user in userList) {
            userUiStateList.add(UserUiState(user.id, user.name))
        }
        return UserListUiState(userUiStateList)
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                UserListViewModel(
                    UserRepository.getInstance()
                )
            }
        }

    }

}