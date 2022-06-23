package orbital.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    private lateinit var bookingcalendar : ImageView
    private lateinit var queueicon : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bookingcalendar = findViewById(R.id.bookingcalendar)
        queueicon = findViewById(R.id.queueicon)
        bookingcalendar.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }
        queueicon.setOnClickListener {
            startActivity(Intent(this,QueueLocator::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        bookingcalendar = findViewById(R.id.bookingcalendar)
        queueicon = findViewById(R.id.queueicon)
        bookingcalendar.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java))
        }
        queueicon.setOnClickListener {
            startActivity(Intent(this,QueueLocator::class.java))
        }
    }
}