package orbital.project.helper_classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import orbital.project.R

class CancellationAdaptor(private val appointments: ArrayList<AppointmentDetails>)
    : RecyclerView.Adapter<CancellationAdaptor.ViewHolder>(){

    private lateinit var appointmentListener : OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        appointmentListener = listener
    }

    class ViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val name : TextView
        val clinic : TextView
        val date : TextView
        val time : TextView

        init {
            name = view.findViewById(R.id.doctorName)
            clinic = view.findViewById(R.id.clinicName)
            date = view.findViewById(R.id.appointmentDate)
            time = view.findViewById(R.id.appointmentTime)
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.appointment_cardview, viewGroup, false)
        return ViewHolder(view, appointmentListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.name.text = appointment.name
        holder.clinic.text = appointment.clinic
        holder.time.text = appointment.time
        holder.date.text = appointment.date
    }

    override fun getItemCount(): Int {
        return appointments.size
    }


}