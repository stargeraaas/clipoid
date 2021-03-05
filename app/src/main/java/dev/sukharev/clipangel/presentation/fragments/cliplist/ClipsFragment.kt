package dev.sukharev.clipangel.presentation.fragments.cliplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.ToolbarPresenter
import dev.sukharev.clipangel.presentation.fragments.BaseFragment
import org.koin.android.ext.android.inject


class ClipsFragment: BaseFragment(), OnClipItemClickListener {

    private val clipListAdapter = ClipListAdapter().apply {
        onItemCLickListener = this@ClipsFragment
    }

    private val viewModel: ClipListViewModel by inject()

    override fun initToolbar(presenter: ToolbarPresenter) {
        presenter.show()
        presenter.setTitle("Мои клипы")
    }

    override fun showBottomNavigation(): Boolean = true

    private val clipListObserver = Observer<List<ClipItemViewHolder.Model>> {
        clipListAdapter.setItems(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clipItemsLiveData.observe(requireActivity(), clipListObserver)
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
        println("CLICKED ON ITEM WITH ID $id")
        val fragment = DetailClipDialogFragment()
        fragment.show(childFragmentManager, "clip_detail_bottom_dialog")
    }
}