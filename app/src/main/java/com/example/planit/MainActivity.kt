package com.example.planit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    private lateinit var taskContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addTaskButton = findViewById<Button>(R.id.buttonAddTask)
        val addIconButton = findViewById<ImageButton>(R.id.buttonAdd)
        taskContainer = findViewById(R.id.taskContainer)


        // 할 일 추가 버튼 이벤트
        val intent = Intent(this, AddTaskActivity::class.java)
        addTaskButton.setOnClickListener { startActivityForResult(intent, 1) }
        addIconButton.setOnClickListener { startActivityForResult(intent, 1) }

        // 메뉴 버튼 클릭 시 할 일 편집/삭제 메뉴 표시
        val menuButton = findViewById<ImageButton>(R.id.buttonMenu)
        val menuOptions = findViewById<LinearLayout>(R.id.menuOptions)
        menuButton.setOnClickListener {
            if (menuOptions.visibility == View.GONE) {
                menuOptions.visibility = View.VISIBLE
            } else {
                menuOptions.visibility = View.GONE
            }
        }

        // 메뉴 외부 클릭 시 메뉴 숨기기
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)
        mainLayout.setOnClickListener {
            if (menuOptions.visibility == View.VISIBLE) {
                menuOptions.visibility = View.GONE
            }
        }

        // 달력 버튼 클릭 시 CalendarActivity로 이동
        val calendarButton = findViewById<ImageButton>(R.id.buttonCalendar)
        calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
    }

    // AddTaskActivity로부터 결과 받아오기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val taskName = data?.getStringExtra("TASK_NAME")
            taskName?.let { addTaskToList(it) }
        }
    }

    // 할 일 목록에 추가
    private fun addTaskToList(taskName: String) {
        val taskView = layoutInflater.inflate(R.layout.task_item, taskContainer, false)
        val taskText = taskView.findViewById<TextView>(R.id.taskText)
        val checkBox = taskView.findViewById<CheckBox>(R.id.taskCheckBox)
        val reviewLayout = taskView.findViewById<LinearLayout>(R.id.reviewLayout)
        val reviewInput = taskView.findViewById<EditText>(R.id.reviewInput)
        val saveReviewButton = taskView.findViewById<Button>(R.id.buttonSaveReview)

        taskText.text = taskName
        reviewLayout.visibility = View.GONE

        // 체크박스 클릭 시 리뷰 입력 레이아웃 표시
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            reviewLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        // 리뷰 저장 & 수정 기능
        saveReviewButton.setOnClickListener {
            if (reviewInput.isEnabled) {
                // 리뷰 저장
                Toast.makeText(this, "리뷰 저장 완료", Toast.LENGTH_SHORT).show()
                reviewInput.isEnabled = false
                saveReviewButton.text = "리뷰 수정하기"
            } else {
                // 리뷰 수정 가능하도록 변경
                reviewInput.isEnabled = true
                reviewInput.requestFocus()
                saveReviewButton.text = "리뷰 저장하기"
            }
        }

        // 목록에 새로운 할 일 추가
        taskContainer.addView(taskView)
    }
}
