package com.example.FindFun
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName ="ScheduleList")
data class ScheduleList(val date: String,
                        val message: String,
                        val userName: String,
                        val createdDate: String){
}