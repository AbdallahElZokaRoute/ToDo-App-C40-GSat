package com.route.todoappc40gsat.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.helper.widget.Carousel.Adapter
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.WeekDayBinder
import com.kizitonwose.calendar.view.WeekHeaderFooterBinder
import com.kizitonwose.calendar.view.WeekScrollListener
import com.route.todoappc40gsat.CalendarDayWeekContainer
import com.route.todoappc40gsat.EditTaskActivity
import com.route.todoappc40gsat.R
import com.route.todoappc40gsat.adapters.TasksAdapter
import com.route.todoappc40gsat.clearTime
import com.route.todoappc40gsat.database.TaskDatabase
import com.route.todoappc40gsat.database.model.Task
import com.route.todoappc40gsat.databinding.FragmentTodoListBinding
import com.route.todoappc40gsat.utilts.Constants
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

class TodoListFragment : Fragment() {
    lateinit var binding: FragmentTodoListBinding
    lateinit var adapter: TasksAdapter
    lateinit var calendar: Calendar
    var selectedDate: WeekDay? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = Calendar.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.customCalendarView.weekScrollListener = object : WeekScrollListener {
                override fun invoke(p1: Week) {
                    binding.weekMonthYearText.text = "${p1.days[0].date.month}"
                }

            }
            binding.customCalendarView.dayBinder =
                object : WeekDayBinder<CalendarDayWeekContainer> {
                    override fun bind(container: CalendarDayWeekContainer, data: WeekDay) {
                        container.dayWeekTextView.text =
                            data.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        val black = ResourcesCompat.getColor(
                            resources, R.color.black, null
                        )
                        if (selectedDate == data) {

                            val selectedColor = ResourcesCompat.getColor(
                                resources, R.color.blue, null
                            )
                            container.dayWeekTextView.setTextColor(
                                selectedColor
                            )
                            container.dayMonthTextView.setTextColor(selectedColor)
                        } else {
                            container.dayWeekTextView.setTextColor(black)
                            container.dayMonthTextView.setTextColor(black)
                        }
                        container.dayMonthTextView.text = "${data.date.dayOfMonth}"
                        container.itemCalendarDayView.setOnClickListener {

                            selectedDate = data
                            binding.customCalendarView.notifyWeekChanged(data)

                            val dayOfMonth = data.date.dayOfMonth
                            val month = data.date.month.value - 1   // Calendar -> 0
                            //  1
                            Log.e("TAG", "bind: MONTH = $month")
                            Log.e(
                                "TAG",
                                "bind: MONTH in Calendar = ${calendar.get(Calendar.MONTH)}"
                            )

                            val year = data.date.year
                            //  6
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            // 8
                            calendar.set(Calendar.MONTH, month)
                            calendar.set(Calendar.YEAR, year)
                            calendar.clearTime()
                            val filteredList = TaskDatabase.getInstance(requireContext())
                                .getTaskDao()
                                .getTasksByDate(calendar.time)
                            adapter.updateData(filteredList)
                        }
                    }

                    override fun create(view: View): CalendarDayWeekContainer {
                        return CalendarDayWeekContainer(view)
                    }
                }
            val currentDate = LocalDate.now()
            val currentMonth = YearMonth.now()
            val startDate = currentMonth.minusMonths(100).atStartOfMonth() // Adjust as needed
            val endDate = currentMonth.plusMonths(100).atEndOfMonth() // Adjust as needed
            val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
            binding.customCalendarView.setup(startDate, endDate, firstDayOfWeek)
            binding.customCalendarView.scrollToWeek(currentDate)
        }
        getAllTasksFromDataBase()

    }

    fun getAllTasksFromDataBase() {
        val tasks = TaskDatabase.getInstance(requireContext()).getTaskDao().getAllTasks()
        adapter = TasksAdapter(tasks)
        binding.tasksRecyclerView.adapter = adapter

        adapter.onTaskClickListener =
            TasksAdapter.OnItemClickListener { position, task ->
                val intent = Intent(requireContext(),EditTaskActivity::class.java)
                intent.putExtra(Constants.TASK_KEY,task)
                startActivity(intent)
            }
    }
}
