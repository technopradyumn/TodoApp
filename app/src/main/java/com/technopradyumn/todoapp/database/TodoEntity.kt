package com.technopradyumn.todoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Entity("todos")
data class TodoEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo("title")
    val title: String,

    @ColumnInfo("sub_title")
    val subtitle: String,

    @ColumnInfo("done")
    val done: Boolean = false,

    @ColumnInfo("added")
    val added: Long = System.currentTimeMillis()

)

val TodoEntity.addDate: String
    get() {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.getDefault())
            .withZone(ZoneId.systemDefault())

        return formatter.format(Instant.ofEpochMilli(added))
    }