package kanti.domain

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kanti.domain.databinding.ActivityUserListBinding

class UserListActivity : AppCompatActivity() {

    private lateinit var view: ActivityUserListBinding
    private val viewModel: UserListViewModel by viewModels { UserListViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(view.root)

        observeUsers()
        addNewUserButton()
        viewModel.getUsers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUsers()
    }

    private fun observeUsers() {
        viewModel.userList.observe(this) {
            view.llUserList.removeAllViews()
            for (userUiState in it.users) {
                addUserTextView(userUiState)
            }
        }
    }

    private fun addUserTextView(userUiState: UserUiState) {
        val userTextView = TextView(this).apply {
            text = userUiState.name
            setOnClickListener {
                UserScreenActivity.startActivity(this@UserListActivity, userUiState.id)
            }
        }
        view.llUserList.addView(userTextView)
    }

    private fun addNewUserButton() {
        view.buttonAddUser.setOnClickListener {
            val userName = view.editTextUserName.text.toString()
            if (userName == "") {
                Toast.makeText(this, "User name field is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            view.editTextUserName.text.clear()
            viewModel.addNewUser(userName)
        }
    }

}