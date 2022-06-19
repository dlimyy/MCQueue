package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button

class BookingScreenDoctor : AppCompatActivity() {
    private lateinit var nextButton: Button
    private lateinit var bookingDoctor : AutoCompleteTextView
    private val Doctors = arrayOf("Tan Ah kow", "John Tan", "Douglas Lim",
        "Ngoh Zai Yan", "Justin Soh", "Samuel Tan", "Samuel Pang", "Constance Woo",
        "Sally Ng", "Tan Swee Say"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_screen_doctor)
        val clinicName = intent.extras!!.getString("clinic")
        val bookingDate = intent!!.extras!!.getString("date")
        val adaptor = ArrayAdapter<String>(this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,Doctors)
        bookingDoctor = findViewById(R.id.bookingDoctor)
        bookingDoctor.setAdapter(adaptor)
        nextButton = findViewById(R.id.nextBookingScreenTiming)
        nextButton.setOnClickListener {
            val intent = Intent(this, BookingScreenTiming::class.java)
            intent.putExtra("date", bookingDate.toString())
            intent.putExtra("clinic", clinicName.toString())
            intent.putExtra("doctor", bookingDoctor.text.toString())
            startActivity(intent)
        }
    }
}