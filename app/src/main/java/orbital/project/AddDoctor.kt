package orbital.project

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.Int
import kotlin.collections.ArrayList
import com.google.firebase.auth.FirebaseAuth


class AddDoctor : AppCompatActivity() {

    private lateinit var addIcon : ImageView
    private lateinit var removeIcon : ImageView
    private lateinit var submitButton: Button
    private lateinit var timeInterval1 : CardView
    private lateinit var timeInterval2 : CardView
    private lateinit var timeInterval3 : CardView
    private lateinit var startTime1 : Button
    private lateinit var startTime2 : Button
    private lateinit var startTime3 : Button
    private lateinit var endTime1 : Button
    private lateinit var endTime2 : Button
    private lateinit var endTime3 : Button
    private lateinit var doctorName : TextInputEditText
    private lateinit var mcrNumber : TextInputEditText
    private lateinit var doctorNameLayout : TextInputLayout
    private lateinit var mcrNumberLayout : TextInputLayout
    private var counter : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_doctor)

        addIcon = findViewById(R.id.addTimeInterval)
        removeIcon = findViewById(R.id.removeTimeInterval)
        submitButton = findViewById(R.id.addDoctorSubmitButton)
        timeInterval1 = findViewById(R.id.timeInterval1)
        timeInterval2 = findViewById(R.id.timeInterval2)
        timeInterval3 = findViewById(R.id.timeInterval3)
        timeInterval2.visibility = View.GONE
        timeInterval3.visibility = View.GONE
        startTime1 = timeInterval1.findViewById(R.id.startTimeButton)
        startTime2 = timeInterval2.findViewById(R.id.startTimeButton)
        startTime3 = timeInterval3.findViewById(R.id.startTimeButton)
        endTime1 = timeInterval1.findViewById(R.id.endTimeButton)
        endTime2 = timeInterval2.findViewById(R.id.endTimeButton)
        endTime3 = timeInterval3.findViewById(R.id.endTimeButton)
        doctorName = findViewById(R.id.doctorNameAddDoctor)
        mcrNumber = findViewById(R.id.MCRnumberAddDoctor)
        doctorNameLayout = findViewById(R.id.doctorNameAddDoctorLayout)
        mcrNumberLayout = findViewById(R.id.MCRnumberAddDoctorLayout)
    }

    override fun onStart() {
        super.onStart()
        doctorNameTextChange()
        MCRTextChange()
        addIcon.setOnClickListener {
            when(counter)  {
                0 -> {
                    timeInterval2.visibility = View.VISIBLE
                    counter++
                }
                1 -> {
                    timeInterval3.visibility = View.VISIBLE
                    counter++
                } else -> {

                }
            }
        }
        removeIcon.setOnClickListener {
            when(counter)  {
                2 -> {
                    timeInterval3.visibility = View.GONE
                    counter--
                }
                1 -> {
                    timeInterval2.visibility = View.GONE
                    counter--
                } else -> {

                }
            }
        }

        startTime1.setOnClickListener {
            var hour : Int = 0
            var minute : Int = 0

            val onTimeSetListener =
                OnTimeSetListener { _, selectedHour, selectedMinute ->
                    hour = selectedHour
                    minute = selectedMinute
                    startTime1.text = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hour,
                        minute
                    )
                }

            val style : Int = AlertDialog.THEME_HOLO_DARK

            val timePickerDialog =
                TimePickerDialog(this, style, onTimeSetListener, hour, minute, true)

            timePickerDialog.setTitle("Select Time")
            timePickerDialog.show()
        }

        startTime2.setOnClickListener {
            var hour : Int = 0
            var minute : Int = 0

            val onTimeSetListener =
                OnTimeSetListener { _, selectedHour, selectedMinute ->
                    hour = selectedHour
                    minute = selectedMinute
                    startTime2.text = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hour,
                        minute
                    )
                }

            val style : Int = AlertDialog.THEME_HOLO_DARK

            val timePickerDialog =
                TimePickerDialog(this, style, onTimeSetListener, hour, minute, true)

            timePickerDialog.setTitle("Select Time")
            timePickerDialog.show()
        }

        startTime3.setOnClickListener {
            var hour : Int = 0
            var minute : Int = 0

            val onTimeSetListener =
                OnTimeSetListener { _, selectedHour, selectedMinute ->
                    hour = selectedHour
                    minute = selectedMinute
                    startTime3.text = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hour,
                        minute
                    )
                }

            val style : Int = AlertDialog.THEME_HOLO_DARK

            val timePickerDialog =
                TimePickerDialog(this, style, onTimeSetListener, hour, minute, true)

            timePickerDialog.setTitle("Select Time")
            timePickerDialog.show()
        }

        endTime1.setOnClickListener {
            var hour : Int = 0
            var minute : Int = 0

            val onTimeSetListener =
                OnTimeSetListener { _, selectedHour, selectedMinute ->
                    hour = selectedHour
                    minute = selectedMinute
                    endTime1.text = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hour,
                        minute
                    )
                }

            val style : Int = AlertDialog.THEME_HOLO_DARK

            val timePickerDialog =
                TimePickerDialog(this, style, onTimeSetListener, hour, minute, true)

            timePickerDialog.setTitle("Select Time")
            timePickerDialog.show()
        }

        endTime2.setOnClickListener {
            var hour : Int = 0
            var minute : Int = 0

            val onTimeSetListener =
                OnTimeSetListener { _, selectedHour, selectedMinute ->
                    hour = selectedHour
                    minute = selectedMinute
                    endTime2.text = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hour,
                        minute
                    )
                }

            val style : Int = AlertDialog.THEME_HOLO_DARK

            val timePickerDialog =
                TimePickerDialog(this, style, onTimeSetListener, hour, minute, true)

            timePickerDialog.setTitle("Select Time")
            timePickerDialog.show()
        }

        endTime3.setOnClickListener {
            var hour : Int = 0
            var minute : Int = 0

            val onTimeSetListener =
                OnTimeSetListener { _, selectedHour, selectedMinute ->
                    hour = selectedHour
                    minute = selectedMinute
                    endTime3.text = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hour,
                        minute
                    )
                }

            val style : Int = AlertDialog.THEME_HOLO_DARK

            val timePickerDialog =
                TimePickerDialog(this, style, onTimeSetListener, hour, minute, true)

            timePickerDialog.setTitle("Select Time")
            timePickerDialog.show()
        }

        submitButton.setOnClickListener {
            val combinedTimeArray = timeFormat()
            val uid : String = FirebaseAuth.getInstance().currentUser!!.uid
            if (isValidDoctorName(doctorName) && isValidMCR(mcrNumber)) {
                Toast.makeText(this,"SAd",Toast.LENGTH_SHORT).show()
            }
            Log.d("WHat",doctorName.text!!.length.toString())
            Log.d("@",mcrNumber.text!!.length.toString())

        }

    }

    private fun timeFormat() : ArrayList<String> {
        val tempArray = ArrayList<String>()
        if (startTime1.text.toString() != "Start" && endTime1.text.toString() != "End") {
            tempArray.add(startTime1.text.toString() + "-" + endTime1.text.toString())
        }
        if (startTime2.text.toString() != "Start" && endTime2.text.toString() != "End") {
            tempArray.add(startTime2.text.toString() + "-" + endTime2.text.toString())
        }
        if (startTime3.text.toString() != "Start" && endTime3.text.toString() != "End") {
            tempArray.add(startTime3.text.toString() + "-" + endTime3.text.toString())
        }
        return tempArray
    }

    private fun doctorNameTextChange() {
        doctorName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                doctorNameLayout.error = null;
            }
        })
    }

    private fun MCRTextChange() {
        mcrNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mcrNumberLayout.error = null;
            }
        })
    }

    private fun isValidDoctorName(name: TextInputEditText) : Boolean {
        if (TextUtils.isEmpty(name.toString())) {
            doctorNameLayout.error = "Please enter doctor's name"
            return false
        }

        return true
    }

    private fun isValidMCR(MCR: TextInputEditText) : Boolean {
        if (TextUtils.isEmpty(MCR.toString())) {
            mcrNumberLayout.error = "Please enter MCR"
            return false
        }

        return true
    }
}