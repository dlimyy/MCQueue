package orbital.project

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class DataAnalytics : AppCompatActivity() {
    private lateinit var agePieChart : PieChart
    private lateinit var timePieChart : PieChart
    private lateinit var dayPieChart : PieChart
    private lateinit var predictedLoad : TextView
    private lateinit var noAgeAvailable : TextView
    private lateinit var noDayAvailable : TextView
    private lateinit var noTimeAvailable : TextView
    private lateinit var uid : String
    private val db = FirebaseFirestore.getInstance()
    private val ageArray: ArrayList<PieEntry> = ArrayList()
    private val timeArray : ArrayList<PieEntry> = ArrayList()
    private val dayArray : ArrayList<PieEntry> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_analytics)
        agePieChart = findViewById(R.id.agePieChart)
        timePieChart = findViewById(R.id.timingPieChart)
        dayPieChart = findViewById(R.id.dayPieChart)
        predictedLoad = findViewById(R.id.predictedLoad)
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        noAgeAvailable = findViewById(R.id.noAgeAvailable)
        noDayAvailable = findViewById(R.id.noDayAvailable)
        noTimeAvailable = findViewById(R.id.noTimeAvailable)
        setupPieCharts();
        loadPieChartsData();
    }

    private fun setupPieCharts() {
        agePieChart.isDrawHoleEnabled = true
        agePieChart.setUsePercentValues(true)
//        agePieChart.setEntryLabelTextSize(resources.getDimension(com.intuit.ssp.R.dimen._8ssp) /
//                resources.displayMetrics.scaledDensity)
//        agePieChart.setEntryLabelColor(Color.BLACK)
        agePieChart.setDrawEntryLabels(false)
        agePieChart.centerText = "Age Category"
        agePieChart.setCenterTextSize(resources.getDimension(com.intuit.ssp.R.dimen._16ssp) /
                resources.displayMetrics.scaledDensity)
        agePieChart.description.isEnabled = false

        dayPieChart.isDrawHoleEnabled = true
        dayPieChart.setUsePercentValues(true)
        dayPieChart.setDrawEntryLabels(false)
        dayPieChart.centerText = "Days visited by patient"
        dayPieChart.setCenterTextSize(resources.getDimension(com.intuit.ssp.R.dimen._16ssp) /
                resources.displayMetrics.scaledDensity)
        dayPieChart.description.isEnabled = false

        timePieChart.isDrawHoleEnabled = true
        timePieChart.setUsePercentValues(true)
        timePieChart.setDrawEntryLabels(false)

        timePieChart.centerText = "Time visited by patient"
        timePieChart.setCenterTextSize(resources.getDimension(com.intuit.ssp.R.dimen._16ssp) /
                resources.displayMetrics.scaledDensity)
        timePieChart.description.isEnabled = false

    }

    private fun loadPieChartsData() {
        val ageLegend = agePieChart.legend
        val dayLegend = dayPieChart.legend
        val timeLegend = timePieChart.legend


        ageLegend.textSize = resources.getDimension(com.intuit.ssp.R.dimen._12ssp) /
                resources.displayMetrics.scaledDensity
        dayLegend.textSize = resources.getDimension(com.intuit.ssp.R.dimen._12ssp) /
                resources.displayMetrics.scaledDensity
        timeLegend.textSize = resources.getDimension(com.intuit.ssp.R.dimen._12ssp) /
                resources.displayMetrics.scaledDensity

        val colors: ArrayList<Int> = ArrayList()
        for (color in ColorTemplate.MATERIAL_COLORS) {
            colors.add(color)
        }
        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }
        db.collection("Users").document(uid).get().addOnSuccessListener { doc ->
            val ageList =doc.get("Age") as? HashMap<String, Float>
            val dayList = doc.get("Day") as? HashMap<String, Float>
            val timeList = doc.get("Time") as? HashMap<String, Float>
            val predictedPatientCount = doc.get("PredictedLoad") as? Long

            if (ageList != null && dayList != null && timeList != null) {
                ageLegend.isWordWrapEnabled = true
                ageLegend.isEnabled = true
                noAgeAvailable.visibility = View.GONE
                for ((key,value ) in ageList) {
                    ageArray.add(PieEntry(value,key))
                }
                val ageSet = PieDataSet(ageArray, "")
                ageSet.colors = colors
                val ageData = PieData(ageSet)
                ageData.setDrawValues(true)
                ageData.setValueFormatter(PercentFormatter(agePieChart))
                ageData.setValueTextSize(resources.getDimension(com.intuit.ssp.R.dimen._10ssp) /
                        resources.displayMetrics.scaledDensity)
                ageData.setValueTextColor(Color.BLACK)
                agePieChart.data = ageData
                agePieChart.invalidate()
                agePieChart.animateY(1400, Easing.EaseInOutQuad)

                dayLegend.isWordWrapEnabled = true
                dayLegend.isEnabled = true
                noDayAvailable.visibility = View.GONE
                for ((key,value) in dayList) {
                    dayArray.add(PieEntry(value,key))
                }
                val daySet = PieDataSet(dayArray, "")
                daySet.colors = colors
                val dayData = PieData(daySet)
                dayData.setDrawValues(true)
                dayData.setValueFormatter(PercentFormatter(dayPieChart))
                dayData.setValueTextSize(resources.getDimension(com.intuit.ssp.R.dimen._10ssp) /
                        resources.displayMetrics.scaledDensity)
                dayData.setValueTextColor(Color.BLACK)
                dayPieChart.data = dayData
                dayPieChart.invalidate()
                dayPieChart.animateY(1400, Easing.EaseInOutQuad)

                timeLegend.isWordWrapEnabled = true
                timeLegend.isEnabled = true
                noTimeAvailable.visibility = View.GONE
                for ((key,value) in timeList) {
                    timeArray.add(PieEntry(value,key))
                }
                val timeSet = PieDataSet(timeArray, "")
                timeSet.colors = colors
                val timeData = PieData(timeSet)
                timeData.setDrawValues(true)
                timeData.setValueFormatter(PercentFormatter(timePieChart))
                timeData.setValueTextSize(resources.getDimension(com.intuit.ssp.R.dimen._10ssp) /
                        resources.displayMetrics.scaledDensity)
                timeData.setValueTextColor(Color.BLACK)
                timePieChart.data = timeData
                timePieChart.invalidate()
                timePieChart.animateY(1400, Easing.EaseInOutQuad)

                predictedLoad.text = "Predicted Patient Load:" + " " +
                        predictedPatientCount.toString()

            } else {
                ageLegend.isEnabled = false
                ageArray.add(PieEntry(1f))
                val ageSet = PieDataSet(ageArray, "")
                ageSet.colors = colors
                val agedata = PieData(ageSet)
                agedata.setDrawValues(false)
                agedata.setValueFormatter(PercentFormatter(agePieChart))
                agedata.setValueTextSize(resources.getDimension(com.intuit.ssp.R.dimen._16ssp) /
                        resources.displayMetrics.scaledDensity)
                agedata.setValueTextColor(Color.BLACK)
                agePieChart.data = agedata
                agePieChart.invalidate()
                agePieChart.animateY(1400, Easing.EaseInOutQuad)

                dayLegend.isEnabled = false
                dayArray.add(PieEntry(1f))
                val daySet = PieDataSet(dayArray, "")
                daySet.colors = colors
                val dayData = PieData(daySet)
                dayData.setDrawValues(false)
                dayData.setValueFormatter(PercentFormatter(dayPieChart))
                dayData.setValueTextSize(resources.getDimension(com.intuit.ssp.R.dimen._16ssp) /
                        resources.displayMetrics.scaledDensity)
                dayData.setValueTextColor(Color.BLACK)
                dayPieChart.data = dayData
                dayPieChart.invalidate()
                dayPieChart.animateY(1400, Easing.EaseInOutQuad)

                timeLegend.isEnabled = false
                timeArray.add(PieEntry(1f))
                val timeSet = PieDataSet(timeArray, "")
                timeSet.colors = colors
                val timeData = PieData(timeSet)
                timeData.setDrawValues(false)
                timeData.setValueFormatter(PercentFormatter(timePieChart))
                timeData.setValueTextSize(resources.getDimension(com.intuit.ssp.R.dimen._16ssp) /
                        resources.displayMetrics.scaledDensity)
                timeData.setValueTextColor(Color.BLACK)
                timePieChart.data = timeData
                timePieChart.invalidate()
                timePieChart.animateY(1400, Easing.EaseInOutQuad)
            }
        }
    }
}