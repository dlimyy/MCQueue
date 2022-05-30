package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class DoctorForgotPasswordActivity : AppCompatActivity() {
    private lateinit var doc_resetemail : TextInputEditText
    private lateinit var doc_resetemaillayout : TextInputLayout
    private lateinit var doc_resetbutton : Button
    private lateinit var doc_backlogintext : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_forgot_password)

        doc_resetemail = findViewById(R.id.doc_resetemail)
        doc_resetemaillayout = findViewById(R.id.doc_resetemaillayout)
        doc_resetbutton = findViewById(R.id.doc_resetbutton)
        doc_backlogintext = findViewById(R.id.doc_backlogin)
        backloginClickEvent()
        resetEmailTextChange()
        resetbuttonClickEvent()
    }

    private fun isValidEmail(resetemail : String?) : Boolean {
        if (TextUtils.isEmpty(resetemail)) {
            doc_resetemaillayout.error = "Please enter a valid email"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(resetemail).matches()) {
            doc_resetemaillayout.error = "Please key in a valid email address"
            return false
        }
        return true
    }

    private fun resetEmailTextChange() {
        doc_resetemail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                doc_resetemaillayout.error = null;
            }
        })
    }

    private fun backloginClickEvent() {
        doc_backlogintext.setOnClickListener {
            val intent: Intent = Intent(this, DoctorLoginpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun resetbuttonClickEvent() {
        doc_resetbutton.setOnClickListener {
            val trimmedemail: String = doc_resetemail.text.toString().trim { it <= ' ' }
            val emailValidity: Boolean = isValidEmail(trimmedemail)

            if (emailValidity) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(trimmedemail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(
                                doc_resetemail,
                                "Password has been successfully reset",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } else {
                            Snackbar.make(
                                doc_resetemail,
                                task.exception!!.message.toString(),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}