package com.example.rickandmortycharacterlibrary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortycharacterlibrary.models.Characters

class RecyclerAdapter(private val context: Context, private val characters: ArrayList<Characters>) :
    RecyclerView.Adapter<RecyclerAdapter.RecyclerAdapterViewHolder>() {

    private var characterFilter = characters

    class RecyclerAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: AppCompatImageView = view.findViewById(R.id.image)
        val name: AppCompatTextView = view.findViewById(R.id.name)
        val status: AppCompatTextView = view.findViewById(R.id.alive_species)
        val location: AppCompatTextView = view.findViewById(R.id.location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return RecyclerAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapterViewHolder, position: Int) {
        val character: Characters = characterFilter[position]

        Glide.with(context).load(character.image).into(holder.image)

        holder.name.text = character.name

        holder.status.text = "${character.alive} - ${character.species}"

        holder.location.text = character.location
    }

    override fun getItemCount(): Int {
        return characterFilter.size
    }

    fun search(query: String) {
        if (query.isNotBlank() && query.isNotEmpty()) {
            characterFilter.filter { it.name.contains(query, ignoreCase = true) }
            notifyDataSetChanged()
        } else {
            characterFilter = characters
            notifyDataSetChanged()
        }
    }
}