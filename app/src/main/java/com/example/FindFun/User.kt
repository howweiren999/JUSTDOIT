package com.example.FindFun
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="user")
data class User(@PrimaryKey val userName: String,
                val name: String,
                val email: String,
                val contact: Int,
                val password: String){
}