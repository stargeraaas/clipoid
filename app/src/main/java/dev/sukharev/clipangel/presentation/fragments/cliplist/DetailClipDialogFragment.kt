package dev.sukharev.clipangel.presentation.fragments.cliplist

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.fragments.bottom.BaseBottomDialog
import dev.sukharev.clipangel.utils.copyInClipboardWithToast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import java.util.*

class DetailClipDialogFragment(private val model: ClipListViewModel.DetailedClipModel) : BaseBottomDialog() {

    private var channelNameTextView: TextView? = null
    private var createDateTextView: TextView? = null
    private var clipDataTextView: TextView? = null
    private var favoriteButton: ImageView? = null
    private var copyButton: Button? = null
    private var deleteButton: Button? = null
    private var protectClipImageView: ImageView? = null

    private val viewModel: ClipListViewModel by inject()

    private fun setDetailedClipInfo(clip: ClipListViewModel.DetailedClipModel) {
        channelNameTextView?.text = clip.channelName
        createDateTextView?.text = clip.createDate
        clipDataTextView?.text = clip.data
        favoriteButton?.let { view ->
            val drwbl = view.drawable
            DrawableCompat.setTint(drwbl,
                    this@DetailClipDialogFragment.requireContext().getColor(
                            if (clip.isFavorite) R.color.favorite_active else R.color.favorite_inactive)
            )
            view.setImageDrawable(drwbl)
        }

        protectClipImageView?.let { view ->
            val drwbl = view.drawable
            DrawableCompat.setTint(drwbl,
                    this@DetailClipDialogFragment.requireContext().getColor(
                            if (clip.isProtected) R.color.pantone_orange else R.color.favorite_inactive)
            )
            view.setImageDrawable(drwbl)
        }
    }

    private val copyClipObserver = Observer<String> {
        it.copyInClipboardWithToast(R.string.copied_alert)
    }

    private val deleteClipObserver = Observer<Boolean> {
        if (it) {
            Toast.makeText(requireContext(), "Clip was deleted", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    override fun getLayoutId(): Int = R.layout.dialog_fragment_clip_detail

    @ExperimentalCoroutinesApi
    override fun initViews(view: View) {
        channelNameTextView = view.findViewById(R.id.channel_name_text_view)
        createDateTextView = view.findViewById(R.id.create_date_text_view)
        clipDataTextView = view.findViewById(R.id.clip_data_text_view)
        favoriteButton = view.findViewById(R.id.favorite_button)
        copyButton = view.findViewById(R.id.copy_button)
        deleteButton = view.findViewById(R.id.delete_button)
        protectClipImageView = view.findViewById(R.id.protect_clip)

        copyButton?.setOnClickListener {
            viewModel.copyClip(model.id)
        }

        deleteButton?.setOnClickListener {
            viewModel.deleteClip(model.id)
        }

        favoriteButton?.setOnClickListener {
            viewModel.markAsFavorite(model.id)
        }

        protectClipImageView?.setOnClickListener {
            viewModel.protectClip(model.id)
        }

        viewModel.detailedClip.observe(viewLifecycleOwner) {
            setDetailedClipInfo(it)
        }

        viewModel.copyClipData.observe(this, copyClipObserver)
        viewModel.onDeleteClip.observe(this, deleteClipObserver)

        setDetailedClipInfo(model)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.copyClipData.removeObserver(copyClipObserver)
        viewModel.onDeleteClip.removeObserver(deleteClipObserver)
    }


}