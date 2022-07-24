package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.ContentInfoCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private lateinit var bookingcalendar : CardView
    private lateinit var queueLocator: CardView
    private lateinit var searchDoctor: CardView
    private lateinit var logout : Button
    private lateinit var appointmentText : TextView
    private lateinit var clinic : TextView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bookingcalendar = findViewById(R.id.patientBookingCardView)
        queueLocator = findViewById(R.id.queueLocatorCardView)
        searchDoctor = findViewById(R.id.findDoctorCardView)
        logout = findViewById(R.id.patientLogOutButton)
        appointmentText = findViewById(R.id.patientBookingAppointmentDate)
        clinic = findViewById(R.id.patientClinic)
        db.collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnSuccessListener { it ->
                val appointmentTime = it.get("DateTime") as? ArrayList<String>
                val clinicPlace = it.get("Clinics") as? HashMap<String, ArrayList<String>>
                if (clinicPlace != null && appointmentTime != null
                    && clinicPlace.isNotEmpty() && appointmentTime.isNotEmpty()) {

                    appointmentText.text =  appointmentTime[0]
                        .substring(6,8) + "-" + appointmentTime[0]
                        .substring(3,5) + "-" + appointmentTime[0]
                        .substring(0,2) + "\n" + appointmentTime[0].substring(9)
                    clinic.text = clinicPlace[appointmentTime[0]]
                        ?.get(0) ?: "No Queue Number issued yet"
                }
            }
    }

    override fun onStart() {
        super.onStart()
        bookingcalendar.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }
        queueLocator.setOnClickListener {
            startActivity(Intent(this,QueueLocator::class.java))
        }
        searchDoctor.setOnClickListener {
            startActivity(Intent(this,SearchDoctor::class.java))
        }
        logout.setOnClickListener {
            val intent = Intent(this, Loginpage::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Loginpage::class.java)
        startActivity(intent)
        finishAffinity()
    }
}