package dev.sukharev.clipangel.presentation.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.R

class ChannelRecyclerAdapter : RecyclerView.Adapter<ChannelItemViewHolder>() {

    private val items = mutableListOf<ChannelItemVM>()

    private val clickListeners = mutableListOf<OnItemClickListener>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_channel_item, parent, false)
        return ChannelItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChannelItemViewHolder, position: Int) {
        holder.bind(items[position], clickListeners)
    }

    override fun getItemCount(): Int = items.size

    fun addItems(newItems: List<ChannelItemVM>) {
        items.apply {
            clear()
            addAll(newItems)
            notifyDataSetChanged()
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(channelItemVM: ChannelItemVM)
    }

    fun addOnItemClickListener(listener: OnItemClickListener) {
        clickListeners.add(listener)
    }

    fun removeOnItemClickListener(listener: OnItemClickListener) {
        clickListeners.remove(listener)
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

    }

    fun bind(model: ChannelItemVM, onClickListeners: List<ChannelRecyclerAdapter.OnItemClickListener>) {
        itemView.setOnClickListener {
            onClickListeners.forEach { it.onItemClicked(model) }
        }
        id = model.id
        nameView?.text = model.name
        createDateView?.text = itemView.context.getString(R.string.created_date)
                .plus(": ").plus(model.createDate)
    }
}