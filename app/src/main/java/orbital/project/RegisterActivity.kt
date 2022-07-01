package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var registeremail : TextInputEditText
    private lateinit var emaillayout : TextInputLayout
    private lateinit var registerpassword : TextInputEditText
    private lateinit var passwordlayout : TextInputLayout
    private lateinit var registerbutton: Button
    private lateinit var backbutton : ImageView
    private lateinit var firstname : TextInputEditText
    private lateinit var firstnamelayout : TextInputLayout
    private lateinit var lastname : TextInputEditText
    private lateinit var lastnamelayout : TextInputLayout
    private lateinit var confirmpassword : TextInputEditText
    private lateinit var confirmpasswordlayout : TextInputLayout
    private lateinit var emailValidator: EmailValidator
    private lateinit var passwordValidator : PasswordValidator
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registeremail = findViewById(R.id.registeremail)
        emaillayout = findViewById(R.id.registeremaillayout)
        registerpassword = findViewById(R.id.registerpassword)
        passwordlayout = findViewById(R.id.registerpasswordlayout)
        registerbutton = findViewById(R.id.registerbutton)
        backbutton = findViewById(R.id.registerbackbutton)
        firstname = findViewById(R.id.firstname)
        firstnamelayout = findViewById(R.id.firstnamelayout)
        lastname = findViewById(R.id.lastname)
        lastnamelayout = findViewById(R.id.lastnamelayout)
        confirmpassword = findViewById(R.id.confirmpassword)
        confirmpasswordlayout = findViewById(R.id.confirmpasswordlayout)
        emailValidator = EmailValidator()
        passwordValidator = PasswordValidator()

        backButtonClickEvent()
        firstNameTextChange()
        lastNameTextChange()
        emailValidator.textChange(registeremail,emaillayout)
        passwordValidator.textChange(registerpassword,passwordlayout)
        confirmPasswordTextChange()
        registerButtonClickEvent()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,Loginpage::class.java))
        finish()
    }

    private fun backButtonClickEvent() {
        backbutton.setOnClickListener {
            val intent : Intent = Intent(this, Loginpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isValidFirstName(firstname : String?) : Boolean {
        if (TextUtils.isEmpty(firstname)) {
            firstnamelayout.error = "Please enter a valid name"
            return false
        }
        return true
    }

    private fun isValidLastName(lastname : String?) : Boolean {
        if (TextUtils.isEmpty(lastname)) {
            lastnamelayout.error = "Please enter a valid name"
            return false
        }
        return true
    }

    private fun isValidConfirmPassword(password: String?, confirmpassword: String?) : Boolean {
        if (TextUtils.isEmpty(confirmpassword)) {
            confirmpasswordlayout.error = "Please enter password"
            return false
        }

        if (!confirmpassword.equals(password)) {
            confirmpasswordlayout.error = "Passwords do not match"
            return false
        }
        return true
    }

    private fun firstNameTextChange() {
        firstname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                firstnamelayout.error = null;
            }
        })
    }

    private fun lastNameTextChange() {
        lastname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lastnamelayout.error = null;
            }
        })
    }

    private fun confirmPasswordTextChange() {
        confirmpassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                confirmpasswordlayout.error = null;
            }
        })
    }

    private fun registerButtonClickEvent() {
        registerbutton.setOnClickListener {
            val trimmedfirstname : String = firstname.text.toString().trim { it <= ' ' }
            val trimmedlastname : String = lastname.text.toString().trim { it <= ' ' }

            val firstnameValidity : Boolean = isValidFirstName(trimmedfirstname)
            val lastnameValidity : Boolean = isValidLastName(trimmedlastname)
            val emailValidity : Boolean = emailValidator
                .layoutErrorChange(registeremail,emaillayout)
            val passwordValidity : Boolean = passwordValidator
                .layoutErrorChange(registerpassword,passwordlayout)
            val confirmPasswordValidity : Boolean = isValidConfirmPassword(registerpassword
                .text.toString(), confirmpassword.text.toString())



            if (firstnameValidity && lastnameValidity && emailValidity
                && passwordValidity && confirmPasswordValidity){
                Log.d("email",registeremail.text.toString())
                Log.d("password",registerpassword.text.toString())
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(registeremail
                    .text.toString(), registerpassword.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uid = FirebaseAuth.getInstance().currentUser!!.uid
                            db.collection("Users").document(uid)
                                .set(hashMapOf("Role" to "Patient"))
                            val intent =
                                Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // If the login is not successful then show error message.
                            Snackbar.make(registeremail
                                , "Sorry, please try a different email address or use a password with 6 characters",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}