package orbital.project

import android.Manifest
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.size
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*


class AddDoctor : AppCompatActivity() {

    private lateinit var doctorProfilepic : ImageView
    private lateinit var submitButton: Button
    private lateinit var startTime : Button
    private lateinit var endTime : Button
    private lateinit var doctorName : TextInputEditText
    private lateinit var mcrNumber : TextInputEditText
    private lateinit var doctorNameLayout : TextInputLayout
    private lateinit var mcrNumberLayout : TextInputLayout
    private lateinit var toggleGender : MaterialButtonToggleGroup
    private lateinit var chipGroup : ChipGroup
    private lateinit var addButton: Button
    private lateinit var day : AutoCompleteTextView
    private lateinit var doctorInfoDateTimeError : TextView
    private lateinit var doctorInfoLanguagesFirstRow : MaterialButtonToggleGroup
    private lateinit var doctorInfoLanguagesSecondRow : MaterialButtonToggleGroup
    private lateinit var doctorInfoLanguagesThirdRow : MaterialButtonToggleGroup
    private lateinit var chooseProfileButton : Button
    private val daysOfWeek = arrayOf("Monday","Tuesday","Wednesday","Thursday"
        ,"Friday","Saturday","Sunday")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_doctor)
        doctorProfilepic = findViewById(R.id.doctorProfilePic)
        chooseProfileButton = findViewById(R.id.doctorInfoChooseProfilePicture)
        submitButton = findViewById(R.id.addDoctorSubmitButton)
        startTime = findViewById(R.id.startTimeButton)
        endTime = findViewById(R.id.endTimeButton)
        doctorName = findViewById(R.id.doctorNameAddDoctor)
        mcrNumber = findViewById(R.id.MCRnumberAddDoctor)
        doctorNameLayout = findViewById(R.id.doctorNameAddDoctorLayout)
        mcrNumberLayout = findViewById(R.id.MCRnumberAddDoctorLayout)
        toggleGender = findViewById(R.id.doctorGenderButtonToggle)
        chipGroup = findViewById(R.id.doctorInfoChipGroup)
        addButton = findViewById(R.id.addDoctorInfoAddButton)
        day = findViewById(R.id.doctorInfoDay)
        doctorInfoDateTimeError = findViewById(R.id.doctorInfoDateTimeError)
        doctorInfoLanguagesFirstRow = findViewById(R.id.doctorInfoLanguagesFirstRow)
        doctorInfoLanguagesSecondRow = findViewById(R.id.doctorInfoLanguagesSecondRow)
        doctorInfoLanguagesThirdRow = findViewById(R.id.doctorInfoLanguagesThirdRow)
        doctorInfoDateTimeError.visibility = View.GONE
        val adaptor = ArrayAdapter<String>(this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,daysOfWeek)
        day.setAdapter(adaptor)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        doctorNameTextChange()
        MCRTextChange()

        startTime.setOnClickListener {
            doctorInfoDateTimeError.visibility = View.GONE
            var hour : Int = 0
            var minute : Int = 0
            val onTimeSetListener =
                OnTimeSetListener { _, selectedHour, selectedMinute ->
                    hour = selectedHour
                    minute = selectedMinute
                    startTime.text = String.format(
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

        endTime.setOnClickListener {
            doctorInfoDateTimeError.visibility = View.GONE
            var hour : Int = 0
            var minute : Int = 0
            val onTimeSetListener =
                OnTimeSetListener { _, selectedHour, selectedMinute ->
                    hour = selectedHour
                    minute = selectedMinute
                    endTime.text = String.format(
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

        addButton.setOnClickListener {
            if (TextUtils.isEmpty(day.text.toString())) {
                doctorInfoDateTimeError.text = "Please Select a valid date"
                doctorInfoDateTimeError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (startTime.text.toString()=="Start Time" || endTime.text.toString()=="End Time") {
                doctorInfoDateTimeError.text = "Please Select a valid time"
                doctorInfoDateTimeError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (chipGroup.size > 15) {
                return@setOnClickListener
            }
            val chipString = day.text.toString().substring(0,3) + " " + startTime.text.toString() + "-" + endTime.text.toString()
            addChip(chipString)
        }

        chooseProfileButton.setOnClickListener {
            showBottomSheetDialog()
        }

        submitButton.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val uid : String = FirebaseAuth.getInstance().currentUser!!.uid
            val validDoc = isValidDoctorName(doctorName)
            val validMCR = isValidMCR(mcrNumber)
            val isGender = checkGender()
            val languageArray = getLanguageArray()
            val chipArray = getDayAndTime()

            if (validDoc && validMCR && isGender && languageArray.size > 0 && chipArray.size > 0) {
                val docData = hashMapOf("Name" to doctorName.text.toString(), "Gender"
                        to findViewById<Button>(toggleGender.checkedButtonId).text.toString()
                    , "Clinic uid" to uid, "Languages" to languageArray, "DayAndTime" to chipArray)
                db.collection("Doctors").document(mcrNumber.text.toString()).set(docData)
                db.collection("Clinics").document(uid).get()
                    .addOnSuccessListener{ document ->
                        if (document == null) {
                            Log.d("Document error","Document does not exist")
                            return@addOnSuccessListener
                        }
                        if (document.get("Doctor") == null) {
                            val docArray = arrayListOf<String>(mcrNumber.text.toString())
                            db.collection("Clinics").document(uid)
                                .set(hashMapOf("Doctor" to docArray), SetOptions.merge())
                        }
                        else {
                            val docArray = document.get("Doctor") as ArrayList<String>
                            docArray.add(mcrNumber.text.toString())
                            db.collection("Clinics").document(uid)
                                .set(hashMapOf("Doctor" to docArray), SetOptions.merge())
                        }
                    }
            }
        }

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
        if (TextUtils.isEmpty(name.text.toString())) {
            doctorNameLayout.error = "Please enter doctor's name"
            return false
        }
        return true
    }

    private fun isValidMCR(MCR: TextInputEditText) : Boolean {
        if (TextUtils.isEmpty(MCR.text.toString())) {
            mcrNumberLayout.error = "Please enter MCR"
            return false
        }
        return true
    }

    private fun checkGender() : Boolean {
        if (toggleGender.checkedButtonId == View.NO_ID) {
            return false
        }
        return true
    }

    private fun addChip(text : String) {
        val chip = Chip(this)
        chip.text = text
        chip.isCloseIconVisible = true
        chip.id = View.generateViewId()
        chip.setOnCloseIconClickListener {
            chipGroup.removeView(chip)
        }
        chipGroup.addView(chip)
    }

    private fun getLanguageArray() : ArrayList<String> {
        val arraylist = ArrayList<String>()
        for (language in doctorInfoLanguagesFirstRow.checkedButtonIds) {
            arraylist.add(findViewById<Button>(language).text.toString())
        }
        for (language in doctorInfoLanguagesSecondRow.checkedButtonIds) {
            arraylist.add(findViewById<Button>(language).text.toString())
        }
        for (language in doctorInfoLanguagesThirdRow.checkedButtonIds) {
            arraylist.add(findViewById<Button>(language).text.toString())
        }
        return arraylist
    }

    private fun getDayAndTime() : ArrayList<String> {
        val arraylist = ArrayList<String>()
        for (pos in 0 until chipGroup.childCount) {
            Log.d("Trying",chipGroup.getChildAt(pos).id.toString())
            arraylist.add(findViewById<Chip>(chipGroup.getChildAt(pos).id).text.toString())
            Log.d("FINALLLY",findViewById<Chip>(chipGroup.getChildAt(pos).id).text.toString())
        }
        return arraylist
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)
        val camera = bottomSheetDialog.findViewById<LinearLayout>(R.id.cameraBottomSheetDialog)
        val gallery = bottomSheetDialog.findViewById<LinearLayout>(R.id.galleryBottomSheetDialog)
        camera?.setOnClickListener {
            if (!checkCameraPermission()) {
               requestCameraPermission()
            } else {
                cropImage.launch(
                    options {
                        setGuidelines(CropImageView.Guidelines.ON)
                        setCropShape(CropImageView.CropShape.OVAL)
                        setFixAspectRatio(true)
                        setAspectRatio(1,1)
                        setImageSource(includeCamera = true, includeGallery = false)
                    }
                )
            }
            bottomSheetDialog.dismiss()
        }
        gallery?.setOnClickListener {
            if (!checkStoragePermission()) {
                Log.d("Here","CLicked")
                requestStoragePermission()
            } else {
                Log.d("There","CLicked")
                cropImage.launch(
                    options {
                        setGuidelines(CropImageView.Guidelines.ON)
                        setCropShape(CropImageView.CropShape.OVAL)
                        setFixAspectRatio(true)
                        setAspectRatio(1,1)
                        setImageSource(includeCamera = false, includeGallery = true)
                    }
                )
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            doctorProfilepic.setImageURI(uriContent)
        } else {
            // an error occurred
            val exception = result.error
        }
    }

    // checking camera permissions
    private fun checkCameraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        return result
    }

    // checking storage permissions
    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Requesting  gallery permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestStoragePermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_REQUEST)
    }

    // Requesting camera permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST)
    }

    // Action after requesting permission
    override fun onRequestPermissionsResult(requestCode: Int,
                                           permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST -> {
                if (grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cropImage.launch(
                        options {
                            setGuidelines(CropImageView.Guidelines.ON)
                            setCropShape(CropImageView.CropShape.OVAL)
                            setFixAspectRatio(true)
                            setAspectRatio(1,1)
                            setImageSource(includeCamera = true, includeGallery = false)
                        }
                    )
                } else {
                    Snackbar.make(doctorProfilepic, "Please enable permissions for camera"
                        , Snackbar.LENGTH_LONG).show()
                }
            }
            STORAGE_REQUEST -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    cropImage.launch(
                        options {
                            setGuidelines(CropImageView.Guidelines.ON)
                            setCropShape(CropImageView.CropShape.OVAL)
                            setFixAspectRatio(true)
                            setAspectRatio(1,1)
                            setImageSource(includeCamera = false, includeGallery = true)
                        }
                    )
                } else {
                    Snackbar.make(doctorProfilepic, "Please enable permissions under Files and Media"
                        , Snackbar.LENGTH_LONG).show()
                }
            }

            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    companion object {
        const val CAMERA_REQUEST = 100
        const val STORAGE_REQUEST = 150
    }
}