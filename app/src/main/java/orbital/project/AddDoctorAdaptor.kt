package orbital.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class AddDoctorAdaptor(private val timing: ArrayList<Timing>)
    : RecyclerView.Adapter<AddDoctorAdaptor.ViewHolder>() {

    private lateinit var timeIntervalListener: OnButtonClickListener

    interface OnButtonClickListener {
        fun updateTime(button: Button)
    }

    fun setOnButtonClickListener(listener: AddDoctorAdaptor.OnButtonClickListener) {
        timeIntervalListener = listener
    }

    class ViewHolder(view: View, listener : OnButtonClickListener) : RecyclerView.ViewHolder(view) {
        val startTime: Button
        val endTime : Button

        init {
            startTime = view.findViewById(R.id.startTimeButton)
            endTime = view.findViewById(R.id.endTimeButton)

            startTime.setOnClickListener {
                listener.updateTime(startTime)
            }
            endTime.setOnClickListener {
                listener.updateTime(endTime)
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.time_interval_doctorinfo, viewGroup, false)
        return ViewHolder(view, timeIntervalListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.startTime.text = timing[position].startTime
        holder.endTime.text = timing[position].endTime
    }

    override fun getItemCount(): Int {
        return timing.size
    }

    fun timeFormatString(holder: ViewHolder) : String{
        return holder.startTime.text.toString() + ":" + holder.endTime.text.toString()
    }
}