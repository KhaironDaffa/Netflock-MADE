package com.dicoding.netflock.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dicoding.netflock.core.utils.DataDummy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailsViewModelTest{

    private lateinit var viewModel: DetailsViewModel
    private val dummyMovies = DataDummy.generateDummyMovie()[0]
    private val dummyShows = DataDummy.generateDummyTvShow()[0]
    private val movieId = dummyMovies.movieId
    private val showId = dummyShows.tvShowId

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var netflockRepository: com.dicoding.netflock.core.data.NetflockRepository

    @Mock
    private lateinit var movieObserver: Observer<com.dicoding.netflock.core.data.Resource<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>>

    @Mock
    private lateinit var showObserver: Observer<com.dicoding.netflock.core.data.Resource<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>>

    @Before
    fun setUp() {
        viewModel = DetailsViewModel(netflockRepository)
        viewModel.setMovies(movieId)
        viewModel.setShow(showId)
    }

    @Test
    fun getMovies() {
        val dummyMovie = com.dicoding.netflock.core.data.Resource.success(DataDummy.generateDummyMovie()[0])
        val movie = MutableLiveData<com.dicoding.netflock.core.data.Resource<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>>()
        movie.value = dummyMovie

        `when`(netflockRepository.getMovie(movieId)).thenReturn(movie)
        viewModel.getMovies.observeForever(movieObserver)
        verify(movieObserver).onChanged(dummyMovie)
    }

    @Test
    fun getShow() {
        val dummyShow = com.dicoding.netflock.core.data.Resource.success(DataDummy.generateDummyTvShow()[0])
        val show = MutableLiveData<com.dicoding.netflock.core.data.Resource<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>>()
        show.value = dummyShow

        `when`(netflockRepository.getTvShow(showId)).thenReturn(show)
        viewModel.getShow.observeForever(showObserver)
        verify(showObserver).onChanged(dummyShow)
    }

    @Test
    fun setFavMovie() {
        val dummyMovie = com.dicoding.netflock.core.data.Resource.success(DataDummy.generateDummyMovie()[0])
        val movie = MutableLiveData<com.dicoding.netflock.core.data.Resource<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>>()
        movie.value = dummyMovie

        `when`(netflockRepository.getMovie(movieId)).thenReturn(movie)
        viewModel.getMovies = netflockRepository.getMovie(movieId)
        viewModel.setFavMovies()
        verify(netflockRepository).setFavMovies(movie.value?.data as com.dicoding.netflock.core.data.source.local.entity.MovieEntity, true)
        verifyNoMoreInteractions(movieObserver)
    }

    @Test
    fun setFavShow() {
        val dummyShow = com.dicoding.netflock.core.data.Resource.success(DataDummy.generateDummyTvShow()[0])
        val show = MutableLiveData<com.dicoding.netflock.core.data.Resource<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>>()
        show.value = dummyShow

        `when`(netflockRepository.getTvShow(showId)).thenReturn(show)
        viewModel.getShow = netflockRepository.getTvShow(showId)
        viewModel.setFavShows()
        verify(netflockRepository).setFavShows(show.value?.data as com.dicoding.netflock.core.data.source.local.entity.TvShowEntity, true)
        verifyNoMoreInteractions(showObserver)
    }

}