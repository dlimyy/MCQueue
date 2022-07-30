package orbital.project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import orbital.project.R

class DoctorForgotPasswordActivity : AppCompatActivity() {
    private lateinit var email : TextInputEditText
    private lateinit var emailLayout: TextInputLayout
    private lateinit var resetButton : Button
    private lateinit var backButton : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_forgot_password)

        email = findViewById(R.id.clinicResetEmail)
        emailLayout = findViewById(R.id.clinicResetEmailLayout)
        resetButton = findViewById(R.id.clinicResetButton)
        backButton = findViewById(R.id.navigateClinicForgettoLogin)
        backLoginClickEvent()
        resetEmailTextChange()
        resetButtonClickEvent()
    }

    private fun isValidEmail(resetemail : String?) : Boolean {
        if (TextUtils.isEmpty(resetemail)) {
            emailLayout.error = "Please enter a valid email"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(resetemail).matches()) {
            emailLayout.error = "Please key in a valid email address"
            return false
        }
        return true
    }

    private fun resetEmailTextChange() {
        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailLayout.error = null;
            }
        })
    }

    private fun backLoginClickEvent() {
        backButton.setOnClickListener {
            val intent: Intent = Intent(this, DoctorLoginpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, DoctorLoginpage::class.java))
        finish()
    }

    private fun resetButtonClickEvent() {
        resetButton.setOnClickListener {
            val trimmedemail: String = email.text.toString().trim { it <= ' ' }
            val emailValidity: Boolean = isValidEmail(trimmedemail)

            if (emailValidity) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(trimmedemail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(
                                email,
                                "Password has been successfully reset",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } else {
                            Snackbar.make(
                                email,
                                "Please key in a valid email address",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}