package com.zahid.mathly.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

@Database(
    entities = [EquationEntity::class, SolutionEntity::class],
    version = 2
)
abstract class EquationDatabase : RoomDatabase() {
    abstract fun equationDao(): EquationDao
    abstract fun solutionDao(): SolutionDao
    
    companion object {
        @Volatile
        private var INSTANCE: EquationDatabase? = null
        
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add new columns to solutions table
                db.execSQL("ALTER TABLE solutions ADD COLUMN originalProblem TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE solutions ADD COLUMN type TEXT NOT NULL DEFAULT 'EQUATION'")
            }
        }
        
        fun getDatabase(context: Context): EquationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EquationDatabase::class.java,
                    "equation_database"
                )
                .addMigrations(MIGRATION_1_2)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 