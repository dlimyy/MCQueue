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
    private lateinit var name : TextInputEditText
    private lateinit var namelayout : TextInputLayout
    private lateinit var age : TextInputEditText
    private lateinit var agelayout : TextInputLayout
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
        name = findViewById(R.id.name)
        namelayout = findViewById(R.id.namelayout)
        age = findViewById(R.id.age)
        agelayout = findViewById(R.id.ageLayout)
        confirmpassword = findViewById(R.id.confirmpassword)
        confirmpasswordlayout = findViewById(R.id.confirmpasswordlayout)
        emailValidator = EmailValidator()
        passwordValidator = PasswordValidator()

        backButtonClickEvent()
        nameTextChange()
        ageTextChange()
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

    private fun isValidName(name : String?) : Boolean {
        if (TextUtils.isEmpty(name)) {
            namelayout.error = "Please enter a valid name"
            return false
        }
        return true
    }

    private fun isValidAge(age : String?) : Boolean {
        if (TextUtils.isEmpty(age) || age!!.toInt() == 0 || age.toInt() > 120 ) {
            agelayout.error = "Please enter a valid age"
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

    private fun nameTextChange() {
        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                namelayout.error = null;
            }
        })
    }

    private fun ageTextChange() {
        age.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               agelayout.error = null;
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

            val nameValidity : Boolean = isValidName(name.text.toString())
            val ageValidity : Boolean = isValidAge(age.text.toString())
            val emailValidity : Boolean = emailValidator
                .layoutErrorChange(registeremail,emaillayout)
            val passwordValidity : Boolean = passwordValidator
                .layoutErrorChange(registerpassword,passwordlayout)
            val confirmPasswordValidity : Boolean = isValidConfirmPassword(registerpassword
                .text.toString(), confirmpassword.text.toString())



            if (nameValidity && ageValidity && emailValidity
                && passwordValidity && confirmPasswordValidity){
                Log.d("email",registeremail.text.toString())
                Log.d("password",registerpassword.text.toString())
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(registeremail
                    .text.toString(), registerpassword.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uid = FirebaseAuth.getInstance().currentUser!!.uid
                            db.collection("Users").document(uid)
                                .set(hashMapOf("Role" to "Patient", "Name" to name.text.toString()
                                , "Age" to age.text.toString()))
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