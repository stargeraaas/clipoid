package dev.sukharev.clipangel.presentation.fragments.cliplist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.models.Category

class ClipListAdapter(val copyClip: (clipId: String) -> Unit):
        RecyclerView.Adapter<ClipItemViewHolder>(), CategoryProvider {

    private lateinit var category: Category

    private val items = mutableListOf<ClipItemViewHolder.Model>()

    var onItemCLickListener: OnClipItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClipItemViewHolder {
        val layoutView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_clip, parent, false)
        return ClipItemViewHolder(this, layoutView, copyClip).apply {
            this.onItemClickListener = onItemCLickListener
        }
    }

    override fun onBindViewHolder(holder: ClipItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(category: Category, newItems: List<ClipItemViewHolder.Model>) {
        this.category = category
        items.apply {
            clear()
            addAll(newItems)
            notifyDataSetChanged()
        }
    }

    override fun currentCategory(): Category = category
}