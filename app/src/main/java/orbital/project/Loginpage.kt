package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Loginpage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        val loginusername : TextInputEditText = findViewById(R.id.loginusername)
        val loginpassword: TextInputEditText = findViewById(R.id.loginpassword)
        val forgetpassword: TextView = findViewById(R.id.forgetpassword)
        val login: Button = findViewById(R.id.login)
        val signup: TextView = findViewById(R.id.signup)

        //Assigning click event to register TextView
        signup.setOnClickListener {
            val intent : Intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)

        }

        //Click event to forget password page
        forgetpassword.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }


        //Assigning click event to login button to go to main page
        login.setOnClickListener {
            when {
                TextUtils.isEmpty(loginusername.text.toString().trim { it <= ' ' }) -> {
                    Snackbar.make(loginusername, "Please enter username",
                        Snackbar.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(loginpassword.text.toString().trim{ it <= ' '}) -> {
                     Snackbar.make(loginpassword, "Please enter password",
                         Snackbar.LENGTH_SHORT).show()
                }

                else -> {
                    val username: String = loginusername.text.toString().trim { it <= ' ' }
                    val password: String = loginpassword.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent =
                                    Intent(this, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra(
                                    "user_id",
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                )
                                intent.putExtra("email_id", username)
                                startActivity(intent)
                                finish()
                            } else {
                                // If the login is not successful then show error message.
                                Snackbar.make(loginusername
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