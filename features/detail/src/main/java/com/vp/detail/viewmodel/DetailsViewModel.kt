package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vp.core_models.MovieDetail
import com.vp.detail.repository.DetailRepository
import com.vp.detail.service.DetailService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

open class DetailsViewModel @Inject constructor(
    private val detailService: DetailService,
    private val detailRepository: DetailRepository,
) : ViewModel() {

    private val details: MutableLiveData<MovieDetail> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val favoriteState = MutableLiveData<FavoriteState>()

    private var movieId: String? = null

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieDetail> = details

    fun loadingState(): LiveData<LoadingState> = loadingState

    fun favoriteState(): LiveData<FavoriteState> = favoriteState


    fun fetchDetails(movieId: String) {
        this.movieId = movieId
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(movieId)
            .enqueue(object : Callback, retrofit2.Callback<MovieDetail> {
                override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                    details.postValue(response.body()?.apply {
                        //a workaround to attach the movieId to the movieDetail item since we do not
                        //receive it from the api
                        this.movieId = movieId
                    })
                    viewModelScope.launch {
                        val favMovie = fetchFavoriteMovie(movieId)
                        favoriteState.value =
                            if (favMovie == null) FavoriteState.IDLE else FavoriteState.FAVORITE
                    }
                    response.body()?.title?.let {
                        title.postValue(it)
                    }

                    loadingState.value = LoadingState.LOADED
                }

                override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                    details.postValue(null)
                    loadingState.value = LoadingState.ERROR
                }
            })
    }


    fun updateMovieFavoriteState() {
        viewModelScope.launch {
            val favMovie = movieId?.let { id -> fetchFavoriteMovie(id) }
            if (favMovie == null) saveMovieToFavorites() else deleteMovieFromFavorites()
        }
    }

    open fun saveMovieToFavorites() {
        viewModelScope.launch {
            details.value?.let { movieDetail ->
                try {
                    detailRepository.saveMovieToFavorites(movieDetail)
                    favoriteState.value = FavoriteState.ADDED
                } catch (e: Exception) {
                    favoriteState.value = FavoriteState.ERROR
                }
            }
        }
    }

    open fun deleteMovieFromFavorites() {
        viewModelScope.launch {
            details.value?.let { movieDetail ->
                try {
                    detailRepository.removeMovieFromFavorites(movieDetail)
                    favoriteState.value = FavoriteState.DELETED
                } catch (e: Exception) {
                    favoriteState.value = FavoriteState.ERROR
                }
            }
        }
    }

    open suspend fun fetchFavoriteMovie(movieId: String): MovieDetail? {
        return try {
            val favoriteMovie = detailRepository.fetchMovieItem(movieId = movieId)
            favoriteMovie?.let {
                favoriteState.value = FavoriteState.FAVORITE
            } ?: kotlin.run {
                favoriteState.value = FavoriteState.IDLE
            }
            favoriteMovie
        } catch (e: Exception) {
            favoriteState.value = FavoriteState.ERROR
            null
        }
    }


    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }

    enum class FavoriteState {
        IDLE,
        FAVORITE,
        DELETED,
        ADDED,
        ERROR,
    }
}