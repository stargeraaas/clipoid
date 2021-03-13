package dev.sukharev.clipangel.presentation.fragments.bottom

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.R

class ListBottomDialogFragment(val data: List<ListItem>):
        BaseBottomDialog<List<ListBottomDialogFragment.ListItem>>(data) {

    private val adapter = SingleListAdapter(data)

    override fun getLayoutId(): Int = R.layout.fragment_bottom_list_dialog

    override fun initViews(view: View) {
        view.findViewById<RecyclerView>(R.id.list_item_recycler)?.apply {
            adapter = this@ListBottomDialogFragment.adapter
            layoutManager = LinearLayoutManager(view.context)
        }
    }

    data class ListItem(
            val id: Int,
            val text: String,
            val icon: Int? = null,
    )

}