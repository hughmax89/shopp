package com.aguerodev.shopp.data.di

import com.aguerodev.shopp.data.repository.RepositoryImplementation
import dagger.Module
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Module
@InstallIn(Singleton::class)
abstract class RepositoryModule(
    implementation: RepositoryImplementation
)