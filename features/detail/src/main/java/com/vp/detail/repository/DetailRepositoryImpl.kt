package com.vp.detail.repository

import com.vp.core_models.MovieDetail
import io.realm.kotlin.Realm
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val relamDb: Realm
) : DetailRepository {
    override suspend fun saveMovieToFavorites(movieDetail: MovieDetail) {
        relamDb.writeBlocking {
            copyToRealm(movieDetail)
        }
    }

    override suspend fun removeMovieFromFavorites(movieDetail: MovieDetail) {
        relamDb.writeBlocking {
            val writeTransactionItems = query(MovieDetail::class).find()
            delete(writeTransactionItems.first())
        }
    }

    override suspend fun fetchMovieItem(movieId: String) =
        relamDb.query(MovieDetail::class, "movieId == $0", movieId).first().find()
}