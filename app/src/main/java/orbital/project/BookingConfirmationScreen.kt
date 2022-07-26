package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BookingConfirmationScreen : AppCompatActivity() {
    private lateinit var confirmationDate: TextInputEditText
    private lateinit var confirmationClinic : TextInputEditText
    private lateinit var confirmationDoctor : TextInputEditText
    private lateinit var confirmationTiming: TextInputEditText
    private lateinit var confirmationButton : Button
    private lateinit var backButton : ImageView
    private lateinit var mcrNumber : String
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_confirmation_screen)
        confirmationDate = findViewById(R.id.confirmationDate)
        confirmationClinic = findViewById(R.id.confirmationClinic)
        confirmationDoctor = findViewById(R.id.confirmationDoctor)
        confirmationTiming = findViewById(R.id.confirmationTiming)
        confirmationButton = findViewById(R.id.nextBookingScreenConfirmation)
        backButton = findViewById(R.id.navigateConfirmationtoTiming)
        val currentDate = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("dd/MM/yy", Locale.ENGLISH)
        val date = dateFormatter.format(currentDate)
        confirmationDate.setText(intent.extras!!.getString("date"))
        confirmationClinic.setText(intent.extras!!.getString("clinic"))
        confirmationDoctor.setText(intent.extras!!.getString("doctor"))
        confirmationTiming.setText(intent.extras!!.getString("time"))
        mcrNumber = intent.extras!!.getString("mcrNumber").toString()
        confirmationButton.setOnClickListener {
            db.collection("Queue").document(mcrNumber)
                .collection(confirmationDate.text.toString().replace('/','-'))
                .document(confirmationTiming.text.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Snackbar.make(confirmationTiming
                            ,"Sorry, please make a booking at another available slot"
                            ,Snackbar.LENGTH_SHORT).show()
                    } else {
                        val uid : String = FirebaseAuth.getInstance().currentUser!!.uid
                        var tempArray = ArrayList<String>()
                        var age = "0"
                        var name = "Anon"
                        db.collection("Users").document(uid).get()
                            .addOnSuccessListener { doc ->
                                age = doc.get("Age") as String
                                name = doc.get("Name") as String
                                val yearMonthDate
                                = confirmationDate.text.toString()
                                    .substring(6) + "-" + confirmationDate.text
                                    .toString().substring(3,5) + "-" + confirmationDate.text
                                    .toString().substring(0,2) + " " + confirmationTiming
                                    .text.toString()
                                var clinicWithDate
                                = hashMapOf<String, ArrayList<String>>()
                                if (doc.get("DateTime") == null || doc.get("Clinics") == null) {
                                    tempArray.add(yearMonthDate)
                                    clinicWithDate[yearMonthDate] = arrayListOf(confirmationClinic
                                        .text.toString(), mcrNumber)
                                } else {
                                    tempArray = doc.get("DateTime") as ArrayList<String>
                                    tempArray.add(yearMonthDate)
                                    tempArray.sort()
                                    clinicWithDate = doc.get("Clinics") as HashMap<String, ArrayList<String>>
                                    clinicWithDate[yearMonthDate] = arrayListOf(confirmationClinic
                                        .text.toString(), mcrNumber)
                                }
                                db.collection("Users").document(uid)
                                    .set(hashMapOf("Clinics" to clinicWithDate,
                                        "DateTime" to tempArray), SetOptions.merge())

                                db.collection("Queue").document(mcrNumber)
                                    .collection(confirmationDate.text.toString().replace('/','-'))
                                    .document(confirmationTiming.text.toString())
                                    .set(hashMapOf("Age" to age, "Name" to name, "uid" to uid))

                                if (date == confirmationDate.text.toString()) {
                                    db.collection("LiveQueue")
                                        .document(mcrNumber).get().addOnSuccessListener {
                                            val queueId = it.get("Queueid") as ArrayList<String>
                                            val timingList = it.get("TimingList") as ArrayList<String>
                                            timingList.add(confirmationTiming.text.toString())
                                            timingList.sort()
                                            val pos = timingList
                                                .indexOf(confirmationTiming.text.toString())
                                            queueId.add(pos,uid)
                                            db.collection("LiveQueue")
                                                .document(mcrNumber)
                                                .update(mapOf("Queueid" to  queueId
                                                    , "TimingList" to timingList))
                                        }
                                }

                        }
                        Snackbar.make(confirmationTiming,"Appointment has been successfully booked"
                            ,Snackbar.LENGTH_SHORT).show()
                        Handler().postDelayed({
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        },2500)
                    }
                }
        }
        backButton.setOnClickListener {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }
}