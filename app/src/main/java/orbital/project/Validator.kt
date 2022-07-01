package orbital.project

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

abstract class Validator() {

    abstract fun isValid(value : String) : Boolean
    fun textChange(text: TextInputEditText, layout: TextInputLayout) {
        text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                layout.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }
    abstract fun layoutErrorChange(text: TextInputEditText, layout: TextInputLayout) : Boolean

}

