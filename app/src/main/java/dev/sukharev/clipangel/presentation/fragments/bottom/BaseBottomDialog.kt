package dev.sukharev.clipangel.presentation.fragments.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.fragments.dialogs.DetailChannelBottomDialog

abstract class BaseBottomDialog: BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        view?.let {
            initViews(it)
        }
        return view
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initViews(view: View)

}