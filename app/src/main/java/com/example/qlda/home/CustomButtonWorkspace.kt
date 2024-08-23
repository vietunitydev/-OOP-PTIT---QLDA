import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class CustomButtonWorkspace @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        // Any custom initialization can be done here if needed
    }

    fun setButtonProperties(id: Int, text: String, color: Int) {
        this.id = id
        this.text = text
        this.setBackgroundColor(color)
    }
}