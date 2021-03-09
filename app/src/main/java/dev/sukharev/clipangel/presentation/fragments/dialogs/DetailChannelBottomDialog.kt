package dev.sukharev.clipangel.presentation.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.utils.toDateFormat1
import java.util.*

class DetailChannelBottomDialog(val channel: Channel): BottomSheetDialogFragment() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_detail_channel_bottom_sheet, container, false)
        view.findViewById<TextView>(R.id.channel_name)?.apply {
            text = channel.name
        }

        view.findViewById<TextView>(R.id.created_date_channel_text_view)?.apply {
            text = context.getString(R.string.created_date).plus(": ")
                    .plus(channel.createTime.toDateFormat1())
        }

        view.findViewById<TextView>(R.id.clip_count_text_view)?.apply {
            text = context.getString(R.string.clip_count).plus(": ").plus(channel.clipCount)
        }

        view.findViewById<TextView>(R.id.identifier_channel_text_view)?.apply {
            text = context.getString(R.string.identifier).plus(": ").plus(channel.id)
        }

        view.findViewById<Button>(R.id.delete_channel_button)?.apply {
            setOnClickListener {
                onClickListener?.onClick(channel.id)
                this@DetailChannelBottomDialog.dismiss()
            }
        }

        return view
    }

    interface OnClickListener {
        fun onClick(id: String)
    }

    fun setOnClickListener(listener: OnClickListener) {
        onClickListener = listener
    }

}