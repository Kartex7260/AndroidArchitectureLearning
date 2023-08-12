package kanti.domain

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kanti.domain.databinding.ActivityUserScreenBinding

class UserScreenActivity : AppCompatActivity() {

    private lateinit var view: ActivityUserScreenBinding
    private val viewModel: UserScreenViewModel by viewModels { UserScreenViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityUserScreenBinding.inflate(layoutInflater)
        setContentView(view.root)

        observeUserData()
        showUserData()
    }

    private fun observeUserData() {
        viewModel.userWithMessages.observe(this) { uiState ->
            if (uiState.error != null) {
                showSnackbar()
                return@observe
            }
            view.apply {
                textViewUser.text = uiState.userName
                for (message in uiState.messageList) {
                    val textViewMessage = TextView(this@UserScreenActivity).apply {
                        text = message.text
                    }
                    llMessages.addView(textViewMessage)
                }
            }
        }
    }

    private fun showSnackbar() {
        Snackbar.make(view.root, "Not found user!", Snackbar.LENGTH_LONG).apply {
            addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    finish()
                }
            })
            show()
        }
    }

    private fun showUserData() {
        intent.extras?.getInt(ZipActivity.UserIdExtra).apply {
            if (this == null) {
                viewModel.notFoundUser()
            } else {
                viewModel.showUser(this)
            }
        }
    }
}