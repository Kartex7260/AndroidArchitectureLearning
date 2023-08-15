package kanti.domain.data

class UserRepository private constructor() : ArrayList<User>() {

    fun addNewUser(name: String): User {
        val lastUser = lastOrNull()
        if (lastUser == null) {
            val user = User(0, name)
            add(user)
            return user
        }
        val user = User(lastUser.id + 1, name)
        add(user)
        return user
    }

    companion object {

        private lateinit var _instance: UserRepository

        fun getInstance(): UserRepository {
            if (!::_instance.isInitialized)
                _instance = UserRepository().apply {
                    add(User(0, "Valakas"))
                    add(User(1, "Pena"))
                    add(User(2, "Krol"))
                    add(User(3, "Aboba"))
                }
            return _instance
        }

    }

}