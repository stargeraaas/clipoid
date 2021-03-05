package dev.sukharev.clipangel.presentation.fragments.cliplist

import android.graphics.Color.green
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.utils.toDateFormat1
import java.util.*

class DetailClipDialogFragment: BottomSheetDialogFragment() {

    private var channelNameTextView: TextView? = null
    private var createDateTextView: TextView? = null
    private var clipDataTextView: TextView? = null
    private var favoriteButton: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_clip_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        channelNameTextView = view.findViewById(R.id.channel_name_text_view)
        createDateTextView = view.findViewById(R.id.create_date_text_view)
        clipDataTextView = view.findViewById(R.id.clip_data_text_view)
        favoriteButton = view.findViewById(R.id.favorite_button)

        channelNameTextView?.text = "Cortex"
        createDateTextView?.text = Date().time.toDateFormat1()
        clipDataTextView?.text = "HELLO"
        favoriteButton?.let {
            val drwbl = it.drawable
            DrawableCompat.setTint(drwbl,
                    this@DetailClipDialogFragment.requireContext().getColor(R.color.black))
            it.setImageDrawable(drwbl)
        }
    }


}