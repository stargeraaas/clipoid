package dev.sukharev.clipangel.presentation



interface ToolbarPresenter {
    fun show()
    fun hide()
    fun setBackToHome(state: Boolean)
    fun setTitle(text: String?)
}