package kanti.domain

import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kanti.domain.databinding.ActivityZipBinding

class ZipActivity : AppCompatActivity() {

    private lateinit var view: ActivityZipBinding
    private val zipViewModel: ZipViewModel by viewModels { ZipViewModel.Factory }

    private val messageTextSize = 16F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityZipBinding.inflate(layoutInflater)
        setContentView(view.root)

        zipViewModel.messages.observe(this) {
            view.root.removeAllViews()
            for (messageWithUser in it.messages) {
                val messageTextView = TextView(this).apply {
                    textSize = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        messageTextSize,
                        resources.displayMetrics
                    )
                    text = "User: ${messageWithUser.user.name}: ${messageWithUser.text}"
                    setOnClickListener {
                        val userId = messageWithUser.user.id
                        UserScreenActivity.startActivity(this@ZipActivity, userId)
                    }
                }
                view.root.addView(messageTextView)
            }
        }

        zipViewModel.getMessages()
    }

    override fun onResume() {
        super.onResume()
        zipViewModel.getMessages()
    }

}