package dev.sukharev.clipangel.presentation.fragments.cliplist

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.ToolbarPresenter
import dev.sukharev.clipangel.presentation.fragments.BaseFragment
import dev.sukharev.clipangel.presentation.fragments.bottom.ListBottomDialogFragment
import org.koin.android.ext.android.inject


class ClipsFragment : BaseFragment(), OnClipItemClickListener {

    private val clipListAdapter = ClipListAdapter().apply {
        onItemCLickListener = this@ClipsFragment
    }

    private val viewModel: ClipListViewModel by inject()

    override fun initToolbar(presenter: ToolbarPresenter) {
        presenter.getToolbar()?.apply {
            title = "HELLOW"
        }
    }

    override fun showBottomNavigation(): Boolean = true

    private val clipListObserver = Observer<List<ClipItemViewHolder.Model>> {
        clipListAdapter.setItems(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.clipItemsLiveData.observe(requireActivity(), clipListObserver)
        getNavDrawer().enabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clipItemsLiveData.removeObserver(clipListObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_clips, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.clip_list_recycler)?.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = clipListAdapter
        }

        viewModel.loadClips()
    }

    override fun onItemClicked(id: String) {
        DetailClipDialogFragment(id).show(childFragmentManager, "clip_detail_bottom_dialog")
    }
}