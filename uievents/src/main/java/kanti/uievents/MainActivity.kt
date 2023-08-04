package kanti.uievents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar
import kanti.uievents.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var view: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)

        view.btnUiEvent.setOnClickListener {
            Snackbar.make(view.btnUiEvent, "Its a only view event", Snackbar.LENGTH_SHORT).show()
        }

        view.btnBusiness.setOnClickListener {
            viewModel.updateText()
        }

        view.btnError.setOnClickListener {
            viewModel.updateTextErr()
        }

        viewModel.textLiveData.observe(this) {
            it.err?.also { err ->
                view.tvResult.text = ""
                val callback = object : Snackbar.Callback() {
                    override fun onDismissed(b: Snackbar, event: Int) {
                        viewModel.removeErrMsg()
                    }
                }
                Snackbar.make(view.tvResult, err, Snackbar.LENGTH_SHORT)
                    .addCallback(callback)
                    .show()
                return@observe
            }
            view.tvResult.text = it.text
        }
    }
}