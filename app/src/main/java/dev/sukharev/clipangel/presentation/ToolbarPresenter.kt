package dev.sukharev.clipangel.presentation

import com.google.android.material.appbar.MaterialToolbar


interface ToolbarPresenter {
    fun show()
    fun hide()
    fun setBackToHome(state: Boolean)
    fun setToolbar(toolbar: MaterialToolbar)
    fun getToolbar(): MaterialToolbar?
    fun setTitle(text: String?)
}