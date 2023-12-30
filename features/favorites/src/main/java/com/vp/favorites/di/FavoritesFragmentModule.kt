package com.vp.favorites.di

import com.vp.favorites.FavoritesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavoritesFragmentModule {

    @ContributesAndroidInjector
    abstract fun bindFavoritesFragment(): FavoritesFragment
}