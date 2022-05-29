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

class RegisterDoctors : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_doctors)

        val registeremaild : EditText = findViewById(R.id.registeremaild)
        val registerpasswordd: EditText = findViewById(R.id.registerpasswordd)
        val registerButtond : Button = findViewById(R.id.registerbuttond)
        val loginredirectd : TextView = findViewById(R.id.signupredirectd)
        val licensecheck : EditText = findViewById(R.id.licenseentry)

        // currently missing license check against database for Doctors

        loginredirectd.setOnClickListener {
            val intent : Intent = Intent(this,Login_Doctors::class.java)
            startActivity(intent)

            registerButtond.setOnClickListener {
                when {
                    TextUtils.isEmpty(registeremaild.text.toString().trim { it <= ' ' }) -> {
                        Snackbar.make(
                            registeremaild, "Please enter username",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(licensecheck.text.toString().trim { it <= ' ' }) -> {
                        Snackbar.make(
                            licensecheck, "Please enter Medical Registration Number",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(registerpasswordd.text.toString().trim { it <= ' ' }) -> {
                        Snackbar.make(
                            registerpasswordd, "Please enter password",
                            Snackbar.LENGTH_SHORT
                        ).show()

                    } else -> {
                    val email: String = registeremaild.text.toString().trim { it <= ' ' }
                    val password: String = registerpasswordd.text.toString().trim { it <= ' ' }
                    val license: String = licensecheck.text.toString().trim { it <= ' ' }

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
                                Snackbar.make(registeremaild
                                    ,task.exception!!.message.toString(),
                                    Snackbar.LENGTH_SHORT
                                ).show()


                            }
                        }
                }
                }
            }
        }
    }}
