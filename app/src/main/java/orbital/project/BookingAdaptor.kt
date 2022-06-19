package orbital.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent

class BookingAdaptor(private val appointments: ArrayList<String>)
    : RecyclerView.Adapter<BookingAdaptor.ViewHolder>(){

    private lateinit var appointmentListener : OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        appointmentListener = listener
    }

    class ViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val timing : TextView

        init {
            // Define click listener for the ViewHolder's View.
            timing = view.findViewById(R.id.appointmentTiming)
            itemView.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.appointment_timing, viewGroup, false)
        return ViewHolder(view, appointmentListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.timing.text = appointment
    }

    override fun getItemCount(): Int {
        return appointments.size
    }


}