package orbital.project.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import orbital.project.helper_classes.GlideApp
import orbital.project.R

class DoctorProfilePageSearch : AppCompatActivity() {
    private lateinit var name : TextInputEditText
    private lateinit var gender : TextInputEditText
    private lateinit var clinic : TextInputEditText
    private lateinit var languagesArray: ArrayList<String>
    private lateinit var daysArray : ArrayList<String>
    private lateinit var languageChipGroup: ChipGroup
    private lateinit var dayChipGroup: ChipGroup
    private lateinit var backButton : ImageView
    private lateinit var profilePic : ImageView
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile_page_search)
        name = findViewById(R.id.doctorProfilePageName)
        gender = findViewById(R.id.doctorProfilePageGender)
        clinic = findViewById(R.id.doctorProfilePageClinic)
        languageChipGroup = findViewById(R.id.doctorProfileLanguageGroup)
        dayChipGroup = findViewById(R.id.doctorProfileDays)
        profilePic = findViewById(R.id.profilePageSearchpic)
        languagesArray = ArrayList()
        daysArray = ArrayList()
        backButton = findViewById(R.id.navigateProfileToFindDoctor)
        val mcrNumber = intent.extras!!.get("mcrNumber").toString()
        GlideApp.with(profilePic).load(
            Firebase.storage.reference.child("Images/"
                + mcrNumber)).placeholder(R.drawable.doctor__1_).into(profilePic)
        db.collection("Doctors").document(mcrNumber).get().addOnSuccessListener { it ->
            name.setText(it.get("Name") as String)
            gender.setText(it.get("Gender") as String)
            languagesArray = it.get("Languages") as ArrayList<String>
            daysArray = it.get("Days") as ArrayList<String>
            val clinicNumber = it.get("Clinic uid") as String
            db.collection("Clinics").document(clinicNumber).get().addOnSuccessListener {
                    result ->
                clinic.setText(result.get("Name") as String)
            }
            languageChipsAdd()
            dayChipsAdd()
        }
        backButton.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun languageChipsAdd() {
        for (language in languagesArray) {
            languageChipGroup.setChipSpacing(20)
            languageAddChip(language)
        }
    }

    private fun languageAddChip(text : String) {
        val chip = Chip(this)
        chip.text = text
        chip.id = View.generateViewId()
        chip.setChipBackgroundColorResource(R.color.white)
        languageChipGroup.addView(chip)
    }

    private fun dayChipsAdd() {
        for (day in daysArray) {
            dayChipGroup.setChipSpacing(20)
            daysAddChip(day)
        }
    }

    private fun daysAddChip(text : String) {
        val chip = Chip(this)
        chip.text = text
        chip.id = View.generateViewId()
        chip.setChipBackgroundColorResource(R.color.white)
        dayChipGroup.addView(chip)
    }
}