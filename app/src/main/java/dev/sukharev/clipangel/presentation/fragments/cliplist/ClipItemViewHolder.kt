package dev.sukharev.clipangel.presentation.fragments.cliplist

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.R

class ClipItemViewHolder(rootView: View): RecyclerView.ViewHolder(rootView) {

    private val descriptionTextView: TextView? = rootView.findViewById(R.id.description_clip)

    fun bind(model: Model) {
        descriptionTextView?.text = model.description
    }

    data class Model(
            val id: String,
            val description: String
    )

}