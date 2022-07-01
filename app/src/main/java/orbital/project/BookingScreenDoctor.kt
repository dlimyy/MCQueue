package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class BookingScreenDoctor : AppCompatActivity() {
    private lateinit var nextButton: Button
    private lateinit var bookingDoctor : AutoCompleteTextView
    private lateinit var backButton: ImageView
    private val db = FirebaseFirestore.getInstance()
    private val docArray = ArrayList<String>()
    private val mcrNumberArray = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_screen_doctor)
        val clinicName = intent?.extras!!.getString("clinic")
        val bookingDate = intent?.extras!!.getString("date")
        backButton = findViewById(R.id.navigateBookingDoctorBookingActivity)
        db.collection("Clinics").whereEqualTo("Name",clinicName).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document.get("Doctor") == null) {
                        break
                    }
                    val tempdocArray = document.get("Doctor") as ArrayList<String>
                    for (doc in tempdocArray) {
                        db.collection("Doctors").document(doc).get()
                            .addOnSuccessListener { physician ->
                                docArray.add(physician.get("Name") as String)
                                mcrNumberArray.add(doc)
                            }.addOnFailureListener {
                                Log.d("Search error","Unable to find doctor")
                            }
                    }
                }
            }.addOnFailureListener {
                Log.d("No doctor error","Unable to find any doctor")
            }
        val adaptor = ArrayAdapter<String>(this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,docArray)
        bookingDoctor = findViewById(R.id.bookingDoctor)
        bookingDoctor.setAdapter(adaptor)
        nextButton = findViewById(R.id.nextBookingScreenTiming)
        nextButton.setOnClickListener {
            if (!TextUtils.isEmpty(bookingDoctor.text.toString())) {
                val mcrNumber = mcrNumberArray[adaptor.getPosition(bookingDoctor.text.toString())]
                val intent = Intent(this, BookingScreenTiming::class.java)
                intent.putExtra("date", bookingDate.toString())
                intent.putExtra("clinic", clinicName.toString())
                intent.putExtra("doctor", bookingDoctor.text.toString())
                intent.putExtra("mcrNumber",mcrNumber)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } else {
                Snackbar.make(bookingDoctor,
                    "Please select a doctor", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        backButton.setOnClickListener {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}