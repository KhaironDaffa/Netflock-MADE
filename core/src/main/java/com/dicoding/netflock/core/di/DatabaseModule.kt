package com.dicoding.netflock.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): com.dicoding.netflock.core.data.source.local.room.NetflockDatabase = Room.databaseBuilder(
        context,
        com.dicoding.netflock.core.data.source.local.room.NetflockDatabase::class.java, "Netflock.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideNetflockDao(database: com.dicoding.netflock.core.data.source.local.room.NetflockDatabase): com.dicoding.netflock.core.data.source.local.room.NetflockDao = database.netflockDao()
}