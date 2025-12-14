package com.aguerodev.shopp.data.di

import com.aguerodev.shopp.data.repository.AuthRepositoryImplementation
import com.aguerodev.shopp.domain.repository.AuthRepository
import com.aguerodev.shopp.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImplementation: AuthRepositoryImplementation
    ): AuthRepository
}