package orbital.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val forgotpassword_email : EditText = findViewById(R.id.forgotpasswordemail)
        val resetbutton : Button = findViewById(R.id.forgotpasswordbutton)

        resetbutton.setOnClickListener {
            val email : String = forgotpassword_email.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(email)) {
                Snackbar.make(forgotpassword_email, "Please enter email",
                    Snackbar.LENGTH_SHORT).show()
            }
            else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(forgotpassword_email,
                                "Password has been successfully reset",
                                Snackbar.LENGTH_SHORT).show()

                            finish()
                        } else {
                            Snackbar.make(forgotpassword_email,
                            task.exception!!.message.toString(),
                            Snackbar.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }

}