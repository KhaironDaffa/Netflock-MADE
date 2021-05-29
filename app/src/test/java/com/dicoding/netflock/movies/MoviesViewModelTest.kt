package com.dicoding.netflock.movies

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
class MoviesViewModelTest {

    private lateinit var viewModel: MoviesViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var netflockRepository: com.dicoding.netflock.core.data.NetflockRepository

    @Mock
    private lateinit var observer: Observer<com.dicoding.netflock.core.data.Resource<PagedList<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>>>

    @Mock
    private lateinit var pagedList: PagedList<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>

    @Before
    fun setUp() {
        viewModel = MoviesViewModel(netflockRepository)
    }

    @Test
    fun getMovies() {
        val dummyMovies = com.dicoding.netflock.core.data.Resource.success(pagedList)
        `when`(dummyMovies.data?.size).thenReturn(10)
        val movies = MutableLiveData<com.dicoding.netflock.core.data.Resource<PagedList<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>>>()
        movies.value = dummyMovies

        `when`(netflockRepository.getAllMovies()).thenReturn(movies)
        val movieEntities = viewModel.getMovies().value?.data
        verify<com.dicoding.netflock.core.data.NetflockRepository>(netflockRepository).getAllMovies()
        assertNotNull(movieEntities)
        assertEquals(10, movieEntities?.size)

        viewModel.getMovies().observeForever(observer)
        verify(observer).onChanged(dummyMovies)
    }
}