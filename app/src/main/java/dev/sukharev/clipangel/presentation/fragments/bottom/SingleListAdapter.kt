package dev.sukharev.clipangel.presentation.fragments.bottom

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.view.list.SingleLineItemViewHolder

class SingleListAdapter(private val items: List<ListBottomDialogFragment.ListItem>):
        RecyclerView.Adapter<SingleLineItemViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleLineItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.material_list_item_single_line, parent, false)

        return SingleLineItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SingleLineItemViewHolder, position: Int) {
        items[position].apply {
            icon?.let { iconRes ->
                holder.icon?.setImageResource(iconRes)
            } ?: run {
                holder.icon?.setImageDrawable(null)
            }

            holder.text.text = text

            holder.itemView.setOnClickListener {
                onItemClickListener?.onClick(this)
            }

            if (isSelected) {
                holder.itemView.background = holder.getContext().getDrawable(R.color.pantone_light_green)
                DrawableCompat.setTint(holder.icon.drawable, holder.getContext().getColor(R.color.pantone_orange))
                holder.text.setTextColor(holder.getContext().getColor(R.color.pantone_orange))
            } else {
                val typedValue = TypedValue()
                holder.itemView.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
                holder.itemView.background = ContextCompat.getDrawable(holder.getContext(), typedValue.resourceId)
                DrawableCompat.setTint(holder.icon.drawable, holder.getContext().getColor(R.color.pantone_primary))
                holder.text.setTextColor(holder.getContext().getColor(R.color.pantone_primary))
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(item: ListBottomDialogFragment.ListItem)
    }

    override fun getItemCount(): Int = items.count()

    fun SingleLineItemViewHolder.getContext() = itemView.context
}