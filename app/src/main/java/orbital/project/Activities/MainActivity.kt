package orbital.project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import orbital.project.R
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
    private lateinit var queueNumber : TextView
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
        queueNumber = findViewById(R.id.queueNumber)

        db.collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnSuccessListener { it ->
                val currentDate = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("yy-MM-dd", Locale.ENGLISH)
                val timeFormat = SimpleDateFormat("HH:mm")
                val date = dateFormat.format(currentDate)
                val time = timeFormat.format(currentDate)

                val appointmentTime = it.get("DateTime") as? ArrayList<String>
                val clinicPlace = it.get("Clinics") as? HashMap<String, ArrayList<String>>
                if (clinicPlace != null && appointmentTime != null
                    && clinicPlace.isNotEmpty() && appointmentTime.isNotEmpty()) {
                    for (appointment in appointmentTime) {
                        val appointmentDate = appointment.substring(0,8)
                        val timeOfAppointment = appointment.substring(9)
                        val mcr = clinicPlace[appointment]!![1]
                        if (appointmentDate <= date) {
                            if (timeOfAppointment < time) {
                                if (appointmentDate == date) {
                                    db.collection("LiveQueue")
                                        .document(mcr)
                                        .get().addOnSuccessListener { doc ->
                                            val queueId = doc.get("Queueid") as ArrayList<String>
                                            val timingList = doc
                                                .get("TimingList") as ArrayList<String>
                                            val index = queueId.indexOf(timeOfAppointment)
                                            timingList.removeAt(index)
                                            queueId.removeAt(index)
                                            db.collection("LiveQueue")
                                                .document(mcr)
                                                .update(mapOf("Queueid" to queueId
                                                    , "TimingList" to timingList))
                                        }
                                }

                                appointmentTime.remove(appointment)
                                clinicPlace.remove(appointment)
                            }
                        }
                    }

                    appointmentText.text =  appointmentTime[0]
                        .substring(6,8) + "-" + appointmentTime[0]
                        .substring(3,5) + "-" + appointmentTime[0]
                        .substring(0,2) + "\n" + appointmentTime[0].substring(9)
                    clinic.text = clinicPlace[appointmentTime[0]]!![0]
                    if (appointmentTime[0].substring(0,8) == date) {
                        db.collection("LiveQueue")
                            .document(clinicPlace[appointmentTime[0]]!![1])
                            .get().addOnSuccessListener { snapshot ->
                                val queueList = snapshot.get("Queueid") as ArrayList<String>
                                val currentPos = queueList
                                    .indexOf(FirebaseAuth.getInstance()
                                        .currentUser!!.uid) + 1
                                queueNumber.text = "Queue Number : " + currentPos.toString()
                            }
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
        bookingcalendar.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }
        queueLocator.setOnClickListener {
            startActivity(Intent(this, QueueLocator::class.java))
        }
        searchDoctor.setOnClickListener {
            startActivity(Intent(this, SearchDoctor::class.java))
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