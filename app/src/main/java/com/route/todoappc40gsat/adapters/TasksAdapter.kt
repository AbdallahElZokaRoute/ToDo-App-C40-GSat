package com.route.todoappc40gsat.adapters

import android.annotation.SuppressLint
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.route.todoappc40gsat.R
import com.route.todoappc40gsat.database.model.Task
import com.route.todoappc40gsat.databinding.ItemTaskBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class TasksAdapter(var tasksList: List<Task>) : Adapter<TasksAdapter.TaskViewHolder>() {

    class TaskViewHolder(val binding: ItemTaskBinding) : ViewHolder(binding.root) {

       @SuppressLint("ResourceAsColor")
       fun changeTaskStatus(isDone: Boolean){

           if(isDone){
               binding.dividerStatus.setBackgroundColor(R.color.green)
               binding.btnDone.setBackgroundColor(R.color.green)
           }else{
               val blue = ContextCompat.getColor(itemView.context, R.color.blue)
               binding.taskDate.setTextColor(blue)
               binding.dividerStatus.setBackgroundColor(R.color.blue)
               binding.btnDone.setBackgroundColor(R.color.blue)
           }

       }

        @SuppressLint("SimpleDateFormat")
        fun bind(task: Task) {
            binding.taskTitle.text = task.title
            val dateFormattedAsString = SimpleDateFormat("yyyy/MM/dd").format(task.date!!)
            binding.taskDate.text = dateFormattedAsString
        }
    }
    // 2 Fragments Communication :-
//         1- Callbacks
//         2- Shared View Model

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)


        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    fun updateData(tasks: List<Task>) {
        tasksList = tasks
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasksList.get(position)
        holder.bind(task)

        onDoneBtnClickListaner?.let{
            holder.binding.btnDone.setOnClickListener{
                onDoneBtnClickListaner.onClick(position,task)
            }
        }
        onTaskClickListener?.let{
            holder.binding.dragItem.setOnClickListener{
                onTaskClickListener!!.onClick(position,task)

            }
        }
    }
    val onDoneBtnClickListaner: OnItemClickListener? = null
    var onTaskClickListener: OnItemClickListener? = null

    fun interface OnItemClickListener{
        fun onClick(position: Int,task: Task)
    }



}