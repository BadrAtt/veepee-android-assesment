package com.vp.favorites

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.favorites.databinding.FragmentFavoritesBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class FavoritesFragment : DaggerFragment() {

    private lateinit var bindig: FragmentFavoritesBinding
    private lateinit var favoritesViewModel: FavoritesViewModel

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val favoritesAdapter: ListAdapter by lazy {
        ListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindig = FragmentFavoritesBinding.inflate(layoutInflater)
        return bindig.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesViewModel =
            ViewModelProvider(requireActivity(), factory)[FavoritesViewModel::class.java]
        favoritesViewModel.fetchFavoriteMovies().observe(viewLifecycleOwner) {
            favoritesAdapter.setItems(it)
        }

        setupList()
    }

    private fun setupList() {
        val gridLayoutManager = GridLayoutManager(
            context,
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
        )

        bindig.idFavMoviesList.apply {
            setHasFixedSize(true)
            adapter = favoritesAdapter
            layoutManager = gridLayoutManager
        }
    }
}