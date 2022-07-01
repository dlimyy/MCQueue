package orbital.project

import android.text.TextUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class McrValidator() : Validator() {
    override fun isValid(value : String): Boolean {
        if (value.isEmpty()) {
            return false
        }
        if (!value.startsWith('M') || value.length != 7
            || value.last() < 'A' || value.last() > 'Z'
        ) {
            return false
        }
        return true
    }

    override fun layoutErrorChange(text: TextInputEditText, layout: TextInputLayout) : Boolean {
        if (!isValid(text.text.toString())) {
            layout.error = "Please enter valid Medical License Number"
            return false
        }
        return true
    }
}