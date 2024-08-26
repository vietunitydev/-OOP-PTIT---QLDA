import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton

class CustomButtonWorkspace @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    fun setButtonProperties(id: Int, text: String, color: Int) {
        this.id = id
        this.text = text
        this.setBackgroundColor(color)

        this.textSize = 16f
//        this.setTextColor(Color.WHITE)

        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.MATCH_PARENT,
            ViewGroup.MarginLayoutParams.WRAP_CONTENT
        )
        layoutParams.bottomMargin = 8
        this.layoutParams = layoutParams
    }
}
