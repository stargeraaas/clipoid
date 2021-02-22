package dev.sukharev.clipangel.presentation.fragments

sealed class ChannelFragmentState {

    class Content<T> (val value: T) : ChannelFragmentState()

    class Loading() : ChannelFragmentState()

    class Failure(val title: String?, val subtitle: String? = null,
                            val submitText: String? = null, val iconRes: Int? = null) : ChannelFragmentState()

}
