package kanti.aal

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

class ItemView(context: Context, val state: ItemUiState) : LinearLayout(context) {

    private lateinit var textView: TextView

    init {
        setOnClickListener {
            state.onRemove()
        }

        gravity = Gravity.CENTER
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            val margin = 8f
            val marginDip = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                margin,
                resources.displayMetrics
            ).toInt()
            setMargins(marginDip, marginDip, marginDip, 0)
        }

        textView = TextView(context).apply {
            text = state.text
        }
        addView(textView)
    }

}