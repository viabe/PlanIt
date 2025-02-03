package com.example.planit

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.planit.databinding.CalendarBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {





    
    //달력 관련 코드, 건들지 말것
    private lateinit var binding: CalendarBinding // ViewBinding 변수 초기화
    private val calendar = Calendar.getInstance() // 현재 날짜와 시간 정보 초기화
    private val scheduleData: MutableMap<String, List<String>> = mutableMapOf() // 일정 데이터를 저장할 맵

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CalendarBinding.inflate(layoutInflater) // 뷰 바인딩 초기화
        setContentView(binding.root) // 레이아웃 설정

        setupCalendar() // 달력 초기화
        setupMonthSelector() // 월 선택 슬라이더 초기화
        fetchScheduleData() // 일정 데이터 가져오기
    }

    private fun setupCalendar() {
        val sdf = SimpleDateFormat("yyyy년 MM월", Locale.getDefault())
        binding.textViewMonth.text = sdf.format(calendar.time)

        binding.gridCalendar.removeAllViews()  // 기존 날짜 제거

        // 2025년 기준으로 달력 초기화
        val tempCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2025)
            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, 1)
        }

        // 1일의 요일 (1 = 일요일, 7 = 토요일)
        val firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK)

        // 빈 칸 추가 (1일의 요일 이전 칸에 빈 셀 추가)
        for (i in 1 until firstDayOfWeek) {
            val emptyView = TextView(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)  // 행(row)도 균등 분배
                }
            }
            binding.gridCalendar.addView(emptyView)
        }

        // 날짜 추가
        val maxDay = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (day in 1..maxDay) {
            val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tempCalendar.apply {
                set(Calendar.DAY_OF_MONTH, day)
            }.time)

            val dayView = TextView(this).apply {
                text = day.toString()
                textSize = 16f
                gravity = Gravity.CENTER
                setPadding(8, 8, 8, 8)
                setBackgroundResource(
                    if (scheduleData.containsKey(dateKey)) android.R.color.holo_blue_light
                    else android.R.color.transparent
                )
                setTextColor(resources.getColor(android.R.color.white))
                setOnClickListener { onDateSelected(day) }
            }

            // GridLayout에 날짜 추가 (열과 행을 모두 균등 분배)
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setGravity(Gravity.FILL)
            }

            binding.gridCalendar.addView(dayView, params)
        }
    }

    private fun setupMonthSelector() {
        binding.monthSelector.removeAllViews() // 중복 방지를 위해 초기화

        val months = listOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
        months.forEachIndexed { index, month ->
            val monthView = TextView(this).apply {
                text = month // 월 표시
                textSize = 16f
                gravity = Gravity.CENTER
                setPadding(16, 16, 16, 16)
                setTextColor(resources.getColor(android.R.color.white)) // 글자색 흰색

                setOnClickListener {
                    calendar.set(Calendar.MONTH, index) // 선택한 월로 달력 업데이트
                    calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR)) // 올해로 설정
                    setupCalendar()
                    highlightSelectedMonth(index) // 선택한 월 강조 표시
                }
            }
            binding.monthSelector.addView(monthView) // 슬라이딩 바에 월 추가
        }
    }

    // 선택한 월을 강조하는 함수
    private fun highlightSelectedMonth(selectedIndex: Int) {
        for (i in 0 until binding.monthSelector.childCount) {
            val monthView = binding.monthSelector.getChildAt(i) as TextView
            if (i == selectedIndex) {
                monthView.setTextColor(resources.getColor(android.R.color.black)) // 선택한 달 글자색 검정
                monthView.setBackgroundResource(android.R.color.white) // 선택한 달 배경 흰색
            } else {
                monthView.setTextColor(resources.getColor(android.R.color.white)) // 비선택 달 글자색 흰색
                monthView.setBackgroundResource(android.R.color.transparent) // 비선택 달 배경 투명
            }
        }
    }

    private fun fetchScheduleData() {
        // 예제 일정 데이터 추가 (실제 데이터는 API 호출 또는 Intent로 받을 수 있음)
        scheduleData["2025-01-10"] = listOf("회의", "운동")
        scheduleData["2025-01-20"] = listOf("프로젝트 마감일")
    }

    private fun onDateSelected(day: Int) {
        val sdf = SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN) // Locale.KOREAN으로 변경
        val selectedCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2025)
            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, day)
        }

        val formattedDate = sdf.format(selectedCalendar.time)
        binding.textViewSelectedDate.text = formattedDate // 선택한 날짜와 한글 요일 표시

        val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(selectedCalendar.time)

        // 일정이 있는 경우
        if (scheduleData.containsKey(dateKey)) {
            binding.textViewNoEvent.visibility = View.GONE
            binding.eventContainer.removeAllViews()

            scheduleData[dateKey]?.forEach { event ->
                val eventView = TextView(this).apply {
                    text = event
                    setTextColor(resources.getColor(android.R.color.white))
                    textSize = 14f
                }
                binding.eventContainer.addView(eventView)
            }
        } else {
            binding.textViewNoEvent.visibility = View.VISIBLE
            binding.textViewNoEvent.text = "활동내역이 없습니다."
            binding.eventContainer.removeAllViews()
        }
    }
    //여기까지 달력 코드, 건들지 말것

}
