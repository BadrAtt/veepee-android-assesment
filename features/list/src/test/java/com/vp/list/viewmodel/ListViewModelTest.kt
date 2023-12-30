package com.vp.list.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vp.list.model.SearchResponse;
import com.vp.list.service.SearchService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import retrofit2.mock.Calls;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskRule = new InstantTaskExecutorRule();
    private SearchResponse dummySearchResponse;

    @Before
    public void setupDummySearchResult() {
        String responseJsonString = "{\n" +
                "  \"Search\": [\n" +
                "    {\n" +
                "      \"Title\": \"The Interview\",\n" +
                "      \"Year\": \"2014\",\n" +
                "      \"imdbID\": \"tt2788710\",\n" +
                "      \"Type\": \"movie\",\n" +
                "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTQzMTcwMzgyMV5BMl5BanBnXkFtZTgwMzAyMzQ2MzE@._V1_SX300.jpg\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"Title\": \"Interview with the Vampire: The Vampire Chronicles\",\n" +
                "      \"Year\": \"1994\",\n" +
                "      \"imdbID\": \"tt0110148\",\n" +
                "      \"Type\": \"movie\",\n" +
                "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BYThmYjJhMGItNjlmOC00ZDRiLWEzNjUtZjU4MjA3MzY0MzFmXkEyXkFqcGdeQXVyNTI4MjkwNjA@._V1_SX300.jpg\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"Title\": \"Interview with the Vampire\",\n" +
                "      \"Year\": \"2022–\",\n" +
                "      \"imdbID\": \"tt14921986\",\n" +
                "      \"Type\": \"series\",\n" +
                "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMmJiMTA0ZDctMTQ1OS00Y2FhLWI4MjUtZWRkMmU5ZjA2MTM3XkEyXkFqcGdeQXVyMDM2NDM2MQ@@._V1_SX300.jpg\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"Title\": \"Interview\",\n" +
                "      \"Year\": \"2007\",\n" +
                "      \"imdbID\": \"tt0480269\",\n" +
                "      \"Type\": \"movie\",\n" +
                "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMjU5ODAzZjItMmVhMy00YTc5LWEzNjktM2IwOWFlZGVmZmZmXkEyXkFqcGdeQXVyNjk1Njg5NTA@._V1_SX300.jpg\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"Title\": \"Interview with a Hitman\",\n" +
                "      \"Year\": \"2012\",\n" +
                "      \"imdbID\": \"tt2061712\",\n" +
                "      \"Type\": \"movie\",\n" +
                "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BY2NjODAyMGYtOWVjMy00MGMzLTgzNGItY2RmZDJmNjU1YThjXkEyXkFqcGdeQXVyNzQ5MzY0NjM@._V1_SX300.jpg\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"Title\": \"The Interview\",\n" +
                "      \"Year\": \"1998\",\n" +
                "      \"imdbID\": \"tt0120714\",\n" +
                "      \"Type\": \"movie\",\n" +
                "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BYWNkOTc5MjgtMzM4Zi00MjVjLWFjNWItNmRiMjIzZjgzYTM2XkEyXkFqcGdeQXVyMTMxMTY0OTQ@._V1_SX300.jpg\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"Title\": \"An Interview with God\",\n" +
                "      \"Year\": \"2018\",\n" +
                "      \"imdbID\": \"tt5779372\",\n" +
                "      \"Type\": \"movie\",\n" +
                "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTk0ODQ3MzM5Nl5BMl5BanBnXkFtZTgwMzkyOTY4NTM@._V1_SX300.jpg\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"Title\": \"Steve Jobs: The Lost Interview\",\n" +
                "      \"Year\": \"2012\",\n" +
                "      \"imdbID\": \"tt2104994\",\n" +
                "      \"Type\": \"movie\",\n" +
                "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNjk1NDE5MDExMV5BMl5BanBnXkFtZTcwNDYyMzYwNw@@._V1_SX300.jpg\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"Title\": \"Interview with the Assassin\",\n" +
                "      \"Year\": \"2002\",\n" +
                "      \"imdbID\": \"tt0308411\",\n" +
                "      \"Type\": \"movie\",\n" +
                "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMjAwNDE3MjYxOF5BMl5BanBnXkFtZTYwNTE0MDg5._V1_SX300.jpg\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"Title\": \"Interview\",\n" +
                "      \"Year\": \"2003\",\n" +
                "      \"imdbID\": \"tt0360674\",\n" +
                "      \"Type\": \"movie\",\n" +
                "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BZDg5ZDlmMGYtYWZiMS00YWRlLTk4MGMtM2MzOWIxMWJmNGY0XkEyXkFqcGdeQXVyMjA0MzYwMDY@._V1_SX300.jpg\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"totalResults\": \"1986\",\n" +
                "  \"Response\": \"True\"\n" +
                "}";

        Type type = new TypeToken<SearchResponse>() {
        }.getType();
        dummySearchResponse = new Gson().fromJson(responseJsonString, type);

    }

    @Test
    public void shouldReturnErrorState() {
        //given
        SearchService searchService = mock(SearchService.class);
        when(searchService.search(anyString(), anyInt())).thenReturn(Calls.failure(new IOException()));
        ListViewModel listViewModel = new ListViewModel(searchService);

        //when
        listViewModel.searchMoviesByTitle("title", 1);

        //then
        assertThat(listViewModel.observeMovies().getValue().getListState()).isEqualTo(ListState.ERROR);
    }

    @Test
    public void shouldReturnInProgressState() {
        //given
        SearchService searchService = mock(SearchService.class);
        when(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(mock(SearchResponse.class)));
        ListViewModel listViewModel = new ListViewModel(searchService);
        Observer<SearchResult> mockObserver = (Observer<SearchResult>) mock(Observer.class);
        listViewModel.observeMovies().observeForever(mockObserver);

        //when
        listViewModel.searchMoviesByTitle("title", 1);

        //then
        verify(mockObserver).onChanged(SearchResult.inProgress());
        listViewModel.observeMovies().removeObserver(mockObserver);
    }

    @Test
    public void shouldSetLiveDataWithSuccessState() {
        //Given
        SearchService searchService = mock(SearchService.class);
        when(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(dummySearchResponse));
        ListViewModel listViewModel = new ListViewModel(searchService);
        Observer<SearchResult> mockObserver = (Observer<SearchResult>) mock(Observer.class);
        listViewModel.observeMovies().observeForever(mockObserver);

        //When
        listViewModel.searchMoviesByTitle("Interview", 1);

        //then
        SearchResult expextedSuccessResult = SearchResult.success(dummySearchResponse.getSearch(), dummySearchResponse.getSearch().size());
        verify(mockObserver).onChanged(expextedSuccessResult);
        assertThat(listViewModel.observeMovies().getValue().getListState()).isEqualTo(ListState.LOADED);

        listViewModel.observeMovies().removeObserver(mockObserver);
    }

    @Test
    public void shouldReturnStateLoadedWhenSuccess() {
        //Given
        SearchService searchService = mock(SearchService.class);
        when(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(dummySearchResponse));
        ListViewModel listViewModel = new ListViewModel(searchService);
        Observer<SearchResult> mockObserver = (Observer<SearchResult>) mock(Observer.class);
        listViewModel.observeMovies().observeForever(mockObserver);

        //When
        listViewModel.searchMoviesByTitle("Interview", 1);

        //then
        assertThat(listViewModel.observeMovies().getValue().getListState()).isEqualTo(ListState.LOADED);

        listViewModel.observeMovies().removeObserver(mockObserver);
    }
}