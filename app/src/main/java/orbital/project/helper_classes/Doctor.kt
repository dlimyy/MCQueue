package orbital.project.helper_classes

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView

data class Doctor(val name : String, val mcrNumber : String,
                  val languages : ArrayList<String>, val gender : String
                  , val days : ArrayList<String>)
