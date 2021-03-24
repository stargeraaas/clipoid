package dev.sukharev.clipangel.presentation.fragments.bottom

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.sukharev.clipangel.R


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val dialog = dialog as BottomSheetDialog?
                val bottomSheet = dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
            }
        })
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initViews(view: View)

}