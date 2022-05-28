package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val forgotpassword_emaild : EditText = findViewById(R.id.resetemaild)
        val resetbuttond : Button = findViewById(R.id.reset)
        val redirect : TextView = findViewById(R.id.back)

        redirect.setOnClickListener {
            val intent : Intent = Intent(this,Login_page::class.java)
            startActivity(intent)

        resetbuttond.setOnClickListener {
            val email : String = forgotpassword_emaild.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(email)) {
                Snackbar.make(forgotpassword_emaild, "Please enter email",
                    Snackbar.LENGTH_SHORT).show()
            }
            else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(forgotpassword_emaild,
                                "Password has been successfully reset",
                                Snackbar.LENGTH_SHORT).show()

                            finish()
                        } else {
                            Snackbar.make(forgotpassword_emaild,
                            task.exception!!.message.toString(),
                            Snackbar.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }

}}