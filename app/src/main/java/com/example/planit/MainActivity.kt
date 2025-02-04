package com.example.planit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
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

        val intent = Intent(this, AddTaskActivity::class.java)
        addTaskButton.setOnClickListener { startActivityForResult(intent, 1) }
        addIconButton.setOnClickListener { startActivityForResult(intent, 1) }

        val menuButton = findViewById<ImageButton>(R.id.buttonMenu)
        val menuOptions = findViewById<LinearLayout>(R.id.menuOptions)
        menuButton.setOnClickListener {
            if (menuOptions.visibility == View.GONE) {
                menuOptions.visibility = View.VISIBLE
            } else {
                menuOptions.visibility = View.GONE
            }
        }

        val mainLayout = findViewById<ConstraintLayout>(R.id.main)
        mainLayout.setOnClickListener {
            if (menuOptions.visibility == View.VISIBLE) {
                menuOptions.visibility = View.GONE
            }
        }

        val calendarButton = findViewById<ImageButton>(R.id.buttonCalendar)
        calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val taskName = data?.getStringExtra("TASK_NAME")
            taskName?.let { addTaskToList(it) }
        }
    }

    private fun addTaskToList(taskName: String) {
        val taskView = layoutInflater.inflate(R.layout.task_item, taskContainer, false)
        val taskText = taskView.findViewById<TextView>(R.id.taskText)
        val checkBox = taskView.findViewById<CheckBox>(R.id.taskCheckBox)
        val reviewLayout = taskView.findViewById<LinearLayout>(R.id.reviewLayout)
        val reviewInput = taskView.findViewById<EditText>(R.id.reviewInput)
        val saveReviewButton = taskView.findViewById<Button>(R.id.buttonSaveReview)

        taskText.text = taskName
        reviewLayout.visibility = View.GONE

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            reviewLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        saveReviewButton.setOnClickListener {
            if (reviewInput.isEnabled) {
                Toast.makeText(this, "리뷰 저장 완료", Toast.LENGTH_SHORT).show()
                reviewInput.isEnabled = false
                saveReviewButton.text = "리뷰 수정하기"
            } else {
                reviewInput.isEnabled = true
                reviewInput.requestFocus()
                saveReviewButton.text = "리뷰 저장하기"
            }
        }

        // 🖱 할 일 항목을 길게 누르면 삭제 확인 창 표시
        taskView.setOnLongClickListener {
            showDeleteConfirmationDialog(taskView)
            true
        }

        taskContainer.addView(taskView)
    }

    private fun showDeleteConfirmationDialog(taskView: View) {
        AlertDialog.Builder(this)
            .setTitle("삭제 확인")
            .setMessage("이 항목을 삭제하시겠습니까?")
            .setPositiveButton("확인") { _, _ ->
                taskContainer.removeView(taskView)
            }
            .setNegativeButton("취소", null)
            .show()
    }
}
