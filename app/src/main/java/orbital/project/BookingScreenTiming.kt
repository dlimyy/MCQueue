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
        appointmentlist.layoutManager = GridLayoutManager(this,2)
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
            }
        })
    }


    private fun database() {
        timinglist = ArrayList<String>()
        timinglist.add("8.00am")
        timinglist.add("9.00am")
        timinglist.add("10.00am")
        timinglist.add("11.00am")
        timinglist.add("12.00pm")
        timinglist.add("1.00pm")
        timinglist.add("2.00pm")
        timinglist.add("3.00am")
        timinglist.add("4.00am")
        timinglist.add("5.00am")
    }
}