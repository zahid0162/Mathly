package com.zahid.mathly.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

@Database(
    entities = [EquationEntity::class, SolutionEntity::class, GraphEntity::class, CaloriesEntity::class, BMIEntity::class],
    version = 3,

)
abstract class EquationDatabase : RoomDatabase() {
    abstract fun equationDao(): EquationDao
    abstract fun solutionDao(): SolutionDao
    abstract fun graphDao(): GraphDao
    abstract fun caloriesDao(): CaloriesDao
    abstract fun bmiDao(): BMIDao
    
    companion object {
        @Volatile
        private var INSTANCE: EquationDatabase? = null
        
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create calories table
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS calories (" +
                    "id TEXT PRIMARY KEY NOT NULL, " +
                    "foodDescription TEXT NOT NULL, " +
                    "totalCalories INTEGER NOT NULL, " +
                    "timestamp INTEGER NOT NULL, " +
                    "breakdownJson TEXT NOT NULL, " +
                    "exercisesJson TEXT NOT NULL)"
                )
            }
        }
        
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create BMI records table
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS bmi_records (" +
                    "id TEXT PRIMARY KEY NOT NULL, " +
                    "height REAL NOT NULL, " +
                    "weight REAL NOT NULL, " +
                    "bmiValue REAL NOT NULL, " +
                    "category TEXT NOT NULL, " +
                    "timestamp INTEGER NOT NULL)"
                )
            }
        }
        
        fun getDatabase(context: Context): EquationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EquationDatabase::class.java,
                    "equation_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 