package orbital.project

import android.content.Intent
import java.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*
import kotlin.collections.ArrayList

class BookingScreenTiming : AppCompatActivity() {

    private lateinit var timinglist : ArrayList<String>
    private lateinit var appointmentlist : RecyclerView
    private lateinit var adaptor: BookingAdaptor
    private lateinit var noTiming : TextView
    private lateinit var bookingDate : String
    private lateinit var mcrNumber : String
    private lateinit var backButton : ImageView
    private lateinit var currentDay : String
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_screen_timing)
        val clinicName = intent.extras!!.getString("clinic")
        bookingDate = intent.extras!!.getString("date").toString()
        val doctorName = intent.extras!!.getString("doctor")
        mcrNumber = intent.extras!!.getString("mcrNumber").toString()
        currentDay = intent.extras!!.getString("Day").toString()
        timinglist = ArrayList()
        backButton = findViewById(R.id.navigateBookingTimingBookDoctor)
        noTiming = findViewById(R.id.noTimingsAvailable)
        appointmentlist = findViewById(R.id.appointmentList)
        appointmentlist.layoutManager = GridLayoutManager(this,3)
        adaptor = BookingAdaptor(timinglist)
        appointmentlist.adapter = adaptor
        database()
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
        val tempTimeArray = ArrayList<String>()
        val currentDate = Calendar.getInstance().time
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        val currentTime = timeFormatter.format(currentDate)
        val dateFormatter = SimpleDateFormat("dd/MM/yy", Locale.ENGLISH)
        val date = dateFormatter.format(currentDate)

        db.collection("Doctors").document(mcrNumber).get()
            .addOnSuccessListener { document ->
               val timings = document.get(currentDay.lowercase() + "Array") as ArrayList<String>
                for (time in timings) {
                    var startTime = (time.substring(0,2) + time.substring(3,5)).toInt()
                    val endTime = (time.substring(6,8) + time.substring(9)).toInt()
                    while (startTime <= endTime) {
                        if (startTime < 1000) {
                            tempTimeArray.add(
                                StringBuilder("0" + startTime.toString())
                                    .insert(2, ":").toString()
                            )
                        } else {
                            tempTimeArray.add(
                                StringBuilder(startTime.toString())
                                    .insert(2, ":").toString()
                            )
                        }
                        startTime += 30
                        if (startTime % 100 >= 60) {
                            startTime += 40
                        }
                    }
                }
                var counter = 0
                db.collection("Queue").document(mcrNumber)
                    .collection(bookingDate.replace('/','-'))
                    .get().addOnSuccessListener { result ->
                        for (timing in tempTimeArray) {
                            Log.d("bookingdate", bookingDate)
                            Log.d("date", date)
                            if (bookingDate == date) {
                                Log.d("timing", timing)
                                Log.d("currentTime", currentTime)
                                if (timing < currentTime) {
                                    continue
                                }
                            }
                            if (result.size() == 0) {
                                timinglist.add(timing)
                                adaptor.notifyItemInserted(counter)
                                counter++
                            } else {
                                var indicator = 0
                                for (doc in result.documents) {
                                    if (timing == doc.id) {
                                        if ((doc.get("Name") as String) == "") {
                                            timinglist.add(timing)
                                            adaptor.notifyItemInserted(counter)
                                            counter++
                                        }
                                        break
                                    }
                                    indicator++
                                }
                                if (indicator == result.size()) {
                                    timinglist.add(timing)
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
    }
}