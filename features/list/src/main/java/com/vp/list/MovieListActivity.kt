package com.vp.list

import android.os.Bundle
import android.view.Menu
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.vp.list.databinding.ActivityMovieListBinding
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MovieListActivity : AppCompatActivity(), HasAndroidInjector {


    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Any>

    private lateinit var activityViewBinding: ActivityMovieListBinding
    private lateinit var searchView: SearchView

    private var searchViewExpanded = true
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        activityViewBinding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(activityViewBinding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, ListFragment(), ListFragment.TAG)
                .commit()
        } else {
            searchViewExpanded = savedInstanceState.getBoolean(IS_SEARCH_VIEW_ICONIFIED)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val menuItem = menu.findItem(R.id.search)
        searchView = (menuItem.actionView as SearchView).apply {
            imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
            isIconified = searchViewExpanded
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    val listFragment =
                        supportFragmentManager.findFragmentByTag(ListFragment.TAG) as ListFragment
                    listFragment.submitSearchQuery(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_SEARCH_VIEW_ICONIFIED, searchView.isIconified)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingActivityInjector
    }

    fun getSearchView() = searchView

    companion object {
        private const val IS_SEARCH_VIEW_ICONIFIED = "is_search_view_iconified"
    }
}