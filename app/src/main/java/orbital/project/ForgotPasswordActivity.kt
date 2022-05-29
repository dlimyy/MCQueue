package orbital.project

<<<<<<< HEAD
=======
import android.content.Intent
>>>>>>> remotes/origin/Update3.0
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
<<<<<<< HEAD
=======
import android.widget.TextView
>>>>>>> remotes/origin/Update3.0
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

<<<<<<< HEAD
        val forgotpassword_email : EditText = findViewById(R.id.forgotpasswordemail)
        val resetbutton : Button = findViewById(R.id.forgotpasswordbutton)

        resetbutton.setOnClickListener {
            val email : String = forgotpassword_email.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(email)) {
                Snackbar.make(forgotpassword_email, "Please enter email",
=======
        val forgotpassword_emaild : EditText = findViewById(R.id.resetemaild)
        val resetbuttond : Button = findViewById(R.id.reset)
        val redirect : TextView = findViewById(R.id.back)

        redirect.setOnClickListener {
            val intent : Intent = Intent(this,Login_page::class.java)
            startActivity(intent)

        resetbuttond.setOnClickListener {
            val email : String = forgotpassword_emaild.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(email)) {
                Snackbar.make(forgotpassword_emaild, "Please enter email",
>>>>>>> remotes/origin/Update3.0
                    Snackbar.LENGTH_SHORT).show()
            }
            else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
<<<<<<< HEAD
                            Snackbar.make(forgotpassword_email,
=======
                            Snackbar.make(forgotpassword_emaild,
>>>>>>> remotes/origin/Update3.0
                                "Password has been successfully reset",
                                Snackbar.LENGTH_SHORT).show()

                            finish()
                        } else {
<<<<<<< HEAD
                            Snackbar.make(forgotpassword_email,
=======
                            Snackbar.make(forgotpassword_emaild,
>>>>>>> remotes/origin/Update3.0
                            task.exception!!.message.toString(),
                            Snackbar.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }

<<<<<<< HEAD
}
=======
}}
>>>>>>> remotes/origin/Update3.0
