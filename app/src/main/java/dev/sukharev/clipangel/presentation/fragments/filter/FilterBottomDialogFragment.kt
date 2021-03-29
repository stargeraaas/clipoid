package dev.sukharev.clipangel.presentation.fragments.filter

import android.view.View
import androidx.appcompat.widget.SearchView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.fragments.bottom.BaseBottomDialog

class FilterBottomDialogFragment: BaseBottomDialog() {

    override fun getLayoutId(): Int = R.layout.fragment_bottom_filter

    override fun initViews(view: View) {
        view.findViewById<SearchView>(R.id.search_row)?.apply {
            requestFocus()
        }
    }

}