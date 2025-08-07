package com.zahid.mathly.di

import android.content.Context
import com.zahid.mathly.data.local.EquationDao
import com.zahid.mathly.data.local.EquationDatabase
import com.zahid.mathly.data.local.SolutionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideEquationDatabase(@ApplicationContext context: Context): EquationDatabase {
        return EquationDatabase.getDatabase(context)
    }
    
    @Provides
    @Singleton
    fun provideEquationDao(database: EquationDatabase): EquationDao {
        return database.equationDao()
    }
    
    @Provides
    @Singleton
    fun provideSolutionDao(database: EquationDatabase): SolutionDao {
        return database.solutionDao()
    }
} 