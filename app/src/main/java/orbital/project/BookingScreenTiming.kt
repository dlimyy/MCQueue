package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookingScreenTiming : AppCompatActivity() {

    private lateinit var timinglist : ArrayList<String>
    private lateinit var appointmentlist : RecyclerView
    private lateinit var adaptor: BookingAdaptor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_screen_timing)
        database()
        val clinicName = intent.extras!!.getString("clinic")
        val bookingDate = intent.extras!!.getString("date")
        val doctorName = intent.extras!!.getString("doctor")
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
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })
    }


    private fun database() {
        timinglist = ArrayList<String>()
        timinglist.add("08:00")
        timinglist.add("08:30")
        timinglist.add("09:00")
        timinglist.add("09:30")
        timinglist.add("10:00")
        timinglist.add("10:30")
        timinglist.add("11:00")
        timinglist.add("11:30")
        timinglist.add("12:00")
        timinglist.add("12:30")
        timinglist.add("13:00")
        timinglist.add("13:30")
        timinglist.add("14:00")
        timinglist.add("14:30")
        timinglist.add("15:00")
        timinglist.add("15:30")
        timinglist.add("16:00")
        timinglist.add("16:30")
        timinglist.add("17:00")
        timinglist.add("17:30")
        timinglist.add("18:00")
    }
}