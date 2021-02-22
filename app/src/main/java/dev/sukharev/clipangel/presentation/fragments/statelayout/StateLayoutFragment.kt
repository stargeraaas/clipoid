package dev.sukharev.clipangel.presentation.fragments.statelayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.fragments.BaseFragment

abstract class StateLayoutFragment: BaseFragment() {

//    sealed class State<out T> {
//        sealed class Content<T>(val data: T): State<T>()
//        sealed class EmptyContent(): State<Nothing>
//        sealed class Loading(val state: Boolean): State<Nothing>()
//    }

//    interface ErrorDialogProvider {
//        fun get
//
//    }

    private val loadingFragment = LoadingFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_state_layout, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().replace(R.id.fragment_container, loadingFragment).commit()
    }

    abstract fun getDataFragment(): Fragment

}