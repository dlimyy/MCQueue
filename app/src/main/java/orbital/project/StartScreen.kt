package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartScreen : AppCompatActivity() {
    private lateinit var patientlogin : Button
    private lateinit var doctorlogin : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)

        patientlogin = findViewById(R.id.patient)
        patientlogin.setOnClickListener {
            val intent: Intent = Intent(this, Loginpage::class.java)
            startActivity(intent)
            finish()
        }
        doctorlogin = findViewById(R.id.clinic)
        doctorlogin.setOnClickListener {
            val intent : Intent = Intent(this, DoctorLoginpage::class.java)
            startActivity(intent)
            finish()
        }
    }
}