package dev.sukharev.clipangel.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dev.sukharev.clipangel.presentation.BottomNavView
import dev.sukharev.clipangel.presentation.ToolbarPresenter


abstract class BaseFragment: Fragment() {

    protected fun getToolbarPresenter() = activity as ToolbarPresenter
    protected fun getBottomNavPresenter() = activity as BottomNavView

    protected abstract fun initToolbar(presenter: ToolbarPresenter)

    protected abstract fun showBottomNavigation(): Boolean

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(getToolbarPresenter())
        getBottomNavPresenter().setVisibility(showBottomNavigation())
    }

}