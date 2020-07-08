package com.technical.authentication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.technical.authentication.model.User
import io.reactivex.Single

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUser(): Single<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user:User)
}