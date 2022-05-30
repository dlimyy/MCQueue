package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class Loginpage : AppCompatActivity() {

    private lateinit var loginusername : TextInputEditText
    private lateinit var loginpassword: TextInputEditText
    private lateinit var forgetpassword : TextView
    private lateinit var loginbutton: Button
    private lateinit var signup : TextView
    private lateinit var usernamelayout : TextInputLayout
    private lateinit var passwordlayout : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        loginusername = findViewById(R.id.loginusername)
        loginpassword = findViewById(R.id.loginpassword)
        forgetpassword = findViewById(R.id.forgetpassword)
        loginbutton = findViewById(R.id.login)
        signup = findViewById(R.id.signup)
        usernamelayout= findViewById(R.id.usernamelayout)
        passwordlayout = findViewById(R.id.passwordlayout)
        signUpClickEvent()
        forgetPasswordClickEvent()
        userNameTextChange()
        passwordTextChange()
        loginClickEvent()
    }

    private fun isValidEmail(username : String?) : Boolean {
        if (TextUtils.isEmpty(username)) {
            usernamelayout.error = "Please enter username"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            usernamelayout.error = "Please key in a valid email address"
            return false
        }
        return true
    }

    private fun isValidPassword(password : String?) : Boolean {
        if (TextUtils.isEmpty(password)) {
            passwordlayout.error = "Please enter password"
            return false
        }
        return true
    }

    private fun signUpClickEvent() {
        signup.setOnClickListener {
            val intent : Intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun forgetPasswordClickEvent() {
        forgetpassword.setOnClickListener {
            val intent : Intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun userNameTextChange() {
        loginusername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                usernamelayout.error = null;
            }
        })
    }

    private fun passwordTextChange() {
        loginpassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordlayout.error = null;
            }
        })
    }

    private fun loginClickEvent() {
        loginbutton.setOnClickListener {
            val username: String = loginusername.text.toString().trim { it <= ' ' }
            val password: String = loginpassword.text.toString().trim { it <= ' ' }
            val emailvalidity : Boolean = isValidEmail(username)
            val passwordvalidity : Boolean = isValidPassword(password)

            if (emailvalidity && passwordvalidity){
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