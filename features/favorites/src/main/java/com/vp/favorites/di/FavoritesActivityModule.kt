package com.vp.favorites.di

import com.vp.favorites.FavoriteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoritesActivityModule {
    @ContributesAndroidInjector(
        modules = [
            FavoritesDatabaseModule::class,
            FavoritesRepositoryModule::class,
            FavoritesViewModelsModule::class,
            FavoritesRepositoryModule::class,
            FavoritesFragmentModule::class
        ]
    )
    abstract fun bindFavoritesActivity(): FavoriteActivity
}