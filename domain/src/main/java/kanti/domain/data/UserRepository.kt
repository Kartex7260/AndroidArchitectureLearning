package kanti.domain.data

class UserRepository private constructor() : ArrayList<User>() {

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