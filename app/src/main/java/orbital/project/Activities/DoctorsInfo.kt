package orbital.project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import orbital.project.helper_classes.Doctor
import orbital.project.R
import orbital.project.helper_classes.SearchDoctorAdaptor

class DoctorsInfo : AppCompatActivity() {
    private lateinit var addDoctor : CardView
    private lateinit var doctorRecyclerView : RecyclerView
    private lateinit var noDoctorText : TextView
    private lateinit var backButton : ImageView
    private val docArray : ArrayList<Doctor> = ArrayList()
    private lateinit var adaptor : SearchDoctorAdaptor
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctors_info)
        addDoctor = findViewById(R.id.addDoctorCardView)
        doctorRecyclerView = findViewById(R.id.doctorInfoRecyclerView)
        backButton = findViewById(R.id.navigateDoctorInfoToHome)
        noDoctorText = findViewById(R.id.noDoctorsIn)
        auth = FirebaseAuth.getInstance()
        readData(object : SearchDoctor.MyCallback {
            override fun onCallback(docArray: ArrayList<Doctor>) {
            }
        })
        doctorRecyclerView.layoutManager = LinearLayoutManager(this)
        adaptor = SearchDoctorAdaptor(docArray)
        doctorRecyclerView.adapter = adaptor
        adaptor.setOnItemClickListener(object : SearchDoctorAdaptor.OnItemClickListener {
            override fun onItemClick(position: Int) {
                intent = Intent(this@DoctorsInfo, DoctorProfilePageSearch::class.java)
                intent.putExtra("mcrNumber", docArray[position].mcrNumber)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })
        backButton.setOnClickListener {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        addDoctor.setOnClickListener {
            intent = Intent(this, AddDoctor::class.java)
            startActivity(intent)
        }
    }

    private fun readData(myCallback: SearchDoctor.MyCallback) {

        db.collection("Doctors")
            .whereEqualTo("Clinic uid", auth.currentUser!!.uid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for ((counter, doc) in task.result.withIndex()) {
                    docArray.add(
                        Doctor(doc.get("Name") as String,doc.id,
                        doc.get("Languages") as ArrayList<String>, doc.get("Gender") as String
                        ,doc.get("Days") as ArrayList<String>)
                    )
                    adaptor.notifyItemInserted(counter)
                    if (adaptor.itemCount > 0) {
                        noDoctorText.visibility = View.GONE
                    }
                    }
                myCallback.onCallback(docArray)
            } else {
                Log.d("Error getting documents",task.exception.toString())
            }
        }
    }

}