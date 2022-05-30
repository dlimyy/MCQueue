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
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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

        backButtonClickEvent()
        firstNameTextChange()
        lastNameTextChange()
        registerEmailTextChange()
        passwordTextChange()
        confirmPasswordTextChange()
        registerButtonClickEvent()

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

    private fun isValidEmail(email : String?) : Boolean {
        if (TextUtils.isEmpty(email)) {
            emaillayout.error = "Please enter a valid email"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emaillayout.error = "Please key in a valid email address"
            return false
        }
        return true
    }

    private fun isValidPassword(password: String?) : Boolean {
        if (TextUtils.isEmpty(password)) {
            passwordlayout.error = "Please enter password"
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

    private fun registerEmailTextChange() {
        registeremail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emaillayout.error = null;
            }
        })
    }

    private fun passwordTextChange() {
        registerpassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordlayout.error = null;
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
            val trimmedemail: String = registeremail.text.toString().trim { it <= ' ' }
            val trimmedpassword: String = registerpassword.text.toString().trim { it <= ' ' }
            val trimmedfirstname : String = firstname.text.toString().trim { it <= ' ' }
            val trimmedlastname : String = lastname.text.toString().trim { it <= ' ' }
            val trimmedconfirmpassword : String = confirmpassword.text.toString().trim { it <= ' ' }

            val firstnameValidity : Boolean = isValidFirstName(trimmedfirstname)
            val lastnameValidity : Boolean = isValidLastName(trimmedlastname)
            val emailValidity : Boolean = isValidEmail(trimmedemail)
            val passwordValidity : Boolean = isValidPassword(trimmedpassword)
            val confirmPasswordValidity : Boolean = isValidConfirmPassword(trimmedpassword,
                trimmedconfirmpassword)



            if (firstnameValidity && lastnameValidity && emailValidity
                && passwordValidity && confirmPasswordValidity){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(trimmedemail,
                    trimmedpassword).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val intent =
                                Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id",firebaseUser.uid)
                            intent.putExtra("email_id", trimmedemail)
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