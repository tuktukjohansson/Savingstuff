package com.example.savingstuff

import androidx.room.*

@Dao
interface NameDao {

    @Insert
    fun insert(name: Myname)

    @Delete
    fun delete(name: Myname)

    @Query("SELECT * FROM name_table")
    fun getAll() : List<Myname>
}