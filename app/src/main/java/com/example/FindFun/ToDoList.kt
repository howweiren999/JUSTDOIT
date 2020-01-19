package com.example.FindFun

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="ToDoList")
data class ToDoList(@PrimaryKey val ToDoName: String,
                    val createdAt: String,
                    val userName: String){

}


