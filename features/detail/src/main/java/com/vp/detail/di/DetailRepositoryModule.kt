package com.vp.detail.di

import com.vp.detail.repository.DetailRepository
import com.vp.detail.repository.DetailRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class DetailRepositoryModule {
    @Binds
    abstract fun bindsDetailRepository(detailRepository: DetailRepositoryImpl): DetailRepository
}