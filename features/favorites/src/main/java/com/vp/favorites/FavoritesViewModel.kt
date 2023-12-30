package com.vp.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.vp.favorites.repository.FavoritesRepository
import io.realm.kotlin.Realm
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val realm: Realm
) : ViewModel() {

    fun fetchFavoriteMovies() = liveData {
        val result = favoritesRepository.getFavoriteMovies()
        emit(realm.copyFromRealm(result))
    }
}