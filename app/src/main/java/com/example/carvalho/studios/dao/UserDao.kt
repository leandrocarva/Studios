package com.example.carvalho.studios.dao

import android.arch.persistence.room.*
import com.example.carvalho.studios.model.UserPers

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: UserPers)

    @Query("SELECT * FROM UserPers")
    fun getUser(): UserPers

    @Update
    fun updateUser(user: UserPers)

    @Query("DELETE FROM UserPers")
    fun deleteUser()
}