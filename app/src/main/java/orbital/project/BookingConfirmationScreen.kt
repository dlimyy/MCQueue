package orbital.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.w3c.dom.Text
import java.sql.Date

class BookingConfirmationScreen : AppCompatActivity() {
    private lateinit var confirmationDate: TextView
    private lateinit var confirmationClinic : TextView
    private lateinit var confirmationDoctor : TextView
    private lateinit var confirmationTiming: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_confirmation_screen)
        confirmationDate = findViewById(R.id.confirmationDate)
        confirmationClinic = findViewById(R.id.confirmationClinic)
        confirmationDoctor = findViewById(R.id.confirmationDoctor)
        confirmationTiming = findViewById(R.id.confirmationTiming)

        confirmationDate.text = intent.extras!!.getString("date")
        confirmationClinic.text = intent.extras!!.getString("clinic")
        confirmationDoctor.text = intent.extras!!.getString("doctor")
        confirmationTiming.text = intent.extras!!.getString("time")
    }
}