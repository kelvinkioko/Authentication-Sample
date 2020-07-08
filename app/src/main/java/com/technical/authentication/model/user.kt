package com.technical.authentication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class LoginResult(val success:Boolean,val data:Data,val message:String)
data class Data(val token:String,val user:User)
@Entity(tableName = "user")
data class User(@PrimaryKey(autoGenerate = true)
                val id:Int,
                val phone_id:String,
                val name:String,
                val email:String)

data class LoginBody(var email: String,var password:String)