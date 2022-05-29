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
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registeremail : EditText = findViewById(R.id.registeremail)
        val registerpassword: EditText = findViewById(R.id.registerpassword)
        val registerButton : Button = findViewById(R.id.registerbutton)
        val loginredirect : TextView = findViewById(R.id.signupredirect)

        loginredirect.setOnClickListener {
            val intent: Intent = Intent(this, Loginpage::class.java)
            startActivity(intent)
        }

            registerButton.setOnClickListener {
                when {
                    TextUtils.isEmpty(registeremail.text.toString().trim { it <= ' ' }) -> {
                        Snackbar.make(
                            registeremail, "Please enter username",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(registerpassword.text.toString().trim { it <= ' ' }) -> {
                        Snackbar.make(
                            registerpassword, "Please enter password",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else -> {
                    val email: String = registeremail.text.toString().trim { it <= ' ' }
                    val password: String = registerpassword.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                // Firebase registered user
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                val intent =
                                    Intent(this, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id",firebaseUser.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()
                            } else {
                                // If the login is not successful then show error message.
                                Snackbar.make(registeremail
                                    ,task.exception!!.message.toString(),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}