package dev.sukharev.clipangel.presentation.fragments.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import dev.sukharev.clipangel.R

class CustomAlertDialog: DialogFragment() {

    var titleTextView: TextView? = null
    var bodyTextView: TextView? = null
    var negativeButton: Button? = null
    var positiveButton: Button? = null

    private lateinit var block: () -> Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            titleTextView = findViewById(R.id.title)
            bodyTextView = findViewById(R.id.body)
            negativeButton = findViewById(R.id.negative_button)
            positiveButton = findViewById(R.id.positive_button)
        }

        block()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_custom_dialog, container, false)
    }

    fun show(fragmentManager: FragmentManager, tag: String?, block: ()-> Unit) {
        this.block = block
        show(fragmentManager, tag)
    }

}