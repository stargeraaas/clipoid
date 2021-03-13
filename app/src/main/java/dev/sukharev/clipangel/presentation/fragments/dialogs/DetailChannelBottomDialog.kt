package dev.sukharev.clipangel.presentation.fragments.dialogs

import android.view.View
import android.widget.Button
import android.widget.TextView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.domain.channel.models.Channel

import dev.sukharev.clipangel.presentation.fragments.bottom.BaseBottomDialog
import dev.sukharev.clipangel.utils.toDateFormat1
import java.util.*

class DetailChannelBottomDialog(val channel: Channel): BaseBottomDialog<Channel>(channel) {

    interface OnClickListener {
        fun onClick(id: String)
    }

    override fun getLayoutId(): Int = R.layout.layout_detail_channel_bottom_sheet

    override fun initViews(view: View) {
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
                onClickListenerCallback?.onClick(channel.id)
                this@DetailChannelBottomDialog.dismiss()
            }
        }
    }

}