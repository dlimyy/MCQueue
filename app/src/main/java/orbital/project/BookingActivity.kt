package orbital.project

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BookingActivity : AppCompatActivity() {
    private lateinit var dateListener : DatePickerDialog.OnDateSetListener
    private val bookCalendar = Calendar.getInstance()
    private lateinit var bookingDate : EditText
    private lateinit var bookingClinic : AutoCompleteTextView
    private lateinit var nextButton : Button
    private lateinit var clinics : ArrayList<String>
    private lateinit var db : FirebaseFirestore
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        bookingDate = findViewById(R.id.bookingDate)
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
                val intent = Intent(this, BookingScreenDoctor::class.java)
                intent.putExtra("date", bookingDate.text.toString())
                intent.putExtra("clinic", bookingClinic.text.toString())
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } else {
                Snackbar.make(bookingDate,
                    "Please key in a valid date or clinic", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private fun updateLabel() {
        val myFormat = "dd/MM/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.CHINA)
        bookingDate.setText(dateFormat.format(bookCalendar.time))
    }
}