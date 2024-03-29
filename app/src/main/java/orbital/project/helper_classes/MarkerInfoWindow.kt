package orbital.project.helper_classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import orbital.project.R

class MarkerInfoWindowAdapter(
    private val context: Context
) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View? {

        val clinic = marker.tag as? MarkerInfo ?: return null

        val view = LayoutInflater.from(context).inflate(
            R.layout.marker_info, null
        )
        view.findViewById<TextView>(
            R.id.markerInfoClinicName
        ).text = clinic.name
        view.findViewById<TextView>(
            R.id.markerInfoClinicAddress
        ).text = clinic.address
        view.findViewById<TextView>(
            R.id.markerInfoQueue
        ).text = "Queue: %d".format(clinic.queue)

        return view
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }
}