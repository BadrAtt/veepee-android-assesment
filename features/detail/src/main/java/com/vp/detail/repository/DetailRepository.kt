package com.vp.detail.repository

import com.vp.detail.model.MovieDetail

interface DetailRepository {

    suspend fun saveMovieToFavorites(movieDetail: MovieDetail)
    suspend fun removeMovieFromFavorites(movieDetail: MovieDetail)
    suspend fun fetchMovieItem(movieId: String): MovieDetail?
}