package com.dicoding.netflock.favorite.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavMoviesViewModelTest{
    private lateinit var viewModel: FavMoviesViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var netflockRepository: com.dicoding.netflock.core.data.NetflockRepository

    @Mock
    private lateinit var observer: Observer<PagedList<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>>

    @Mock
    private lateinit var pagedList: PagedList<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>

    @Before
    fun setup() {
        viewModel = FavMoviesViewModel(netflockRepository)
    }

    @Test
    fun getFavMovies() {
        val dummyMovies = pagedList
        `when`(dummyMovies.size).thenReturn(10)
        val movies = MutableLiveData<PagedList<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>>()
        movies.value = dummyMovies

        `when`(netflockRepository.getFavMovies()).thenReturn(movies)
        val movieEntities = viewModel.getFavMovies().value
        verify<com.dicoding.netflock.core.data.NetflockRepository>(netflockRepository).getFavMovies()
        assertNotNull(movieEntities)
        assertEquals(10, movieEntities?.size)

        viewModel.getFavMovies().observeForever(observer)
        verify(observer).onChanged(dummyMovies)
    }
}