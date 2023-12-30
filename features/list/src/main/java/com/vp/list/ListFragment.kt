package com.vp.list

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.list.GridPagingScrollListener.LoadMoreItemsListener
import com.vp.list.databinding.FragmentListBinding
import com.vp.list.viewmodel.ListState
import com.vp.list.viewmodel.ListViewModel
import com.vp.list.viewmodel.SearchResult
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ListFragment : Fragment(), LoadMoreItemsListener, ListAdapter.OnItemClickListener {


    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: FragmentListBinding
    private lateinit var listViewModel: ListViewModel

    private val listAdapter: ListAdapter by lazy {
        ListAdapter()
    }
    private var currentQuery: String = "Interview"
    private var currentPage = 1
    private var gridPagingScrollListener: GridPagingScrollListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        listViewModel = ViewModelProvider(this, factory)[ListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            currentQuery = savedInstanceState.getString(CURRENT_QUERY).orEmpty()
        }
        initBottomNavigation()
        initList()
        listViewModel.observeMovies().observe(viewLifecycleOwner) { searchResult ->
            binding.swipeToRefreshLayout.isRefreshing = false
            if (searchResult != null) {
                handleResult(listAdapter, searchResult)
            }
        }
        listViewModel.searchMoviesByTitle(currentQuery, currentPage)
        showProgressBar()
        binding.swipeToRefreshLayout.setOnRefreshListener {
            listViewModel.searchMoviesByTitle(
                currentQuery, currentPage
            )
        }
    }

    private fun initBottomNavigation() {
        binding.bottomNavigation.setOnItemReselectedListener { item: MenuItem ->
            if (item.itemId == R.id.favorites) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"))
                intent.setPackage(requireContext().packageName)
                startActivity(intent)
            }
        }
    }

    private fun initList() {
        listAdapter.setOnItemClickListener(this)
        val gridLayoutManager = GridLayoutManager(
            context,
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
        ).apply {
            //to have the loader view in the center of grid columns
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (listAdapter.getItemViewType(position)) {
                        ListAdapter.TYPE_ITEM -> 1
                        else -> 2
                    }
                }
            }
        }
        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(gridLayoutManager)
        gridPagingScrollListener?.setLoadMoreItemsListener(this)

        binding.recyclerView.apply {
            adapter = listAdapter
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            gridPagingScrollListener?.let { scrollListener ->
                addOnScrollListener(scrollListener)
            }
        }
    }

    private fun showProgressBar() {
        binding.viewAnimator.displayedChild = binding.viewAnimator.indexOfChild(binding.progressBar)
    }

    private fun showList() {
        binding.viewAnimator.displayedChild =
            binding.viewAnimator.indexOfChild(binding.recyclerView)
    }

    private fun showError() {
        binding.viewAnimator.displayedChild = binding.viewAnimator.indexOfChild(binding.errorText)
    }

    private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
        when (searchResult.listState) {
            ListState.LOADED -> {
                setItemsData(listAdapter, searchResult)
                showList()
            }

            ListState.IN_PROGRESS -> {
                showProgressBar()
            }

            else -> {
                showError()
            }
        }
        gridPagingScrollListener!!.markLoading(false)
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        listAdapter.setItems(searchResult.items)
        if (searchResult.totalResult <= listAdapter.itemCount) {
            gridPagingScrollListener!!.markLastPage(true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_QUERY, currentQuery)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val searchView = (requireActivity() as MovieListActivity).getSearchView()
        if (searchView.isIconified) {
            searchView.setQuery(currentQuery, false)
        }
    }

    override fun loadMoreItems(page: Int) {
        currentPage = page
        gridPagingScrollListener?.markLoading(true)
        listViewModel.searchMoviesByTitle(currentQuery, page)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter.clearItems()
        listViewModel.searchMoviesByTitle(query, 1)
        showProgressBar()
    }

    override fun onItemClick(imdbID: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/detail?imdbID=$imdbID"))
        intent.setPackage(requireContext().packageName)
        startActivity(intent)
    }

    companion object {
        const val TAG = "ListFragment"
        private const val CURRENT_QUERY = "current_query"
    }
}