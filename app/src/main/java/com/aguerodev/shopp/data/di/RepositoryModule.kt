package com.aguerodev.shopp.data.di

import com.aguerodev.shopp.data.repository.RepositoryImplementation
import com.aguerodev.shopp.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(
        repositoryImplementation: RepositoryImplementation
    ): Repository
}