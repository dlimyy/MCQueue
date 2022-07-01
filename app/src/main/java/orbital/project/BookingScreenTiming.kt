package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class BookingScreenTiming : AppCompatActivity() {

    private lateinit var timinglist : ArrayList<String>
    private lateinit var appointmentlist : RecyclerView
    private lateinit var adaptor: BookingAdaptor
    private lateinit var noTiming : TextView
    private lateinit var bookingDate : String
    private lateinit var mcrNumber : String
    private lateinit var backButton : ImageView
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_screen_timing)
        val clinicName = intent.extras!!.getString("clinic")
        bookingDate = intent.extras!!.getString("date").toString()
        val doctorName = intent.extras!!.getString("doctor")
        mcrNumber = intent.extras!!.getString("mcrNumber").toString()
        timinglist = ArrayList()
        backButton = findViewById(R.id.navigateBookingTimingBookDoctor)
        noTiming = findViewById(R.id.noTimingsAvailable)
        database()
        appointmentlist = findViewById(R.id.appointmentList)
        appointmentlist.layoutManager = GridLayoutManager(this,3)
        adaptor = BookingAdaptor(timinglist)
        appointmentlist.adapter = adaptor
        adaptor.setOnItemClickListener(object : BookingAdaptor.OnItemClickListener{
            override fun onItemClick(position: Int) {
                intent = Intent(this@BookingScreenTiming,
                    BookingConfirmationScreen::class.java)
                intent.putExtra("clinic", clinicName.toString())
                intent.putExtra("date", bookingDate.toString())
                intent.putExtra("doctor", doctorName.toString())
                intent.putExtra("time", timinglist[position])
                intent.putExtra("mcrNumber",mcrNumber)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })
        backButton.setOnClickListener {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun database() {
        db.collection("Doctors").document(mcrNumber).collection("Dates")
            .document(bookingDate.replace('/','-'))
            .get().addOnSuccessListener { result ->
                val allTiming = result.data
                var counter : Int = 1
                allTiming?.forEach { time ->
                    val canBook = time.value as String
                    if (canBook.isEmpty()) {
                        timinglist.add(time.key as String)
                        adaptor.notifyItemInserted(counter)
                        counter++
                    }
                }
                if (adaptor.itemCount > 0) {
                    noTiming.visibility = View.GONE
                }
            }
    }
}