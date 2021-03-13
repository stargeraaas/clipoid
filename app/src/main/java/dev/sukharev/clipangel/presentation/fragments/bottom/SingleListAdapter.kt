package dev.sukharev.clipangel.presentation.fragments.bottom

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.view.list.SingleLineItemViewHolder
import dev.sukharev.clipangel.utils.addCircleRipple

class SingleListAdapter(private val items: List<ListBottomDialogFragment.ListItem>):
        RecyclerView.Adapter<SingleLineItemViewHolder>() {

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
                println("CLICKED ID $id")
            }

            if (isSelected) {
                holder.itemView.background = holder.getContext().getDrawable(R.color.pantone_light_green)
            } else {
//                holder.itemView.addCircleRipple()
            }
        }
    }



    override fun getItemCount(): Int = items.count()

    fun SingleLineItemViewHolder.getContext() = itemView.context
}