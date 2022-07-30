package orbital.project.helper_classes

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordValidator() : Validator() {

    override fun isValid(value: String): Boolean {

        return !(value.isEmpty())
    }

    override fun layoutErrorChange(text: TextInputEditText, layout: TextInputLayout) : Boolean {
        if (!isValid(text.text.toString())) {
            layout.error = "Please enter valid password"
            return false
        }
        return true
    }

}