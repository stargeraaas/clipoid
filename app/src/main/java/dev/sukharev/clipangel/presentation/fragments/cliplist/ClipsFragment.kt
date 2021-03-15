package dev.sukharev.clipangel.presentation.fragments.cliplist

import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.ToolbarPresenter
import dev.sukharev.clipangel.presentation.fragments.BaseFragment
import dev.sukharev.clipangel.presentation.fragments.bottom.ListBottomDialogFragment
import dev.sukharev.clipangel.presentation.fragments.bottom.SingleListAdapter
import dev.sukharev.clipangel.presentation.models.Category
import dev.sukharev.clipangel.presentation.view.info.InformationView
import org.koin.android.ext.android.inject


class ClipsFragment: BaseFragment(), OnClipItemClickListener {

    private var emptyClipList: InformationView? = null
    private var errorLayout: InformationView? = null
    private var contentLayout: ConstraintLayout? = null

    private val clipListAdapter = ClipListAdapter().apply {
        onItemCLickListener = this@ClipsFragment
    }

    private val viewModel: ClipListViewModel by inject()

    override fun initToolbar(presenter: ToolbarPresenter) {
        presenter.getToolbar()?.apply {
            title = "Список клипов"
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_category)
            setNavigationIconTint(requireContext().getColor(R.color.pantone_light_green))

            setNavigationOnClickListener {
                createCategoryTypesDialog().show(childFragmentManager, "list_bottom")
            }
        }
    }

    private fun createCategoryTypesDialog(): ListBottomDialogFragment {
        val items = listOf(
                ListBottomDialogFragment.ListItem(Category.All().id, getString(R.string.all),
                        R.drawable.ic_list_2),
                ListBottomDialogFragment.ListItem(Category.Favorite().id, getString(R.string.favorite),
                        R.drawable.ic_star),
                ListBottomDialogFragment.ListItem(Category.Private().id, getString(R.string.secured),
                        R.drawable.ic_lock, isSelected = true)
        )

        return ListBottomDialogFragment(items, getString(R.string.categories)).also { dialog ->
            dialog.setOnItemClickListener(object : SingleListAdapter.OnItemClickListener {
                override fun onClick(item: ListBottomDialogFragment.ListItem) {
                    Category.getById(item.id)?.also {
                        dialog.dismiss()
                        viewModel.changeCategoryType(it)
                    }
                }
            })
        }
    }


    override fun showBottomNavigation(): Boolean = true

    private val errorObserver = Observer<Throwable> {
        if (it == null)
            errorLayout?.visibility = View.GONE
        else {
            errorLayout?.visibility = View.VISIBLE
        }
    }

    private val clipListObserver = Observer<List<ClipItemViewHolder.Model>> {
        if (it.isEmpty()) {
            emptyClipList?.visibility = View.VISIBLE
            contentLayout?.visibility = View.GONE
        } else {
            clipListAdapter.setItems(it)
            emptyClipList?.visibility = View.GONE
            contentLayout?.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        getNavDrawer().enabled(true)
    }

    private fun setToolbarTitleByCategory(category: Category) {
        getToolbarPresenter().apply {
            setTitle(when (category) {
                is Category.All -> getString(R.string.category_all)
                is Category.Favorite -> getString(R.string.category_favorite)
                is Category.Private -> getString(R.string.categoty_protected)
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.clip_list, menu)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_clip_list, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyClipList = view.findViewById(R.id.empty_content_template)
        errorLayout = view.findViewById(R.id.error_view)
        contentLayout = view.findViewById(R.id.content_layout)

        viewModel.categoryTypeLiveData.observe(viewLifecycleOwner) {
            setToolbarTitleByCategory(it)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner, errorObserver)
        viewModel.clipItemsLiveData.observe(viewLifecycleOwner, clipListObserver)

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