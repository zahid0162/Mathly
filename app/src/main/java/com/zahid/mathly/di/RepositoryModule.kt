package com.zahid.mathly.di

import com.zahid.mathly.data.repository.EquationRepositoryImpl
import com.zahid.mathly.domain.repository.EquationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    abstract fun bindEquationRepository(
        equationRepositoryImpl: EquationRepositoryImpl
    ): EquationRepository
} 