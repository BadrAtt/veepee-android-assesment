package com.vp.detail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vp.detail.MainDispatcherRule
import com.vp.detail.model.MovieDetail
import com.vp.detail.repository.DetailRepository
import com.vp.detail.service.DetailService
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.atMostOnce
import org.mockito.Mockito.never
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.mock.Calls

class DetailsViewModelTest {


    @get:Rule
    var instantTaskRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Mock

    lateinit var detailService: DetailService

    @Mock
    lateinit var detailRepository: DetailRepository

    @Mock
    lateinit var mockObserver: Observer<MovieDetail>

    private lateinit var detailsViewModel: DetailsViewModel

    private lateinit var movieDetail: MovieDetail


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        val movieDetailString =
            "{\"Title\":\"The Interview\",\"Year\":\"2014\",\"Rated\":\"R\",\"Released\":\"24 Dec 2014\",\"Runtime\":\"112 min\",\"Genre\":\"Action, Adventure, Comedy\",\"Director\":\"Evan Goldberg, Seth Rogen\",\"Writer\":\"Dan Sterling, Seth Rogen, Evan Goldberg\",\"Actors\":\"James Franco, Seth Rogen, Randall Park\",\"Plot\":\"Dave Skylark and his producer Aaron Rapaport run the celebrity tabloid show \\\"Skylark Tonight\\\". When they land an interview with a surprise fan, North Korean dictator Jong-Un Kim, they are recruited by the CIA to assassinate him.\",\"Language\":\"Korean, Japanese, English\",\"Country\":\"United States\",\"Awards\":\"2 wins & 6 nominations\",\"Poster\":\"https://m.media-amazon.com/images/M/MV5BMTQzMTcwMzgyMV5BMl5BanBnXkFtZTgwMzAyMzQ2MzE@._V1_SX300.jpg\",\"Ratings\":[{\"Source\":\"Internet Movie Database\",\"Value\":\"6.5/10\"},{\"Source\":\"Rotten Tomatoes\",\"Value\":\"51%\"},{\"Source\":\"Metacritic\",\"Value\":\"52/100\"}],\"Metascore\":\"52\",\"imdbRating\":\"6.5\",\"imdbVotes\":\"348,051\",\"imdbID\":\"tt2788710\",\"Type\":\"movie\",\"DVD\":\"03 Mar 2015\",\"BoxOffice\":\"\$6,105,175\",\"Production\":\"N/A\",\"Website\":\"N/A\",\"Response\":\"True\"}"
        val type = object : TypeToken<MovieDetail?>() {}.type
        movieDetail = Gson().fromJson(movieDetailString, type)
        detailsViewModel = spy(DetailsViewModel(detailService, detailRepository))
    }

    @Test
    fun shouldGetMovieDetail() = runTest {
        //Given
        `when`(detailService.getMovie("tt2788710")).thenReturn(Calls.response(movieDetail))
        detailsViewModel.details().observeForever(mockObserver)

        //when
        detailsViewModel.fetchDetails("tt2788710")

        //then
        Assertions.assertThat(detailsViewModel.details().value?.title).isEqualTo(movieDetail.title)
        detailsViewModel.details().removeObserver(mockObserver)
    }

    @Test
    fun shouldSaveMovieToFavorites() = runTest {
        // Arrange
        `when`(detailsViewModel.fetchFavoriteMovie(movieId = "tt2788710")).thenReturn(
            null
        )

        // Act
        detailsViewModel.updateMovieFavoriteState()

        // Assert
        verify(detailsViewModel, atMostOnce()).saveMovieToFavorites()
        verify(detailsViewModel, never()).deleteMovieFromFavorites()
    }

    @Test
    fun shouldDeleteMovieFromFavorites() = runTest {
        // Arrange
        `when`(detailsViewModel.fetchFavoriteMovie(movieId = "tt2788710")).thenReturn(
            movieDetail
        )

        // Act
        detailsViewModel.updateMovieFavoriteState()

        // Assert
        verify(detailsViewModel, never()).saveMovieToFavorites()
        verify(detailsViewModel, atMostOnce()).deleteMovieFromFavorites()
    }
}