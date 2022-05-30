package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class DoctorLoginpage : AppCompatActivity() {
    private lateinit var doc_loginusername : TextInputEditText
    private lateinit var doc_loginpassword: TextInputEditText
    private lateinit var doc_forgetpassword : TextView
    private lateinit var doc_loginbutton: Button
    private lateinit var doc_signup : TextView
    private lateinit var doc_usernamelayout : TextInputLayout
    private lateinit var doc_passwordlayout : TextInputLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_loginpage)

        doc_loginusername = findViewById(R.id.doctor_loginusername)
        doc_loginpassword = findViewById(R.id.doctor_loginpassword)
        doc_forgetpassword = findViewById(R.id.doctor_forgetpassword)
        doc_loginbutton = findViewById(R.id.doctor_loginbutton)
        doc_signup = findViewById(R.id.doctor_signup)
        doc_usernamelayout = findViewById(R.id.doctor_usernamelayout)
        doc_passwordlayout = findViewById(R.id.doctor_passwordlayout)

        signUpClickEvent()
        forgetPasswordClickEvent()
        userNameTextChange()
        passwordTextChange()
        loginClickEvent()
    }

    private fun isValidEmail(username : String?) : Boolean {
        if (TextUtils.isEmpty(username)) {
            doc_usernamelayout.error = "Please enter username"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            doc_usernamelayout.error = "Please key in a valid email address"
            return false
        }
        return true
    }

    private fun isValidPassword(password : String?) : Boolean {
        if (TextUtils.isEmpty(password)) {
            doc_passwordlayout.error = "Please enter password"
            return false
        }
        return true
    }

    private fun signUpClickEvent() {
        doc_signup.setOnClickListener {
            val intent : Intent = Intent(this,DoctorRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun forgetPasswordClickEvent() {
        doc_forgetpassword.setOnClickListener {
            val intent : Intent = Intent(this,DoctorForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun userNameTextChange() {
        doc_loginusername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                doc_usernamelayout.error = null;
            }
        })
    }

    private fun passwordTextChange() {
        doc_loginpassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                doc_passwordlayout.error = null;
            }
        })
    }

    private fun loginClickEvent() {
        doc_loginbutton.setOnClickListener {
            val username: String = doc_loginusername.text.toString().trim { it <= ' ' }
            val password: String = doc_loginpassword.text.toString().trim { it <= ' ' }
            val emailvalidity : Boolean = isValidEmail(username)
            val passwordvalidity : Boolean = isValidPassword(password)

            if (emailvalidity && passwordvalidity){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent =
                                Intent(this, DoctorHomePage::class.java)
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
                            Snackbar.make(doc_loginusername
                                ,task.exception!!.message.toString(),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}