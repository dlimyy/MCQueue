package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var resetemail : TextInputEditText
    private lateinit var resetemaillayout : TextInputLayout
    private lateinit var resetbutton : Button
    private lateinit var backlogin : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        resetemail = findViewById(R.id.patient_resetemail)
        resetemaillayout = findViewById(R.id.patient_resetemaillayout)
        resetbutton = findViewById(R.id.resetbutton)
        backlogin = findViewById(R.id.navigateForgettoLogin)
        backloginClickEvent()
        resetEmailTextChange()
        resetbuttonClickEvent()
    }

    private fun isValidEmail(resetemail : String?) : Boolean {
        if (TextUtils.isEmpty(resetemail)) {
            resetemaillayout.error = "Please enter a valid email"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(resetemail).matches()) {
            resetemaillayout.error = "Please key in a valid email address"
            return false
        }
        return true
    }

    private fun resetEmailTextChange() {
        resetemail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                resetemaillayout.error = null;
            }
        })
    }

    private fun backloginClickEvent() {
        backlogin.setOnClickListener {
            val intent: Intent = Intent(this, Loginpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun resetbuttonClickEvent() {

        resetbutton.setOnClickListener {
            val trimmedemail: String = resetemail.text.toString().trim { it <= ' ' }
            val emailValidity: Boolean = isValidEmail(trimmedemail)

            if (emailValidity) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(trimmedemail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(
                                resetemail,
                                "Password has been successfully reset",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } else {
                            Snackbar.make(
                                resetemail,
                                task.exception!!.message.toString(),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

}