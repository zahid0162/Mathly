package com.zahid.mathly.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [EquationEntity::class, SolutionEntity::class],
    version = 1
)
abstract class EquationDatabase : RoomDatabase() {
    abstract fun equationDao(): EquationDao
    abstract fun solutionDao(): SolutionDao
    
    companion object {
        @Volatile
        private var INSTANCE: EquationDatabase? = null
        
        fun getDatabase(context: Context): EquationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EquationDatabase::class.java,
                    "equation_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 