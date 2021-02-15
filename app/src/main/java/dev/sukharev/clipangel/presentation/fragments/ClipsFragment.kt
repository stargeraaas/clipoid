package dev.sukharev.clipangel.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.ToolbarPresenter


class ClipsFragment: BaseFragment() {

    override fun initToolbar(presenter: ToolbarPresenter) {
        presenter.show()
        presenter.setTitle("Мои клипы")
    }

    override fun showBottomNavigation(): Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_clips, null)
    }
}