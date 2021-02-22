package dev.sukharev.clipangel.presentation.fragments

sealed class ViewFragmentState {

    class Content<T> (val value: T) : ViewFragmentState()

    class Loading() : ViewFragmentState()

    class Failure(val title: String?, val subtitle: String? = null,
                            val submitText: String? = null, val iconRes: Int? = null) : ViewFragmentState()

}
