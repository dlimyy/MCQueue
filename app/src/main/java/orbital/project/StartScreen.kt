package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)

        val patientlogin : Button = findViewById(R.id.patient)
        patientlogin.setOnClickListener {
            val intent: Intent = Intent(this, Loginpage::class.java)
            startActivity(intent)
        }
    }
}