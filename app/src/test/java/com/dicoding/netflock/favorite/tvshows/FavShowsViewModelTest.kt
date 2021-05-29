package com.dicoding.netflock.favorite.tvshows

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
class FavShowsViewModelTest {
    private lateinit var viewModel: FavShowsViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var netflockRepository: com.dicoding.netflock.core.data.NetflockRepository

    @Mock
    private lateinit var observer: Observer<PagedList<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>>

    @Mock
    private lateinit var pagedList: PagedList<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>

    @Before
    fun setup() {
        viewModel = FavShowsViewModel(netflockRepository)
    }

    @Test
    fun getFavMovies() {
        val dummyShows = pagedList
        `when`(dummyShows.size).thenReturn(10)
        val shows = MutableLiveData<PagedList<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>>()
        shows.value = dummyShows

        `when`(netflockRepository.getFavShows()).thenReturn(shows)
        val showEntities = viewModel.getFavShows().value
        verify<com.dicoding.netflock.core.data.NetflockRepository>(netflockRepository).getFavShows()
        assertNotNull(showEntities)
        assertEquals(10, showEntities?.size)

        viewModel.getFavShows().observeForever(observer)
        verify(observer).onChanged(dummyShows)
    }
}