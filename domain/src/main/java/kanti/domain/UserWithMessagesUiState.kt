package kanti.domain

data class UserWithMessagesUiState(
    val userName: String,
    val messageList: List<MessageUiState>,
    val error: String?
)
