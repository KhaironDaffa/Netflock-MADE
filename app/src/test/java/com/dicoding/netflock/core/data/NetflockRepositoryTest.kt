package com.dicoding.netflock.core.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.dicoding.netflock.core.utils.*
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


class NetflockRepositoryTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(com.dicoding.netflock.core.data.source.remote.RemoteDataSource::class.java)
    private val local = mock(com.dicoding.netflock.core.data.source.local.LocalDataSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val netflockRepository = FakeNetflockRepository(remote, local, appExecutors)

    private val movieResponse = DataDummy.generateRemoteDummyMovies()
    private val movieId = movieResponse[0].id
    private val showResponse = DataDummy.generateRemoteDummyShows()
    private val showId = showResponse[0].id

    @Test
    fun getAllMovies() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, com.dicoding.netflock.core.data.source.local.entity.MovieEntity>
        `when`(local.getAllMovies()).thenReturn(dataSourceFactory)
        netflockRepository.getAllMovies()

        val movieEntities = com.dicoding.netflock.core.data.Resource.success(PagedListUtil.mockPagedList(DataDummy.generateDummyMovie()))
        verify(local).getAllMovies()
        assertNotNull(movieEntities.data)
        assertEquals(movieResponse.size.toLong(), movieEntities.data?.size?.toLong())

    }

    @Test
    fun getAllShows() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>
        `when`(local.getAllShows()).thenReturn(dataSourceFactory)
        netflockRepository.getAllTvShows()

        val showEntities = com.dicoding.netflock.core.data.Resource.success(PagedListUtil.mockPagedList(DataDummy.generateDummyTvShow()))
        verify(local).getAllShows()
        assertNotNull(showEntities.data)
        assertEquals(showResponse.size.toLong(), showEntities.data?.size?.toLong())
    }

    @Test
    fun getMovie() {
        val dummyMovie = MutableLiveData<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>()
        dummyMovie.value = DataDummy.generateDummyMovie()[0]
        `when`<LiveData<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>>(local.getMovie(movieId)).thenReturn(dummyMovie)

        val movieEntities = LiveDataTestUtil.getValue(netflockRepository.getMovie(movieId))
        verify(local).getMovie(movieId)
        assertNotNull(movieEntities.data)
        assertNotNull(movieEntities.data?.movieTitle)
        assertEquals(movieResponse[0].title, movieEntities.data?.movieTitle)
    }

    @Test
    fun getShow() {
        val dummyShow = MutableLiveData<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>()
        dummyShow.value = DataDummy.generateDummyTvShow()[0]
        `when`<LiveData<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>>(local.getShow(showId)).thenReturn(dummyShow)

        val showEntities = LiveDataTestUtil.getValue(netflockRepository.getTvShow(showId))
        verify(local).getShow(showId)
        assertNotNull(showEntities.data)
        assertNotNull(showEntities.data?.tvShowTitle)
        assertEquals(showResponse[0].title, showEntities.data?.tvShowTitle)
    }

    @Test
    fun getFavMovie() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, com.dicoding.netflock.core.data.source.local.entity.MovieEntity>
        `when`(local.getFavMovie()).thenReturn(dataSourceFactory)
        netflockRepository.getFavMovies()

        val movieEntities = com.dicoding.netflock.core.data.Resource.success(PagedListUtil.mockPagedList(DataDummy.generateDummyMovie()))
        verify(local).getFavMovie()
        assertNotNull(movieEntities)
        assertEquals(movieResponse.size.toLong(), movieEntities.data?.size?.toLong())
    }

    @Test
    fun getFavShow() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>
        `when`(local.getFavShows()).thenReturn(dataSourceFactory)
        netflockRepository.getFavShows()

        val showEntities = com.dicoding.netflock.core.data.Resource.success(PagedListUtil.mockPagedList(DataDummy.generateDummyTvShow()))
        verify(local).getFavShows()
        assertNotNull(showEntities)
        assertEquals(showResponse.size.toLong(), showEntities.data?.size?.toLong())
    }

    @Test
    fun setFavMovie() {
        val dummyMovie = DataDummy.generateDummyMovie()[0]
        `when`(appExecutors.diskIO()).thenReturn(TestExecutor())
        doNothing().`when`(local).setFavMovie(dummyMovie, true)
        netflockRepository.setFavMovies(dummyMovie, true)
        verify(local, times(1)).setFavMovie(dummyMovie, true)
    }

    @Test
    fun setFavShow() {
        val dummyShow = DataDummy.generateDummyTvShow()[0]
        `when`(appExecutors.diskIO()).thenReturn(TestExecutor())
        doNothing().`when`(local).setFavShow(dummyShow, true)
        netflockRepository.setFavShows(dummyShow, true)
        verify(local, times(1)).setFavShow(dummyShow, true)
    }
}