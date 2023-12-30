package com.vp.favorites.repository

import com.vp.core_models.MovieDetail
import io.realm.kotlin.Realm
import io.realm.kotlin.query.RealmResults
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val realmDb: Realm
) : FavoritesRepository {

    override suspend fun getFavoriteMovies(): RealmResults<MovieDetail> =
        realmDb.query(MovieDetail::class).find()
}