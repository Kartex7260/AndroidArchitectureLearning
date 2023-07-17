package kanti.aal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import kanti.aal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var view: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)

        view.buttonAdd.setOnClickListener {
            val text = view.etText.text.toString()
            view.etText.setText("")
            viewModel.addElement(text)
            Log.d(TAG, "setOnClick, text = $text")
        }

        viewModel.itemsLiveData.observe(this) { it ->
            Log.d(TAG, "observing, items count = ${it.items.size}")
            val layout = view.mainLayout
            layout.removeAllViews()
            it.items.forEach { state ->
                val view = ItemView(this, state)
                layout.addView(view)
            }
        }
    }

}