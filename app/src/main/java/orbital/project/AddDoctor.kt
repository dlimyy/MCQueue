package orbital.project

import android.Manifest
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.os.Handler
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
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


class AddDoctor : AppCompatActivity() {

    private lateinit var backButton : ImageView
    private lateinit var doctorProfilePic : ImageView
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
    private val monArray : ArrayList<String> = arrayListOf()
    private val tueArray : ArrayList<String> = arrayListOf()
    private val wedArray : ArrayList<String> = arrayListOf()
    private val thuArray : ArrayList<String> = arrayListOf()
    private val friArray : ArrayList<String> = arrayListOf()
    private val satArray : ArrayList<String> = arrayListOf()
    private val sunArray : ArrayList<String> = arrayListOf()
    private lateinit var mcrValidator :McrValidator
    private val chipArray = ArrayList<Chip>()
    private val storageRef = FirebaseStorage.getInstance().reference
    private val daysOfWeek = arrayOf("Monday","Tuesday","Wednesday","Thursday"
        ,"Friday","Saturday","Sunday")
    private lateinit var genderError : TextView
    private lateinit var languageError : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_doctor)
        backButton = findViewById(R.id.navigateAddDoctorToDoctorInfo)
        doctorProfilePic = findViewById(R.id.doctorProfilePic)
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
        mcrValidator = McrValidator()
        doctorInfoDateTimeError.visibility = View.GONE
        val adaptor = ArrayAdapter<String>(this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,daysOfWeek)
        day.setAdapter(adaptor)
        genderError = findViewById(R.id.addDoctorGender)
        languageError = findViewById(R.id.addDoctorLanguage)
        genderError.visibility = View.GONE
        languageError.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        doctorNameTextChange()
        dayTextChange()
        mcrValidator.textChange(mcrNumber,mcrNumberLayout)
        toggleGender.addOnButtonCheckedListener { _, _, _ ->
            genderError.visibility = View.GONE
        }

        doctorInfoLanguagesFirstRow.addOnButtonCheckedListener { _, _, _ ->
            languageError.visibility = View.GONE
        }

        doctorInfoLanguagesSecondRow.addOnButtonCheckedListener { _, _, _ ->
            languageError.visibility = View.GONE
        }
        doctorInfoLanguagesThirdRow.addOnButtonCheckedListener { _, _, _ ->
            languageError.visibility = View.GONE
        }

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
            if (!isDayTimeValid(day,startTime,endTime)) {
                doctorInfoDateTimeError.text = "Please select non-overlapping time range"
                doctorInfoDateTimeError.visibility = View.VISIBLE
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
            val validMCR = mcrValidator.layoutErrorChange(mcrNumber,mcrNumberLayout)
            val isGender = checkGender()
            val languageArray = getLanguageArray()
            val daysArray = getDayArray()
            val bitImage = (doctorProfilePic.drawable as BitmapDrawable).bitmap
            val outputStream = ByteArrayOutputStream()
            bitImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val data  = outputStream.toByteArray()
            val imageRef = storageRef.child("Images")


            if (validDoc && validMCR && isGender && languageArray.size > 0 && chipArray.size > 0) {
                getWorkingHours()
                val ref = imageRef.child(mcrNumber.text.toString())
                val docData = hashMapOf("Name" to doctorName.text.toString(), "Gender"
                        to findViewById<Button>(toggleGender.checkedButtonId).text.toString()
                    , "Clinic uid" to uid, "Languages" to languageArray, "Days" to daysArray,
                    "monArray" to monArray, "tueArray" to tueArray, "wedArray" to wedArray,
                    "thuArray" to thuArray, "friArray" to friArray, "satArray" to satArray
                    ,"sunArray" to sunArray)
                ref.putBytes(data).addOnSuccessListener {
                    Snackbar.make(submitButton,"Submitted successfully",Snackbar.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    monArray.clear()
                    tueArray.clear()
                    wedArray.clear()
                    thuArray.clear()
                    friArray.clear()
                    satArray.clear()
                    sunArray.clear()
                    Snackbar.make(submitButton,"Failure",Snackbar.LENGTH_SHORT).show()
                }
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
                db.collection("Doctors").document(mcrNumber.text.toString()).set(docData).addOnFailureListener {
                    monArray.clear()
                    tueArray.clear()
                    wedArray.clear()
                    thuArray.clear()
                    friArray.clear()
                    satArray.clear()
                    sunArray.clear()
                }.addOnSuccessListener {
                    Handler().postDelayed({
                        startActivity(Intent(this, DoctorHomePage::class.java))
                        finishAffinity()
                    },2500)
                }

                monArray.clear()
                tueArray.clear()
                wedArray.clear()
                thuArray.clear()
                friArray.clear()
                satArray.clear()
                sunArray.clear()
            }
        }

        backButton.setOnClickListener {
            super.onBackPressed()
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

    private fun dayTextChange() {
        day.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                doctorInfoDateTimeError.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable?) {

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

    private fun checkGender() : Boolean {
        if (toggleGender.checkedButtonId == View.NO_ID) {
            genderError.visibility = View.VISIBLE
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
            chipArray.remove(chip)
        }
        chipGroup.addView(chip)
        chipArray.add(chip)
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
        if (arraylist.size == 0) {
            languageError.visibility = View.VISIBLE
        }
        return arraylist
    }

    private fun getDayArray() : ArrayList<String> {
        val arraylist = arrayListOf<String>()
        for (i in 0 until chipArray.size) {
            if (!arraylist.contains(chipArray[i].text.toString().substring(0,3))) {
                arraylist.add(chipArray[i].text.toString().substring(0,3))
            }
        }
        if (arraylist.size == 0) {
            doctorInfoDateTimeError.text = "Please select a timing"
            doctorInfoDateTimeError.visibility = View.VISIBLE
        }
        return arraylist
    }

    private fun getWorkingHours() {
        for (chip in chipArray) {
            when (chip.text.toString().substring(0,3)) {
                "Mon" -> monArray.add(chip.text.toString().substring(4))
                "Tue" -> tueArray.add(chip.text.toString().substring(4))
                "Wed" -> wedArray.add(chip.text.toString().substring(4))
                "Thu" -> thuArray.add(chip.text.toString().substring(4))
                "Fri" -> friArray.add(chip.text.toString().substring(4))
                "Sat" -> satArray.add(chip.text.toString().substring(4))
                "Sun" -> sunArray.add(chip.text.toString().substring(4))
                else -> {

                }
            }
        }

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
            doctorProfilePic.setImageURI(uriContent)
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
                    Snackbar.make(doctorProfilePic, "Please enable permissions for camera"
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
                    Snackbar.make(doctorProfilePic, "Please enable permissions under Files and Media"
                        , Snackbar.LENGTH_LONG).show()
                }
            }

            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun isDayTimeValid(day : TextView, startTime : Button, endTime : Button) : Boolean {
        var minTime : Int? = null
        var maxTime : Int? = null
        val startTimeInt = Integer.parseInt(startTime.text.toString().substring(0,2)
                + startTime.text.toString().substring(3))
        val endTimeInt = Integer.parseInt(endTime.text.toString().substring(0,2)
                + endTime.text.toString().substring(3))
        if (startTimeInt >= endTimeInt) {
            return false
        }
        for (chip in chipArray) {
            if(chip.text.toString().substring(0,3) == day.text.toString().substring(0,3)) {
                val starting : Int = Integer.parseInt(chip.text.toString().substring(4,6)
                        + chip.text.toString().substring(7,9))
                val ending : Int = Integer.parseInt(chip.text.toString().substring(10,12)
                        + chip.text.toString().substring(13))

                minTime = starting
                maxTime = ending
                if (minTime <= startTimeInt && maxTime >= endTimeInt) {
                    return false
                }
                if (startTimeInt >= minTime && startTimeInt <= maxTime) {
                    return false
                }
                if (endTimeInt >= minTime && endTimeInt <= maxTime) {
                    return false
                }
                if (minTime >= startTimeInt && endTimeInt >= maxTime) {
                    return false
                }
            }
        }
        return true
    }

    companion object {
        const val CAMERA_REQUEST = 100
        const val STORAGE_REQUEST = 150
    }
}