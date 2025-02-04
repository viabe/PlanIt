package com.example.planit.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.planit.data.TodoData

class TodoRepository(context: Context) {
    private val dbHelper = TodoDatabaseHelper(context)

    // ✅ 할 일 추가
    fun insertTodo(date: String, content: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("date", date)
            put("content", content)
        }
        return db.insert("todos", null, values).also {
            db.close()
        }
    }

    // ✅ 모든 할 일 조회
    fun getAllTodos(): List<TodoData> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM todos ORDER BY date DESC", null)
        val todos = mutableListOf<TodoData>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
            todos.add(
                TodoData(
                    id,
                    date,
                    content
                )
            )
        }
        cursor.close()
        db.close()
        return todos
    }

    // ✅ 할 일 수정
    fun updateTodo(id: Int, date: String, content: String): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("date", date)
            put("content", content)
        }
        return db.update("todos", values, "id = ?", arrayOf(id.toString())).also {
            db.close()
        }
    }

    // ✅ 할 일 삭제
    fun deleteTodo(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete("todos", "id = ?", arrayOf(id.toString())).also {
            db.close()
        }
    }
}