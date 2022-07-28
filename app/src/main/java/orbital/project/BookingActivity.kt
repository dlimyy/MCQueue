package orbital.project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class BookingActivity : AppCompatActivity() {

    private lateinit var backButton : ImageView
    private lateinit var bookAppointmentButton: Button
    private lateinit var cancellationButton: Button
    private lateinit var bookingRecyclerView: RecyclerView
    private lateinit var errorMessage : TextView
    private val db = FirebaseFirestore.getInstance()
    private lateinit var uid : String
    private val appointmentArrayList  = ArrayList<AppointmentDetails>()
    private lateinit var adaptor: CancellationAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        backButton = findViewById(R.id.navigateBookingActivityMainActivity)
        bookAppointmentButton = findViewById(R.id.bookAppointmentButton)
        cancellationButton = findViewById(R.id.cancelAppointmentButton)
        bookingRecyclerView = findViewById(R.id.bookingRecyclerView)
        errorMessage = findViewById(R.id.bookingAppointmentList)
        uid = FirebaseAuth.getInstance().currentUser!!.uid
    }

    override fun onStart() {
        super.onStart()
        backButton.setOnClickListener {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }
        bookAppointmentButton.setOnClickListener {
            val intent = Intent(this, BookingScreenDate::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
        cancellationButton.setOnClickListener {
            val intent = Intent(this, BookingCancellation::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
        initialise()
        bookingRecyclerView.layoutManager = LinearLayoutManager(this)
        adaptor = CancellationAdaptor(appointmentArrayList)
        bookingRecyclerView.adapter = adaptor
        adaptor.setOnItemClickListener(object : CancellationAdaptor.OnItemClickListener{
            override fun onItemClick(position: Int) {

            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
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
                            if (adaptor.itemCount > 0) {
                                errorMessage.visibility = View.GONE
                            }
                        }
                }
            }
        }
    }

}