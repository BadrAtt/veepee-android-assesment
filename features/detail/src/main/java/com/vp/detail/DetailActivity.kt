package com.vp.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vp.detail.databinding.ActivityDetailBinding
import com.vp.detail.viewmodel.DetailsViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity(), QueryProvider {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var detailViewModel: DetailsViewModel
    private var acionBarMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, factory)[DetailsViewModel::class.java]
        binding.viewModel = detailViewModel
        binding.lifecycleOwner = this

        intent?.data?.getQueryParameter("imdbID")?.let { id ->
            detailViewModel.fetchDetails(id)
        } ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }

        detailViewModel.title().observe(this) {
            supportActionBar?.title = it
        }
        detailViewModel.favoriteState().observe(this) { state ->
            handleFavoriteState(state)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        acionBarMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.star) {
            detailViewModel.updateMovieFavoriteState()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getMovieId(): String {
        return intent?.data?.getQueryParameter("imdbID") ?: run {
            throw IllegalStateException("You must provide movie id to display details")
        }
    }

    private fun handleFavoriteState(state: DetailsViewModel.FavoriteState) {
        when (state) {
            DetailsViewModel.FavoriteState.ADDED -> {
                updateFavoriteMenuItemIcon(R.drawable.ic_star_filled)
                Toast.makeText(this, "Movie Added to Favorites!", Toast.LENGTH_SHORT).show()
            }

            DetailsViewModel.FavoriteState.DELETED -> {
                updateFavoriteMenuItemIcon(R.drawable.ic_star)
                Toast.makeText(this, "Movie removed from Favorites", Toast.LENGTH_SHORT).show()
            }

            DetailsViewModel.FavoriteState.ERROR -> {
                updateFavoriteMenuItemIcon(R.drawable.ic_star)
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }

            DetailsViewModel.FavoriteState.FAVORITE -> {
                updateFavoriteMenuItemIcon(R.drawable.ic_star_filled)
            }

            DetailsViewModel.FavoriteState.IDLE -> {
                updateFavoriteMenuItemIcon(R.drawable.ic_star)
            }
        }
    }

    private fun updateFavoriteMenuItemIcon(resId: Int) {
        acionBarMenu?.findItem(R.id.star)?.setIcon(resId)
    }
}
