package orbital.project.helper_classes

import androidx.core.util.PatternsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EmailValidator() : Validator() {
    override fun isValid(value : String): Boolean {
        if (value.isEmpty()) {
                return false
        }
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(value).matches()) {
            return false
        }
        return true
    }


    override fun layoutErrorChange(text: TextInputEditText, layout: TextInputLayout) : Boolean{
        if (!isValid(text.text.toString())) {
            layout.error = "Please enter a valid email address"
            return false
        }
        return true
    }

}