package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.cardview.widget.CardView

class DoctorHomePage : AppCompatActivity() {
    private lateinit var doctorInfo : CardView
    private lateinit var logout : Button
    private lateinit var dataAnalytics: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_home_page)
        doctorInfo = findViewById(R.id.doctorInfoCardView)
        dataAnalytics = findViewById(R.id.dataAnalyticsCardView)
        logout = findViewById(R.id.clinicLogOutButton)
    }

    override fun onStart() {
        super.onStart()
        doctorInfo.setOnClickListener {
            val intent = Intent(this,DoctorsInfo::class.java)
            startActivity(intent)
        }
        dataAnalytics.setOnClickListener {
            val intent = Intent(this,DataAnalytics::class.java)
            startActivity(intent)
        }
        logout.setOnClickListener {
            startActivity(Intent(this,DoctorLoginpage::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, DoctorLoginpage::class.java))
        finishAffinity()
    }
}