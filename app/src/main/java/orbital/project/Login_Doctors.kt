package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Login_Doctors : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_doctors)

        val loginusernamed : TextInputEditText = findViewById(R.id.loginusernamed)
        val loginpasswordd: TextInputEditText = findViewById(R.id.loginpasswordd)
        val forgetpasswordd: TextView = findViewById(R.id.forgetpasswordd)
        val logind: Button = findViewById(R.id.logind)
        val signupd: TextView = findViewById(R.id.signupd)

        //Assigning click event to register TextView
        signupd.setOnClickListener {
            val intent : Intent = Intent(this,RegisterDoctors::class.java)
            startActivity(intent)

        }

        //Click event to forget password page
        forgetpasswordd.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }


        //Assigning click event to login button to go to main page
        logind.setOnClickListener {
            when {
                TextUtils.isEmpty(loginusernamed.text.toString().trim { it <= ' ' }) -> {
                    Snackbar.make(loginusernamed, "Please enter username",
                        Snackbar.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(loginpasswordd.text.toString().trim{ it <= ' '}) -> {
                    Snackbar.make(loginpasswordd, "Please enter password",
                        Snackbar.LENGTH_SHORT).show()
                }

                else -> {
                    val username: String = loginusernamed.text.toString().trim { it <= ' ' }
                    val password: String = loginpasswordd.text.toString().trim { it <= ' ' }

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
                                Snackbar.make(loginusernamed
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