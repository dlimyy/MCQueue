package orbital.project

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BookingScreenDate : AppCompatActivity() {
    private lateinit var dateListener : DatePickerDialog.OnDateSetListener
    private val bookCalendar = Calendar.getInstance()
    private lateinit var bookingDate : EditText
    private lateinit var bookingClinic : AutoCompleteTextView
    private lateinit var nextButton : Button
    private lateinit var clinics : ArrayList<String>
    private lateinit var backButton : ImageView
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_screen_date)
        bookingDate = findViewById(R.id.bookingDate)
        backButton = findViewById(R.id.navigateBookingActivityMainActivity)
        clinics = ArrayList()
        db = FirebaseFirestore.getInstance()
        db.collection("Clinics").get().addOnSuccessListener { result ->
            for (document in result) {
                clinics.add(document.get("Name") as String)
            }
        }
        dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            bookCalendar.set(Calendar.YEAR, year)
            bookCalendar.set(Calendar.MONTH, month)
            bookCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        bookingDate.setOnClickListener {
            val datePicker = DatePickerDialog(this,dateListener,bookCalendar.get(Calendar.YEAR)
                ,bookCalendar.get(Calendar.MONTH),bookCalendar.get(Calendar.DAY_OF_MONTH))
            datePicker.datePicker.minDate = Calendar.getInstance(Locale.CHINA).timeInMillis
            val millisecondsInWeek = 7 * 24 * 60 * 60 * 1000
            datePicker.datePicker.maxDate = Calendar
                .getInstance(Locale.CHINA).timeInMillis + millisecondsInWeek
            datePicker.show()
        }

        val adaptor = ArrayAdapter<String>(this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,clinics)

        bookingClinic = findViewById(R.id.bookingClinic)
        bookingClinic.setAdapter(adaptor)
        nextButton = findViewById(R.id.nextBookingScreenDoctor)

        nextButton.setOnClickListener {
            if (!TextUtils.isEmpty(bookingDate.text.toString())
                && !TextUtils.isEmpty(bookingClinic.text.toString())) {
                val dayFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
                val day = dayFormat.format(bookCalendar.time)
                val intent = Intent(this, BookingScreenDoctor::class.java)
                intent.putExtra("day", day)
                intent.putExtra("date", bookingDate.text.toString())
                intent.putExtra("clinic", bookingClinic.text.toString())
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } else {
                Snackbar.make(bookingDate,
                    "Please key in a valid date or clinic", Snackbar.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        backButton.setOnClickListener {
            startActivity(Intent(this,BookingActivity::class.java))
            finish()
        }
    }

    private fun updateLabel() {
        val myFormat = "dd/MM/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.ENGLISH)
        bookingDate.setText(dateFormat.format(bookCalendar.time))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

}