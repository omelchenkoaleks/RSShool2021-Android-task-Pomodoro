package com.omelchenkoaleks.rsshool2021_android_task_pomodoro.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.omelchenkoaleks.rsshool2021_android_task_pomodoro.Timer
import com.omelchenkoaleks.rsshool2021_android_task_pomodoro.TimerListener
import com.omelchenkoaleks.rsshool2021_android_task_pomodoro.databinding.TimerItemBinding

class TimerAdapter(
    private val context: Context,
    private val listener: TimerListener
) : ListAdapter<Timer, TimerViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TimerItemBinding.inflate(layoutInflater, parent, false)
        return TimerViewHolder(
            context,
            binding,
            listener,
            binding.root.context.resources
        )
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private companion object {

        private val itemComparator = object : DiffUtil.ItemCallback<Timer>() {

            override fun areItemsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.currentMs == newItem.currentMs &&
                        oldItem.isStarted == newItem.isStarted
            }
        }
    }
}