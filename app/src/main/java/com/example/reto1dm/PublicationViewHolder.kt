package com.example.reto1dm

import android.media.Image
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PublicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    //UI Controllers
    var eventNameTV: TextView = itemView.findViewById(R.id.eventNameTV)
    var profileNameTV: TextView = itemView.findViewById(R.id.profileNameTV)
    var ubicationTV: TextView = itemView.findViewById(R.id.ubicationTV)
    var startTimeTV: TextView = itemView.findViewById(R.id.startTimeTV)
    var finishTimeTV: TextView = itemView.findViewById(R.id.finishTimeTV)
    var eventImageV: ImageView = itemView.findViewById(R.id.eventImageV)
    var seeBtn: ImageView = itemView.findViewById(R.id.seeBtn)

    init{

    }
}