package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.health.UidHealthStats
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookingCancellation : AppCompatActivity() {
    private lateinit var cancellationRecyclerView: RecyclerView
    private lateinit var backButton : ImageView
    private lateinit var adaptor: CancellationAdaptor
    private val db = FirebaseFirestore.getInstance()
    private lateinit var uid : String
    private val appointmentArrayList = ArrayList<AppointmentDetails>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_cancellation)
        backButton = findViewById(R.id.navigateBackToBooking)
        cancellationRecyclerView = findViewById(R.id.cancellationCardView)
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        initialise()
        cancellationRecyclerView.layoutManager = LinearLayoutManager(this)
        adaptor = CancellationAdaptor(appointmentArrayList)
        cancellationRecyclerView.adapter = adaptor
        adaptor.setOnItemClickListener(object : CancellationAdaptor.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val appointment = appointmentArrayList[position]
                val intent = Intent(this@BookingCancellation,
                    BookingCancellationConfirmationScreen::class.java)
                intent.putExtra("Name", appointment.name)
                intent.putExtra("Clinic", appointment.clinic)
                intent.putExtra("Date", appointment.date)
                intent.putExtra("Time", appointment.time)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })
        backButton.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }
    }

    private fun initialise() {
        db.collection("Users").document(uid).get().addOnSuccessListener { doc ->
            val clinicInfo = doc.get("Clinics") as? HashMap<String, ArrayList<String>>
            val datetimes =  doc.get("DateTime") as? ArrayList<String>
            if (datetimes != null && clinicInfo != null) {
                var counter = 0
                for (datetime in datetimes) {
                    db.collection("Doctors").document(clinicInfo[datetime]!![1])
                        .get().addOnSuccessListener { it ->
                            val clinic = clinicInfo[datetime]?.get(0) as String
                            val doctor = it.get("Name") as String
                            val date = datetime
                                .substring(6,8) + "-" + datetime
                                .substring(3,5) + "-" + datetime
                                .substring(0,2)
                            val time = datetime.substring(9)
                            appointmentArrayList.add(AppointmentDetails(date,time,doctor,clinic))
                            adaptor.notifyItemInserted(counter)
                            counter++
                        }
                }
            }
        }
    }
}