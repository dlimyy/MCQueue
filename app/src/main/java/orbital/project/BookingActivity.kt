package orbital.project

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class BookingActivity : AppCompatActivity() {
    private lateinit var dateListener : DatePickerDialog.OnDateSetListener
    private val bookCalendar = Calendar.getInstance()
    private lateinit var bookingDate : EditText
    private lateinit var bookingClinic : AutoCompleteTextView
    private lateinit var nextButton : Button
    private val Clinics = arrayOf("Evergreen Clinic", "Bedok Polyclinic", "Westview Clinic",
        "Changi Clinic", "Hougang Clinic", "Serangoon Clinic", "Kovan Clinic", "Tampines Clinic",
        "Dakota Clinic", "Springfield Clinic"
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        bookingDate = findViewById(R.id.bookingDate)
        dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            bookCalendar.set(Calendar.YEAR, year)
            bookCalendar.set(Calendar.MONTH, month)
            bookCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
        bookingDate.setOnClickListener {
            DatePickerDialog(this,dateListener,bookCalendar.get(Calendar.YEAR)
                ,bookCalendar.get(Calendar.MONTH),bookCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val adaptor = ArrayAdapter<String>(this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,Clinics)

        bookingClinic = findViewById(R.id.bookingClinic)
        bookingClinic.setAdapter(adaptor)

        nextButton = findViewById(R.id.nextBookingScreenDoctor)
        nextButton.setOnClickListener {
            val intent = Intent(this, BookingScreenDoctor::class.java)
            intent.putExtra("date", bookingDate.text.toString())
            intent.putExtra("clinic", bookingClinic.text.toString())
            startActivity(intent)
        }
    }


    private fun updateLabel() {
        val myFormat = "dd/MM/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.CHINA)
        bookingDate.setText(dateFormat.format(bookCalendar.time))
    }
}