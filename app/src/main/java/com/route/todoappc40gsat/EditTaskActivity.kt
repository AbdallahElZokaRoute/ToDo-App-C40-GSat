package com.route.todoappc40gsat

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.route.todoappc40gsat.database.TaskDatabase
import com.route.todoappc40gsat.database.model.Task
import com.route.todoappc40gsat.databinding.ActivityEditTaskBinding
import com.route.todoappc40gsat.utilts.Constants
import java.text.SimpleDateFormat
import java.util.Calendar

class EditTaskActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditTaskBinding
    lateinit var intentTask: Task
    lateinit var newTask: Task
    var dateCalender = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intentTask = IntentCompat.getParcelableExtra(intent,Constants.TASK_KEY,Task::class.java) as Task
        initView()
        setUpToolber()
        initNewTask()
        onSelectDateTextView()
        onSaveClick()
    }

    private fun onSaveClick() {
        binding.content.btnSave.setOnClickListener{
            updetTask()
            finish()
        }
    }

    private fun updetTask() {
        if(!isFieldsValid()){
            return
        }

        newTask.apply {
            title = binding.content.title.text.toString()
            description = binding.content.description.text.toString()
        }
        TaskDatabase.getInstance(this).getTaskDao().updateTask(newTask)
    }

    private fun isFieldsValid():Boolean {
       var isValid = true
        if(binding.content.title.text.isNullOrBlank()){
            isValid = false
            binding.content.layoutInputTitle.error = "please enter title"
        }else{
            isValid = true
            binding.content.layoutInputTitle.error = ""
        }

        if(binding.content.date.text.isNullOrBlank()){
            isValid = false
            binding.content.selectDateText.error = "please enter title"
        }else{
            isValid = true
            binding.content.selectDateText.error = ""
        }

        return isValid
    }

    private fun onSelectDateTextView() {
        binding.content.selectDateText.setOnClickListener{
            val dialog = DatePickerDialog(this)
            dialog.setOnDateSetListener { datePicker, year, month, day ->
                binding.content.date.text = "$year/$month/$day"
                dateCalender.clearTime()
                dateCalender.set(Calendar.YEAR,year)
                dateCalender.set(Calendar.MONTH,month)
                dateCalender.set(Calendar.DAY_OF_MONTH,day)
                newTask.date = dateCalender.time
            }
            dialog.show()
        }
    }

    private fun initNewTask() {
        newTask = intentTask.copy()
    }

    private fun setUpToolber() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener{
            finish()
        }
    }

    private fun initView() {
        binding.content.title.setText(intentTask.title)
        binding.content.description.setText( intentTask.description)
//        val dateCalender = Calendar.getInstance()
//        dateCalender.time
            val dateFormattedAsString = SimpleDateFormat("yyyy/MM/dd").format(intentTask.date)
        binding.content.date.text = dateFormattedAsString

    }
}