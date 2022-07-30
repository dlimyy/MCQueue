package orbital.project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import orbital.project.*
import orbital.project.helper_classes.EmailValidator
import orbital.project.helper_classes.PasswordValidator

class Loginpage : AppCompatActivity() {

    private lateinit var loginusername : TextInputEditText
    private lateinit var loginpassword: TextInputEditText
    private lateinit var forgetpassword : TextView
    private lateinit var loginbutton: Button
    private lateinit var signup : Button
    private lateinit var usernamelayout : TextInputLayout
    private lateinit var passwordlayout : TextInputLayout
    private lateinit var emailValidator: EmailValidator
    private lateinit var passwordValidator : PasswordValidator
    private val db = FirebaseFirestore.getInstance()

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
        emailValidator = EmailValidator()
        passwordValidator = PasswordValidator()
    }

    override fun onStart() {
        super.onStart()
        signUpClickEvent()
        forgetPasswordClickEvent()
        emailValidator.textChange(loginusername,usernamelayout)
        passwordValidator.textChange(loginpassword,passwordlayout)
        loginClickEvent()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, StartScreen::class.java))
        finish()
    }

    private fun signUpClickEvent() {
        signup.setOnClickListener {
            val intent : Intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun forgetPasswordClickEvent() {
        forgetpassword.setOnClickListener {
            val intent : Intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginClickEvent() {
        loginbutton.setOnClickListener {
            val emailvalidity : Boolean = emailValidator.layoutErrorChange(loginusername,usernamelayout)
            val passwordvalidity : Boolean = passwordValidator.layoutErrorChange(loginpassword,passwordlayout)
            loginpassword.onEditorAction(EditorInfo.IME_ACTION_DONE)

            if (emailvalidity && passwordvalidity){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(loginusername.text.toString()
                    , loginpassword.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uid = FirebaseAuth.getInstance().currentUser!!.uid
                            var role : String? = null
                            var token = ""
                            FirebaseMessaging.getInstance().token.addOnCompleteListener { mission ->
                                if (mission.isSuccessful) {
                                    token = mission.result
                                }
                            }
                            db.collection("Users")
                                .document(uid).get().addOnSuccessListener { document ->
                                    role = document.get("Role") as String?
                                    if (role == null || role == "Clinic") {
                                        Snackbar.make(loginusername
                                            ,"The username or password is incorrect",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                        return@addOnSuccessListener
                                    } else {
                                        db.collection("Users").document(uid).get()
                                            .addOnSuccessListener {
                                                if (document.get("Token") == null) {
                                                    db.collection("Users")
                                                        .document(uid)
                                                        .set(hashMapOf("Token" to token)
                                                            , SetOptions.merge())
                                                } else if ((document
                                                        .get("Token") as String) != token) {
                                                    db.collection("Users")
                                                        .document(uid)
                                                        .update("Token", token)
                                                }
                                            }
                                        val intent =
                                            Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        overridePendingTransition(
                                            R.anim.slide_in_right,
                                            R.anim.slide_out_left
                                        )
                                        finish()
                                    }
                                }
                        } else {
                            // If the login is not successful then show error message.
                            Snackbar.make(loginusername
                                ,"The username or password is incorrect",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }


}