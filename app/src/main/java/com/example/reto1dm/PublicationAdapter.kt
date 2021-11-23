package com.example.reto1dm

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reto1dm.model.Business
import com.example.reto1dm.model.Publication
import com.google.android.gms.tasks.Task
import com.google.gson.Gson

//Esta clase se encarga de organizar los elementos de la lista de publicaciones
class PublicationAdapter : RecyclerView.Adapter<PublicationViewHolder>(){

    private val publications = ArrayList<Publication>();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.publicationrow, parent, false)
        val publicationViewHolder = PublicationViewHolder(view)
        return publicationViewHolder
    }

    override fun onBindViewHolder(viewHolder: PublicationViewHolder, position: Int) {
        val publication = publications[position]
        viewHolder.eventNameTV.text = publication.eventName
        viewHolder.profileNameTV.text = publication.profileName
        viewHolder.ubicationTV.text = publication.ubication
        viewHolder.startTimeTV.text = publication.startTime
        viewHolder.finishTimeTV.text = publication.finishTime
        viewHolder.eventImageV.setImageBitmap(BitmapFactory.decodeFile(publication.profilePhotoPath))
    }

    override fun getItemCount(): Int {
        return publications.size
    }

    fun addPublication(publication: Publication){
        publications.add(publication)
        notifyItemInserted(publications.size - 1)
    }





}