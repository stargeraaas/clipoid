package dev.sukharev.clipangel.presentation.fragments.cliplist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.utils.copyInClipboardWithToast
import org.w3c.dom.Text


class ClipItemViewHolder(rootView: View): RecyclerView.ViewHolder(rootView) {

    private val descriptionTextView: TextView? = rootView.findViewById(R.id.description_clip)
    private val copyButton: ImageView? = rootView.findViewById(R.id.copy_button)
    private val favoriteIcon: ImageView? = rootView.findViewById(R.id.favorite_icon)
    private val dateTextView: TextView? = rootView.findViewById(R.id.date_clip)

    fun bind(model: Model) {
        descriptionTextView?.text = model.description

        favoriteIcon?.visibility = if (model.isFavorite) View.VISIBLE else View.GONE
        dateTextView?.text = model.date

        copyButton?.setOnClickListener {
            model.description.copyInClipboardWithToast("Text copied")
        }
    }

    data class Model(
            val id: String,
            val description: String,
            var isFavorite: Boolean,
            val date: String
    )

}