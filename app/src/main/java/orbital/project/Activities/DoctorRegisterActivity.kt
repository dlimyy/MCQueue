package orbital.project.activities

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import orbital.project.helper_classes.EmailValidator
import orbital.project.helper_classes.PasswordValidator
import orbital.project.R
import java.util.*

class DoctorRegisterActivity : AppCompatActivity() {

    private lateinit var clinicName : TextInputEditText
    private lateinit var clinicNameLayout : TextInputLayout
    private lateinit var address : TextInputEditText
    private lateinit var addressLayout : TextInputLayout
    private lateinit var registerButton: Button
    private lateinit var backButton : ImageView
    private lateinit var clinicEmail: TextInputEditText
    private lateinit var clinicEmailLayout : TextInputLayout
    private lateinit var password : TextInputEditText
    private lateinit var passwordLayout : TextInputLayout
    private lateinit var confirmPassword: TextInputEditText
    private lateinit var confirmPasswordLayout : TextInputLayout
    private lateinit var emailValidator: EmailValidator
    private lateinit var passwordValidator : PasswordValidator
    private val db = FirebaseFirestore.getInstance()
    private lateinit var geocoder : Geocoder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_register)

        clinicName = findViewById(R.id.clinicName)
        clinicNameLayout= findViewById(R.id.clinicNameLayout)
        address = findViewById(R.id.clinicAddress)
        addressLayout = findViewById(R.id.clinicAddressLayout)
        registerButton = findViewById(R.id.clinicRegisterButton)
        backButton = findViewById(R.id.navigateClinicRegistertoLogin)
        clinicEmail = findViewById(R.id.clinicEmail)
        clinicEmailLayout = findViewById(R.id.clinicEmailLayout)
        password = findViewById(R.id.clinicPassword)
        passwordLayout = findViewById(R.id.clinicPasswordLayout)
        confirmPassword = findViewById(R.id.clinicConfirmPassword)
        confirmPasswordLayout= findViewById(R.id.clinicConfirmPasswordLayout)
        geocoder = Geocoder(this, Locale.getDefault())
        emailValidator = EmailValidator()
        passwordValidator = PasswordValidator()

        backButtonClickEvent()
        clinicNameTextChange()
        addressTextChange()
        emailValidator.textChange(clinicEmail,clinicEmailLayout)
        passwordValidator.textChange(password,passwordLayout)
        confirmPasswordTextChange()
        registerButtonClickEvent()

    }

    private fun backButtonClickEvent() {
        backButton.setOnClickListener {
            val intent : Intent = Intent(this, DoctorLoginpage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isValidClinicName(firstname : String?) : Boolean {
        if (TextUtils.isEmpty(firstname)) {
            clinicNameLayout.error = "Please enter a valid name"
            return false
        }
        return true
    }

    private fun isValidAddress(lastname : String?) : Boolean {
        if (TextUtils.isEmpty(lastname)) {
            addressLayout.error = "Please enter a valid address"
            return false
        }

        return true
    }

    private fun isValidConfirmPassword(password: String?, confirmpassword: String?) : Boolean {
        if (TextUtils.isEmpty(confirmpassword)) {
           confirmPasswordLayout.error = "Please enter password"
            return false
        }

        if (!confirmpassword.equals(password)) {
            confirmPasswordLayout.error = "Passwords do not match"
            return false
        }
        return true
    }


    private fun clinicNameTextChange() {
        clinicName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clinicNameLayout.error = null;
            }
        })
    }

    private fun addressTextChange() {
       address.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addressLayout.error = null;
            }
        })
    }

    private fun confirmPasswordTextChange() {
        confirmPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                confirmPasswordLayout.error = null;
            }
        })
    }



    private fun registerButtonClickEvent() {
        registerButton.setOnClickListener {
            val trimmedclinicname : String = clinicName.text.toString().trim { it <= ' ' }
            val trimmedaddress : String = address.text.toString().trim { it <= ' ' }

            val clinicNameValidity : Boolean = isValidClinicName(trimmedclinicname)
            val addressValidity : Boolean = isValidAddress(trimmedaddress)
            val emailValidity : Boolean = emailValidator.layoutErrorChange(clinicEmail,clinicEmailLayout)
            val passwordValidity : Boolean = passwordValidator.layoutErrorChange(password,passwordLayout)
            val confirmPasswordValidity : Boolean = isValidConfirmPassword(password.text.toString(),
                confirmPassword.text.toString())
            var tempaddress = Address(Locale.getDefault())
            var addressList: List<Address> = listOf()
            if (trimmedaddress != "") {
                addressList =
                    geocoder.getFromLocationName(address.text.toString(),1)
                if (addressList.isNotEmpty()) {
                    tempaddress = addressList[0]
                } else {
                    addressLayout.error = "Please key in a valid address"
                }
            }
            if (clinicNameValidity && addressValidity && emailValidity
                && passwordValidity && confirmPasswordValidity && addressList.isNotEmpty()
            ){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(clinicEmail.text.toString(),
                    password.text.toString()).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val tempdata = hashMapOf("Address" to address.text.toString(),
                            "geoPoint" to GeoPoint(tempaddress.latitude,tempaddress.longitude))
                        db.collection("Mapdata")
                        .document(clinicName.text.toString())
                        .set(tempdata)
                        val data = hashMapOf("Name" to clinicName.text.toString()
                            , "Address" to address.text.toString())
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        db.collection("Clinics")
                            .document(firebaseUser.uid)
                            .set(data)
                        db.collection("Users")
                            .document(firebaseUser.uid)
                            .set(hashMapOf("Role" to "Clinic"))
                        val intent =
                            Intent(this, DoctorHomePage::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If the login is not successful then show error message.
                        Snackbar.make(clinicEmail
                            ,"Sorry, please try a different email address or use a password with 6 characters",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}