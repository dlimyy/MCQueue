package orbital.project

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList


class SearchDoctor : AppCompatActivity() {

    interface MyCallback {
        fun onCallback(docArray: ArrayList<Doctor>)
    }


    private lateinit var doctorRecyclerView : RecyclerView
    private lateinit var adaptor: SearchDoctorAdaptor
    private val docArray : ArrayList<Doctor> = ArrayList()
    private var filterArray : ArrayList<Doctor> = ArrayList()
    private lateinit var doctorSearch : SearchView
    private lateinit var genderbutton : Button
    private lateinit var languagebutton : Button
    private lateinit var dayButton: Button
    private lateinit var backButton : ImageView
    private var currentSearchText = ""
    private val db = FirebaseFirestore.getInstance()
    private val storageRef = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_doctor)
        doctorRecyclerView = findViewById(R.id.searchDoctorRecyclerView)
        doctorSearch = findViewById(R.id.doctorSearchBar)
        genderbutton = findViewById(R.id.searchDoctorGenderButton)
        languagebutton = findViewById(R.id.searchDoctorLanguageButton)
        dayButton = findViewById(R.id.searchDoctorDayButton)
        backButton = findViewById(R.id.navigateSearchDoctorMainActivity)
        readData(object : MyCallback {
            override fun onCallback(docArray: ArrayList<Doctor>) {
            }
        })
        doctorRecyclerView.layoutManager = LinearLayoutManager(this)
        adaptor = SearchDoctorAdaptor(filterArray)
        doctorRecyclerView.adapter = adaptor
        adaptor.setOnItemClickListener(object : SearchDoctorAdaptor.OnItemClickListener{
            override fun onItemClick(position: Int) {
                intent = Intent(this@SearchDoctor,DoctorProfilePageSearch::class.java)
                intent.putExtra("mcrNumber",filterArray[position].mcrNumber)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        genderbutton.setOnClickListener {
            showGenderBottomScreenDialog()
        }
        languagebutton.setOnClickListener {
            showLanguageBottomScreenDialog()
        }
        dayButton.setOnClickListener {
            showDayBottomScreenDialog()
        }
        languageTextChange()
        dayTextChange()
        genderTextChange()
        doctorSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
               return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                    currentSearchText = newText
                    filter(
                        genderbutton.text.toString(),
                        languagebutton.text.toString(),
                        dayButton.text.toString()
                    )
                return false
            }

        })
        backButton.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun showGenderBottomScreenDialog() {
        val bottomScreenDialog = BottomSheetDialog(this)
        bottomScreenDialog.setContentView(R.layout.selectgender_bottomdialog)
        val cancelText = bottomScreenDialog.findViewById<TextView>(R.id.genderCancelButton)
        val doneText = bottomScreenDialog.findViewById<TextView>(R.id.genderDoneButton)
        val genderChipGroup = bottomScreenDialog.findViewById<ChipGroup>(R.id.genderChipGroup)
        cancelText?.setOnClickListener {
            bottomScreenDialog.dismiss()
        }
        doneText?.setOnClickListener {
            if (genderChipGroup!= null && genderChipGroup.checkedChipId != View.NO_ID) {
                genderbutton.text = bottomScreenDialog
                        .findViewById<Chip>(genderChipGroup.checkedChipId)?.text.toString()
                bottomScreenDialog.dismiss()
            }
            bottomScreenDialog.dismiss()
        }
        bottomScreenDialog.show()
    }

    private fun showLanguageBottomScreenDialog() {
        val bottomScreenDialog = BottomSheetDialog(this)
        bottomScreenDialog.setContentView(R.layout.selectlanguage_bottomdialog)
        val cancelText = bottomScreenDialog.findViewById<TextView>(R.id.languageCancelButton)
        val doneText = bottomScreenDialog.findViewById<TextView>(R.id.languageDoneButton)
        val languageChipGroup = bottomScreenDialog.findViewById<ChipGroup>(R.id.languageChipGroup)
        cancelText?.setOnClickListener {
            bottomScreenDialog.dismiss()
        }
        doneText?.setOnClickListener {
            if (languageChipGroup!= null && languageChipGroup.checkedChipId != View.NO_ID) {
                languagebutton.text = bottomScreenDialog
                    .findViewById<Chip>(languageChipGroup.checkedChipId)?.text.toString()
                bottomScreenDialog.dismiss()
            }
            bottomScreenDialog.dismiss()
        }
        bottomScreenDialog.show()
    }

    private fun showDayBottomScreenDialog() {
        val bottomScreenDialog = BottomSheetDialog(this)
        bottomScreenDialog.setContentView(R.layout.selectday_bottomdialog)
        val cancelText = bottomScreenDialog.findViewById<TextView>(R.id.dayCancelButton)
        val doneText = bottomScreenDialog.findViewById<TextView>(R.id.dayDoneButton)
        val dayChipGroup = bottomScreenDialog.findViewById<ChipGroup>(R.id.dayChipGroup)
        cancelText?.setOnClickListener {
            bottomScreenDialog.dismiss()
        }
        doneText?.setOnClickListener {
            if (dayChipGroup!= null && dayChipGroup.checkedChipId != View.NO_ID) {
                dayButton.text = bottomScreenDialog
                    .findViewById<Chip>(dayChipGroup.checkedChipId)?.text.toString().substring(0,3)
                bottomScreenDialog.dismiss()
            }
            bottomScreenDialog.dismiss()
        }
        bottomScreenDialog.show()
    }

    private fun readData(myCallback: MyCallback) {

        db.collection("Doctors").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var counter : Int = 0
                for (doc in task.result) {
                    docArray.add(Doctor(doc.get("Name") as String,doc.id,
                        doc.get("Languages") as ArrayList<String>, doc.get("Gender") as String
                        ,doc.get("Days") as ArrayList<String>))
                    filterArray.add(Doctor(doc.get("Name") as String,doc.id,
                        doc.get("Languages") as ArrayList<String>, doc.get("Gender") as String
                        ,doc.get("Days") as ArrayList<String>))
                    adaptor.notifyItemInserted(counter)
                    counter++
                }
                myCallback.onCallback(docArray)

            } else {
                Log.d("Error getting documents",task.exception.toString())
            }
        }
    }

    private fun genderTextChange() {
        genderbutton.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(genderbutton.text.toString(),languagebutton.text.toString()
                    , dayButton.text.toString())
            }
        })
    }

    private fun languageTextChange() {
        languagebutton.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(genderbutton.text.toString(),languagebutton.text.toString()
                    , dayButton.text.toString())
            }
        })
    }

    private fun dayTextChange() {
        dayButton.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(genderbutton.text.toString(),languagebutton.text.toString()
                    , dayButton.text.toString())
            }
        })
    }

    private fun filter(gender : String, language: String, day: String) {
        filterArray = ArrayList()
        for (doc in docArray) {

            if (doc.gender == gender || gender == "All" || gender == "Gender") {
                if (doc.languages.contains(language) || language == "All" || language == "Language") {
                    if (doc.days.contains(day) || day == "All" || day == "Day") {
                        if (currentSearchText == "") {
                            filterArray.add(doc)
                        } else if (doc.name.lowercase().contains(currentSearchText.lowercase())) {
                            filterArray.add(doc)
                        }
                    }
                }
            }
        }
        adaptor = SearchDoctorAdaptor(filterArray)
        doctorRecyclerView.adapter = adaptor
        adaptor.setOnItemClickListener(object : SearchDoctorAdaptor.OnItemClickListener{
            override fun onItemClick(position: Int) {
                intent = Intent(this@SearchDoctor,DoctorProfilePageSearch::class.java)
                intent.putExtra("mcrNumber",filterArray[position].mcrNumber)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })
    }

}