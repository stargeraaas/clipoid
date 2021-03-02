package dev.sukharev.clipangel.presentation.fragments.cliplist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.R

class ClipListAdapter: RecyclerView.Adapter<ClipItemViewHolder>() {

    private val items = mutableListOf<ClipItemViewHolder.Model>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClipItemViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.item_clip, parent, false)
        return ClipItemViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: ClipItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<ClipItemViewHolder.Model>) {
        items.apply {
            clear()
            addAll(newItems)
            notifyDataSetChanged()
        }
    }
}