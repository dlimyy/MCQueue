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

class DoctorRegisterActivity : AppCompatActivity() {
    private lateinit var doc_registeremail : TextInputEditText
    private lateinit var doc_emaillayout : TextInputLayout
    private lateinit var doc_registerpassword : TextInputEditText
    private lateinit var doc_passwordlayout : TextInputLayout
    private lateinit var doc_registerbutton: Button
    private lateinit var doc_backbutton : ImageView
    private lateinit var doc_firstname : TextInputEditText
    private lateinit var doc_firstnamelayout : TextInputLayout
    private lateinit var doc_lastname : TextInputEditText
    private lateinit var doc_lastnamelayout : TextInputLayout
    private lateinit var doc_confirmpassword : TextInputEditText
    private lateinit var doc_confirmpasswordlayout : TextInputLayout
    private lateinit var doc_license : TextInputEditText
    private lateinit var doc_licenselayout : TextInputLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_register)

        doc_registeremail = findViewById(R.id.doc_registeremail)
        doc_emaillayout = findViewById(R.id.doc_registeremaillayout)
        doc_registerpassword = findViewById(R.id.doc_registerpassword)
        doc_passwordlayout = findViewById(R.id.doc_registerpasswordlayout)
        doc_registerbutton = findViewById(R.id.doc_registerbutton)
        doc_backbutton = findViewById(R.id.doc_backbutton)
        doc_firstname = findViewById(R.id.doc_firstname)
        doc_firstnamelayout = findViewById(R.id.doc_firstnamelayout)
        doc_lastname = findViewById(R.id.doc_lastname)
        doc_lastnamelayout = findViewById(R.id.doc_lastnamelayout)
        doc_confirmpassword = findViewById(R.id.doc_confirmpassword)
        doc_confirmpasswordlayout = findViewById(R.id.doc_confirmpasswordlayout)
        doc_license = findViewById(R.id.doc_license)
        doc_licenselayout = findViewById(R.id.doc_licenselayout)

        backButtonClickEvent()
        firstNameTextChange()
        lastNameTextChange()
        registerEmailTextChange()
        passwordTextChange()
        confirmPasswordTextChange()
        licenseTextChange()
        registerButtonClickEvent()

    }

    private fun backButtonClickEvent() {
        doc_backbutton.setOnClickListener {
            val intent : Intent = Intent(this, DoctorLoginpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isValidFirstName(firstname : String?) : Boolean {
        if (TextUtils.isEmpty(firstname)) {
            doc_firstnamelayout.error = "Please enter a valid name"
            return false
        }
        return true
    }

    private fun isValidLastName(lastname : String?) : Boolean {
        if (TextUtils.isEmpty(lastname)) {
            doc_lastnamelayout.error = "Please enter a valid name"
            return false
        }
        return true
    }

    private fun isValidEmail(email : String?) : Boolean {
        if (TextUtils.isEmpty(email)) {
            doc_emaillayout.error = "Please enter a valid email"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            doc_emaillayout.error = "Please key in a valid email address"
            return false
        }
        return true
    }

    private fun isValidPassword(password: String?) : Boolean {
        if (TextUtils.isEmpty(password)) {
            doc_passwordlayout.error = "Please enter password"
            return false
        }
        return true
    }

    private fun isValidConfirmPassword(password: String?, confirmpassword: String?) : Boolean {
        if (TextUtils.isEmpty(confirmpassword)) {
            doc_confirmpasswordlayout.error = "Please enter password"
            return false
        }

        if (!confirmpassword.equals(password)) {
            doc_confirmpasswordlayout.error = "Passwords do not match"
            return false
        }
        return true
    }

    private fun isValidLicense(license : String?) : Boolean {
        if (TextUtils.isEmpty(license)) {
            doc_licenselayout.error = "Please enter Medical License Number"
            return false
        }
        if (!license!!.startsWith('M') || license.length != 7
            || !(license.last() >= 'A') || !(license.last() <= 'Z')) {
            doc_licenselayout.error = "Please enter valid Medical License Number"
            return false
        }
        return true
    }

    private fun firstNameTextChange() {
        doc_firstname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                doc_firstnamelayout.error = null;
            }
        })
    }

    private fun lastNameTextChange() {
        doc_lastname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                doc_lastnamelayout.error = null;
            }
        })
    }

    private fun registerEmailTextChange() {
        doc_registeremail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                doc_emaillayout.error = null;
            }
        })
    }

    private fun passwordTextChange() {
        doc_registerpassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                doc_passwordlayout.error = null;
            }
        })
    }

    private fun confirmPasswordTextChange() {
        doc_confirmpassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                doc_confirmpasswordlayout.error = null;
            }
        })
    }

    private fun licenseTextChange() {
        doc_license.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                doc_licenselayout.error = null;
            }
        })
    }

    private fun registerButtonClickEvent() {
        doc_registerbutton.setOnClickListener {
            val trimmedemail: String = doc_registeremail.text.toString().trim { it <= ' ' }
            val trimmedpassword: String = doc_registerpassword.text.toString().trim { it <= ' ' }
            val trimmedfirstname : String = doc_firstname.text.toString().trim { it <= ' ' }
            val trimmedlastname : String = doc_lastname.text.toString().trim { it <= ' ' }
            val trimmedconfirmpassword : String = doc_confirmpassword.text
                .toString().trim { it <= ' ' }
            val trimmedlicense : String = doc_license.text.toString().trim { it <= ' ' }

            val firstnameValidity : Boolean = isValidFirstName(trimmedfirstname)
            val lastnameValidity : Boolean = isValidLastName(trimmedlastname)
            val emailValidity : Boolean = isValidEmail(trimmedemail)
            val passwordValidity : Boolean = isValidPassword(trimmedpassword)
            val confirmPasswordValidity : Boolean = isValidConfirmPassword(trimmedpassword,
                trimmedconfirmpassword)
            val licenseValidity : Boolean = isValidLicense(trimmedlicense)



            if (firstnameValidity && lastnameValidity && emailValidity
                && passwordValidity && confirmPasswordValidity && licenseValidity){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(trimmedemail,
                    trimmedpassword).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Firebase registered user
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val intent =
                            Intent(this, DoctorHomePage::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("user_id",firebaseUser.uid)
                        intent.putExtra("email_id", trimmedemail)
                        startActivity(intent)
                        finish()
                    } else {
                        // If the login is not successful then show error message.
                        Snackbar.make(doc_registeremail
                            ,task.exception!!.message.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}