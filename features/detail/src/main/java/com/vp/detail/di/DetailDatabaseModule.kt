package com.vp.detail.di

import com.vp.detail.model.MovieDetail
import dagger.Module
import dagger.Provides
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration


@Module
class DetailDatabaseModule {
    @Provides
    fun provideRealmConfig(): RealmConfiguration =
        RealmConfiguration.Builder(schema = setOf(MovieDetail::class))
            .name("MovieDb.realm")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(1).build()

    @Provides
    fun provideRealmDatabase(config: RealmConfiguration): Realm = Realm.open(config)
}