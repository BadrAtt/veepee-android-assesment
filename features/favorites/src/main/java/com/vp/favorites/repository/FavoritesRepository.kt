package com.vp.favorites.repository

import com.vp.core_models.MovieDetail
import io.realm.kotlin.query.RealmResults

interface FavoritesRepository {

    suspend fun getFavoriteMovies(): RealmResults<MovieDetail>
}