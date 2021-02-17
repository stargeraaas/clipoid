package dev.sukharev.clipangel.presentation.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.R

class ChannelRecyclerAdapter : RecyclerView.Adapter<ChannelItemViewHolder>() {

    private val items = mutableListOf<ChannelItemVM>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_channel_item, null)
        return ChannelItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChannelItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addItems(newItems: List<ChannelItemVM>) {
        items.apply {
            clear()
            addAll(newItems)
            notifyDataSetChanged()
        }
    }

}

data class ChannelItemVM(
        val id: String,
        val name: String,
        val createDate: String,
        val isDeleted: Boolean
)

class ChannelItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var nameView: TextView? = itemView.findViewById(R.id.channel_name)
    private var createDateView: TextView? = itemView.findViewById(R.id.channel_create_date)

    private var id: String? = null

    init {
        itemView.setOnClickListener {
            println("CLICKED ON ITEM WITH ID $id")
        }
    }

    fun bind(model: ChannelItemVM) {
        id = model.id
        nameView?.text = model.name
        createDateView?.text = model.createDate
    }
}