package orbital.project

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.core.app.NotificationCompat

class StartScreen : AppCompatActivity() {
    private lateinit var patientlogin : Button
    private lateinit var doctorlogin : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val channelId = resources.getString(R.string.channel_id)
            val name = getString(R.string.app_name)
            val descriptionText = "MCQueue Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = descriptionText
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            channel.enableVibration(true)
            channel.setSound(
                Settings.System.DEFAULT_NOTIFICATION_URI,
                Notification.AUDIO_ATTRIBUTES_DEFAULT)
            // Register the channel with the system
            val notificationManager = getSystemService(NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        patientlogin = findViewById(R.id.patient)
        patientlogin.setOnClickListener {
            val intent: Intent = Intent(this, Loginpage::class.java)
            startActivity(intent)
            finish()
        }
        doctorlogin = findViewById(R.id.clinic)
        doctorlogin.setOnClickListener {
            val intent : Intent = Intent(this, DoctorLoginpage::class.java)
            startActivity(intent)
            finish()
        }
    }
}