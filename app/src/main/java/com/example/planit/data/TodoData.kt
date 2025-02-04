package com.example.planit.data


// 저장, 수정, 삭제
data class TodoData(
    val id: Int = 0,
    val date: String,
    val content : String,
)