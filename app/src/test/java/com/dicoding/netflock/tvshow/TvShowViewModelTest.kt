package com.dicoding.netflock.tvshow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TvShowViewModelTest {

    private lateinit var viewModel: TvShowViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var netflockRepository: com.dicoding.netflock.core.data.NetflockRepository

    @Mock
    private lateinit var observer: Observer<com.dicoding.netflock.core.data.Resource<PagedList<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>>>

    @Mock
    private lateinit var pagedList: PagedList<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>

    @Before
    fun setUp() {
        viewModel = TvShowViewModel(netflockRepository)
    }

    @Test
    fun getShow() {
        val dummyShows = com.dicoding.netflock.core.data.Resource.success(pagedList)
        `when`(dummyShows.data?.size).thenReturn(10)
        val show = MutableLiveData<com.dicoding.netflock.core.data.Resource<PagedList<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>>>()
        show.value = dummyShows

        `when`(netflockRepository.getAllTvShows()).thenReturn(show)
        val showEntities = viewModel.getShow().value?.data
        verify<com.dicoding.netflock.core.data.NetflockRepository>(netflockRepository).getAllTvShows()
        assertNotNull(showEntities)
        assertEquals(10, showEntities?.size)

        viewModel.getShow().observeForever(observer)
        verify(observer).onChanged(dummyShows)
    }
}