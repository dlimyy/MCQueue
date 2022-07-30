package orbital.project.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import orbital.project.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BookingCancellationConfirmationScreen : AppCompatActivity() {
    private lateinit var backButton : ImageView
    private lateinit var cancellationDate : TextInputEditText
    private lateinit var cancellationTime : TextInputEditText
    private lateinit var cancellationClinic : TextInputEditText
    private lateinit var cancellationName : TextInputEditText
    private lateinit var cancellationButton : Button
    private val db = FirebaseFirestore.getInstance()
    private lateinit var uid : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_cancellation_confirmation_screen)
        cancellationName = findViewById(R.id.cancellationDoctor)
        cancellationClinic = findViewById(R.id.cancellationClinic)
        cancellationDate = findViewById(R.id.cancellationDate)
        cancellationTime = findViewById(R.id.cancellationTiming)
        backButton = findViewById(R.id.navigatebacktoCancellation)
        cancellationButton = findViewById(R.id.cancellationButton)
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        val name = intent.extras!!.get("Name").toString()
        val clinic = intent.extras!!.get("Clinic").toString()
        val date = intent.extras!!.get("Date").toString()
        val time = intent.extras!!.get("Time").toString()
        cancellationName.setText(name)
        cancellationClinic.setText(clinic)
        cancellationDate.setText(date)
        cancellationTime.setText(time)
        backButton.setOnClickListener {
            val intent = Intent(this@BookingCancellationConfirmationScreen,
                BookingCancellation::class.java)
            startActivity(intent)
            finish()
        }
        cancellationButton.setOnClickListener {
            db.collection("Users").document(uid).get().addOnSuccessListener { doc ->
                val clinics = doc.get("Clinics") as HashMap<String, ArrayList<String>>
                val datetime = doc.get("DateTime") as ArrayList<String>
                val dateString = cancellationDate.text.toString()
                    .substring(6) + "-" + cancellationDate.text
                    .toString().substring(3,5) + "-" + cancellationDate.text
                    .toString().substring(0,2) + " " + cancellationTime
                    .text.toString()
                val mcr = clinics[dateString]!![1]
                datetime.remove(dateString)
                clinics.remove(dateString)
                db.collection("Users")
                    .document(uid)
                    .set(hashMapOf("Clinics" to clinics, "DateTime" to datetime),
                        SetOptions.merge())
                db.collection("Queue").document(mcr)
                    .collection(cancellationDate.text.toString().replace('/','-'))
                    .document(cancellationTime.text.toString())
                    .delete()
                val currentDate: Date = Calendar.getInstance().time
                val currentDateFormat = SimpleDateFormat("dd-MM-yy", Locale.ENGLISH)
                if (currentDateFormat.format(currentDate) == cancellationDate.text.toString()) {
                    db.collection("LiveQueue").document(mcr).get()
                        .addOnSuccessListener { task ->
                            val queue = task.get("Queueid") as ArrayList<String>
                            val timinglist = task.get("TimingList") as ArrayList<String>
                            val position = timinglist.indexOf(cancellationTime.text.toString())
                            queue.removeAt(position)
                            timinglist.removeAt(position)
                            db.collection("LiveQueue").document(mcr)
                                .update(mapOf("Queueid" to queue, "TimingList" to timinglist))
                    }
                }
                Snackbar.make(cancellationTime,"Appointment has been successfully cancelled"
                    ,Snackbar.LENGTH_SHORT).show()
                Handler().postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                },2500)
            }
        }
    }
}