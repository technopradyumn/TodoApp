package com.technopradyumn.todoapp.database

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Database(entities = [TodoEntity::class], version = 1)
abstract class TodoDatabase:RoomDatabase() {

    abstract fun todoDao(): TodoDao

}