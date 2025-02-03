package com.example.planit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class AddTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addtask)

        // 할 일 입력 필드와 버튼 참조
        val editTextTask = findViewById<EditText>(R.id.editTextTask)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
        val buttonClose = findViewById<ImageButton>(R.id.buttonClose)

        // 등록하기 버튼 클릭 이벤트
        buttonSubmit.setOnClickListener {
            val taskName = editTextTask.text.toString() // 입력된 할 일 가져오기

            if (taskName.isNotEmpty()) {
                val resultIntent = Intent().apply {
                    putExtra("TASK_NAME", taskName) // 입력된 할 일을 인텐트에 담기
                }
                setResult(Activity.RESULT_OK, resultIntent) // 결과 설정
                finish() // 메인 화면으로 돌아가기
            }
        }

        // 닫기 버튼 클릭 시 메인 화면으로 돌아가기
        buttonClose.setOnClickListener {
            finish() // 별도의 작업 없이 화면 종료
        }

        // 달력 버튼 클릭 시 CalendarActivity로 이동
        val calendarButton = findViewById<ImageButton>(R.id.buttonCalendar)
        calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
    }
}
