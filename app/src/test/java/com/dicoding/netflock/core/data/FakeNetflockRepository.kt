package com.dicoding.netflock.core.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dicoding.netflock.core.data.source.remote.api.ApiResponse
import com.dicoding.netflock.core.data.source.remote.response.MoviesItems
import com.dicoding.netflock.core.data.source.remote.response.ShowsItems
import com.dicoding.netflock.core.utils.AppExecutors

class FakeNetflockRepository(
    private val remoteDataSource: com.dicoding.netflock.core.data.source.remote.RemoteDataSource,
    private val localDataSource: com.dicoding.netflock.core.data.source.local.LocalDataSource,
    private val appExecutors: AppExecutors) : NetflockDataSource {

    override fun getAllMovies(): LiveData<com.dicoding.netflock.core.data.Resource<PagedList<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>>> {
        return object : com.dicoding.netflock.core.data.NetworkBoundResource<PagedList<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>, List<MoviesItems>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localDataSource.getAllMovies(), config).build()
            }

            override fun shouldFetch(data: PagedList<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>?): Boolean =
                data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<MoviesItems>>> =
                remoteDataSource.getAllMovies()


            override fun saveCallResult(data: List<MoviesItems>) {
                val movieList = ArrayList<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>()
                for (response in data) {
                    val movie = com.dicoding.netflock.core.data.source.local.entity.MovieEntity(
                        response.id,
                        response.title,
                        response.posterPath,
                        response.overview,
                        response.releaseDate
                    )
                    movieList.add(movie)
                }

                localDataSource.insertMovie(movieList)
            }

        }.asLiveData()
    }

    override fun getAllTvShows(): LiveData<com.dicoding.netflock.core.data.Resource<PagedList<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>>> {

        return object : com.dicoding.netflock.core.data.NetworkBoundResource<PagedList<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>, List<ShowsItems>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localDataSource.getAllShows(), config).build()
            }

            override fun shouldFetch(data: PagedList<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>?): Boolean = data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<ShowsItems>>> = remoteDataSource.getAllTvShows()

            override fun saveCallResult(data: List<ShowsItems>) {
                val tvShowList = ArrayList<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>()
                for(response in data) {
                    val show = com.dicoding.netflock.core.data.source.local.entity.TvShowEntity(
                        response.id,
                        response.title,
                        response.posterPath,
                        response.overview,
                        response.releaseDate
                    )

                    tvShowList.add(show)
                }

                localDataSource.insertShow(tvShowList)
            }

        }.asLiveData()
    }

    override fun getMovie(movieId: Int): LiveData<com.dicoding.netflock.core.data.Resource<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>> {
        return object : com.dicoding.netflock.core.data.NetworkBoundResource<com.dicoding.netflock.core.data.source.local.entity.MovieEntity, List<MoviesItems>>(appExecutors) {
            override fun loadFromDB(): LiveData<com.dicoding.netflock.core.data.source.local.entity.MovieEntity> =
                localDataSource.getMovie(movieId)

            override fun shouldFetch(data: com.dicoding.netflock.core.data.source.local.entity.MovieEntity?): Boolean = data == null

            override fun createCall(): LiveData<ApiResponse<List<MoviesItems>>> =
                remoteDataSource.getAllMovies()

            override fun saveCallResult(data: List<MoviesItems>) {
                val movieList = ArrayList<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>()
                for (response in data) {
                    val movie = com.dicoding.netflock.core.data.source.local.entity.MovieEntity(
                        response.id,
                        response.title,
                        response.posterPath,
                        response.overview,
                        response.releaseDate,
                        false
                    )

                    movieList.add(movie)
                }

                localDataSource.insertMovie(movieList)
            }

        }.asLiveData()

    }

    override fun getTvShow(tvShowId: Int): LiveData<com.dicoding.netflock.core.data.Resource<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>> {

        return object : com.dicoding.netflock.core.data.NetworkBoundResource<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity, List<ShowsItems>>(appExecutors) {
            override fun loadFromDB(): LiveData<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity> =
                localDataSource.getShow(tvShowId)

            override fun shouldFetch(data: com.dicoding.netflock.core.data.source.local.entity.TvShowEntity?): Boolean =
                data == null

            override fun createCall(): LiveData<ApiResponse<List<ShowsItems>>> =
                remoteDataSource.getAllTvShows()

            override fun saveCallResult(data: List<ShowsItems>) {
                val showList = ArrayList<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>()
                for (response in data) {
                    val show = com.dicoding.netflock.core.data.source.local.entity.TvShowEntity(
                        response.id,
                        response.title,
                        response.posterPath,
                        response.overview,
                        response.releaseDate
                    )

                    showList.add(show)
                }

                localDataSource.insertShow(showList)
            }

        }.asLiveData()

    }

    override fun getFavMovies(): LiveData<PagedList<com.dicoding.netflock.core.data.source.local.entity.MovieEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()

        return LivePagedListBuilder(localDataSource.getFavMovie(), config).build()
    }

    override fun getFavShows(): LiveData<PagedList<com.dicoding.netflock.core.data.source.local.entity.TvShowEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()

        return LivePagedListBuilder(localDataSource.getFavShows(), config).build()
    }

    override fun setFavMovies(movieEntity: com.dicoding.netflock.core.data.source.local.entity.MovieEntity, state: Boolean) =
        appExecutors.diskIO().execute { localDataSource.setFavMovie(movieEntity, state) }

    override fun setFavShows(tvShowEntity: com.dicoding.netflock.core.data.source.local.entity.TvShowEntity, state: Boolean) =
        appExecutors.diskIO().execute { localDataSource.setFavShow(tvShowEntity, state) }
}