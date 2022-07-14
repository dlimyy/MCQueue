package orbital.project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.InputStream


class SearchDoctorAdaptor(private val docArray : ArrayList<Doctor>)
    : RecyclerView.Adapter<SearchDoctorAdaptor.ViewHolder>() {

    private lateinit var doctorListener : OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        doctorListener = listener
    }


    class ViewHolder(view : View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val name :TextView
        val profilePic : ImageView

        init {
            itemView.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
            name = view.findViewById(R.id.doctorInfoName)
            profilePic = view.findViewById(R.id.doctorInfoProfilePicture)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.doctorinfo_cardview, viewGroup, false)
        return ViewHolder(view, doctorListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doctor = docArray[position]
        holder.name.text = doctor.name
        GlideApp.with(holder.itemView).load(Firebase.storage.reference.child("Images/"
                + doctor.mcrNumber)).placeholder(R.drawable.doctor__1_).into(holder.profilePic)
    }

    override fun getItemCount(): Int {
        return docArray.size
    }
}
