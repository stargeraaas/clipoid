package dev.sukharev.clipangel.presentation.fragments.cliplist

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.ToolbarPresenter
import dev.sukharev.clipangel.presentation.fragments.BaseFragment
import dev.sukharev.clipangel.presentation.fragments.bottom.ListBottomDialogFragment
import dev.sukharev.clipangel.presentation.fragments.bottom.SingleListAdapter
import dev.sukharev.clipangel.presentation.models.Category
import dev.sukharev.clipangel.presentation.view.info.InformationView
import dev.sukharev.clipangel.presentation.viewmodels.channellist.MainViewModel
import dev.sukharev.clipangel.utils.copyInClipboardWithToast
import org.koin.android.ext.android.inject


class ClipsFragment : BaseFragment(), OnClipItemClickListener {

    private val viewModel: ClipListViewModel by inject()
    private lateinit var mainViewModel: MainViewModel

    private var emptyClipList: InformationView? = null
    private var errorLayout: InformationView? = null
    private var contentLayout: ConstraintLayout? = null

    private val args: ClipsFragmentArgs by navArgs()

    private val errorObserver = Observer<Throwable> {
        errorLayout?.visibility = if (it == null) View.GONE else View.VISIBLE
    }

    private val detailedClipObserver = Observer<ClipListViewModel.DetailedClipModel> {
        DetailClipDialogFragment(it).show(childFragmentManager, "clip_detail_bottom_dialog")
    }

    private val permitClipAccessObserver = Observer<String> {
        mainViewModel.openBiometryDialogForClip(it)
    }

    private val clipListAdapter = ClipListAdapter {
        viewModel.createClipAction(ClipListViewModel.ClipAction.Copy(it, false))
    }.apply {
        onItemCLickListener = this@ClipsFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        getNavDrawer().enabled(true)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }


    override fun initToolbar(presenter: ToolbarPresenter) {
        presenter.getToolbar()?.apply {
            title = "Список клипов"
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_category)
            setNavigationIconTint(requireContext().getColor(R.color.pantone_light_green))

            setNavigationOnClickListener {
                createCategoryTypesDialog(viewModel.categoryTypeLiveData.value!!).show(childFragmentManager, "list_bottom")
            }
        }
    }

    private fun createCategoryTypesDialog(selectedCategory: Category): ListBottomDialogFragment {
        val items = listOf(
                ListBottomDialogFragment.ListItem(Category.All().id, getString(R.string.all),
                        R.drawable.ic_list_2),
                ListBottomDialogFragment.ListItem(Category.Favorite().id, getString(R.string.favorite),
                        R.drawable.ic_star),
                ListBottomDialogFragment.ListItem(Category.Private().id, getString(R.string.secured),
                        R.drawable.ic_lock)
        )

        // Mark category as selected
        items.find { it.id == selectedCategory.id }?.isSelected = true

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

    private val forceDetailObserver = Observer<String> { id ->
        id?.let {
            onItemClicked(it)
            mainViewModel.forceDetail.value = null
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
            mainViewModel.forceDetail.observe(viewLifecycleOwner, forceDetailObserver)
        }
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

    private lateinit var searchView: SearchView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.clip_list, menu)
        val myActionMenuItem  = menu.findItem(R.id.action_search)
        val searchActionManager  = (myActionMenuItem.actionView as SearchView).also {
            searchView = it
        }

        searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)?.apply {
            setTextColor(resources.getColor(R.color.pantone_light_green, null))
            DrawableCompat.setTint(textCursorDrawable!!, resources.getColor(R.color.pantone_light_green, null))
            setHint(getString(R.string.search_hint))
            setHintTextColor(resources.getColor(R.color.pantone_light_2, null))
        }

        searchView.background = null

        searchActionManager.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchByText(newText)
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
//            R.id.action_find -> {
//                FilterBottomDialogFragment().show(childFragmentManager, "filter_fragment")
//            }
        }
        return false
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

        viewModel.detailedClip.observe(viewLifecycleOwner, detailedClipObserver)
        viewModel.errorLiveData.observe(viewLifecycleOwner, errorObserver)
        viewModel.clipItemsLiveData.observe(viewLifecycleOwner, clipListObserver)
        viewModel.permitClipAccessLiveData.observe(viewLifecycleOwner, permitClipAccessObserver)

        viewModel.copyClipData.observe(viewLifecycleOwner) {
            it.copyInClipboardWithToast(getString(R.string.copied_alert))
        }

        mainViewModel.permitAccessForClip.observe(viewLifecycleOwner) {
            it?.let { clipId ->
                val action: ClipListViewModel.ClipAction? = viewModel.clipAction.value

                when (action) {
                    is ClipListViewModel.ClipAction.ShowDetail ->
                        viewModel.createClipAction(ClipListViewModel.ClipAction.ShowDetail(clipId, true))

                    is ClipListViewModel.ClipAction.Copy -> {
                        viewModel.copyClip(clipId)
                    }
                }

                mainViewModel.permitAccessForClip.value = null
            }
        }

        view.findViewById<RecyclerView>(R.id.clip_list_recycler)?.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = clipListAdapter
        }


        viewModel.loadClips()
    }

    override fun onItemClicked(id: String) {
        viewModel.createClipAction(ClipListViewModel.ClipAction.ShowDetail(id, false))
    }

}