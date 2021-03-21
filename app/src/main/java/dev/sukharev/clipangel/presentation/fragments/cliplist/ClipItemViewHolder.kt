package dev.sukharev.clipangel.presentation.fragments.cliplist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.viewmodels.channellist.MainViewModel
import dev.sukharev.clipangel.utils.copyInClipboardWithToast


class ClipItemViewHolder(rootView: View, val clipClick: (clipId: String) -> Unit):
        RecyclerView.ViewHolder(rootView){

    private val descriptionTextView: TextView? = rootView.findViewById(R.id.description_clip)
    private val copyButton: ImageView? = rootView.findViewById(R.id.copy_button)
    private val favoriteIcon: ImageView? = rootView.findViewById(R.id.favorite_icon)
    private val dateTextView: TextView? = rootView.findViewById(R.id.date_clip)
    private val shortDetailContainer: ConstraintLayout? = rootView.findViewById(R.id.short_detail_container)
    private val padlockIcon: ImageView? = rootView.findViewById(R.id.icon_padlock)
    private val privateCaptionTextView: TextView? = rootView.findViewById(R.id.private_caption)
    private val fromCaptionTextView: TextView? = rootView.findViewById(R.id.from_caption)

    var onItemClickListener: OnClipItemClickListener? = null

    fun bind(model: Model) {
        shortDetailContainer?.setOnClickListener {
            onItemClickListener?.onItemClicked(model.id)
        }

        favoriteIcon?.visibility = if (model.isFavorite) View.VISIBLE else View.GONE
        dateTextView?.text = model.date

        copyButton?.setOnClickListener {
            clipClick.invoke(model.id)
        }

        if (model.isProtected) {
            padlockIcon?.visibility = View.VISIBLE
            privateCaptionTextView?.visibility = View.VISIBLE
            descriptionTextView?.visibility = View.INVISIBLE
        } else {
            padlockIcon?.visibility = View.INVISIBLE
            privateCaptionTextView?.visibility = View.INVISIBLE
            descriptionTextView?.visibility = View.VISIBLE
            descriptionTextView?.text = model.description
        }

        fromCaptionTextView?.text = fromCaptionTextView!!.context.getString(R.string.from)
                .plus(": ")
                .plus(model.channelName)
    }

    data class Model(
            val id: String,
            val description: String,
            var isFavorite: Boolean,
            var isProtected: Boolean,
            val date: String,
            val channelName: String
    )

}