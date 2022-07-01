package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DoctorLoginpage : AppCompatActivity() {
    private lateinit var doc_loginusername : TextInputEditText
    private lateinit var doc_loginpassword: TextInputEditText
    private lateinit var doc_forgetpassword : TextView
    private lateinit var doc_loginbutton: Button
    private lateinit var doc_signup : Button
    private lateinit var doc_usernamelayout : TextInputLayout
    private lateinit var doc_passwordlayout : TextInputLayout
    private lateinit var emailValidator: EmailValidator
    private lateinit var passwordValidator : PasswordValidator
    private val db = FirebaseFirestore.getInstance()
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
        emailValidator = EmailValidator()
        passwordValidator = PasswordValidator()

        signUpClickEvent()
        forgetPasswordClickEvent()
        emailValidator.textChange(doc_loginusername,doc_usernamelayout)
        passwordValidator.textChange(doc_loginpassword,doc_passwordlayout)
        loginClickEvent()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, StartScreen::class.java))
        finish()
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

    private fun loginClickEvent() {
        doc_loginbutton.setOnClickListener {
            val emailvalidity : Boolean = emailValidator
                .layoutErrorChange(doc_loginusername,doc_usernamelayout)
            val passwordvalidity : Boolean = passwordValidator
                .layoutErrorChange(doc_loginpassword,doc_passwordlayout)
            doc_loginpassword.onEditorAction(EditorInfo.IME_ACTION_DONE)
            doc_loginpassword.onEditorAction(EditorInfo.IME_ACTION_DONE)

            if (emailvalidity && passwordvalidity){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(doc_loginusername.text.toString()
                        , doc_loginpassword.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uid = FirebaseAuth.getInstance().currentUser!!.uid
                            var role : String? = null
                            db.collection("Users")
                                .document(uid).get().addOnSuccessListener { document ->
                                    role = document.get("Role") as String?
                                    if (role == null || role == "Patient") {
                                        Snackbar.make(
                                            doc_loginusername, "The username or password is incorrect",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                        return@addOnSuccessListener
                                    } else {
                                        val intent =
                                            Intent(this, DoctorHomePage::class.java)
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
                            Snackbar.make(doc_loginusername
                                ,"The username or password is incorrect",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}