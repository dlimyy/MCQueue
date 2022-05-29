package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class DoctorForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_forgot_password)


        val forgotpassword_emaildoc : EditText = findViewById(R.id.resetemaildoc)
        val resetbuttondoc : Button = findViewById(R.id.resetd)
        val redirectd : TextView = findViewById(R.id.backd)

        redirectd.setOnClickListener {
            val intent: Intent = Intent(this, DoctorLoginpage::class.java)
            startActivity(intent)
        }
        resetbuttondoc.setOnClickListener {
            val email : String = forgotpassword_emaildoc.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(email)) {
                Snackbar.make(forgotpassword_emaildoc, "Please enter email",
                    Snackbar.LENGTH_SHORT).show()
            }
            else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(forgotpassword_emaildoc,
                                "Password has been successfully reset",
                                Snackbar.LENGTH_SHORT).show()

                            finish()
                        } else {
                            Snackbar.make(forgotpassword_emaildoc,
                                task.exception!!.message.toString(),
                                Snackbar.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}