package com.vp.favorites.di

import com.vp.favorites.repository.FavoritesRepository
import com.vp.favorites.repository.FavoritesRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class FavoritesRepositoryModule {
    @Binds
    abstract fun bindsDetailRepository(favoritesRepository: FavoritesRepositoryImpl): FavoritesRepository
}