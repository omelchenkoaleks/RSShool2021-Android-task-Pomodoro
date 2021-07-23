package com.omelchenkoaleks.rsshool2021_android_task_pomodoro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.omelchenkoaleks.rsshool2021_android_task_pomodoro.databinding.ActivityMainBinding
import com.omelchenkoaleks.rsshool2021_android_task_pomodoro.rv.TimerAdapter

class MainActivity : AppCompatActivity(), TimerListener {

    private lateinit var binding: ActivityMainBinding

    // timers state storage
    private val timers = mutableListOf<Timer>()

    private val timerAdapter = TimerAdapter(this)
    private var nextId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timerAdapter
        }

        binding.addNewTimerButton.setOnClickListener {
            timers.add(Timer(nextId++, 0, true))
            timerAdapter.submitList(timers.toList())
        }
    }

    override fun start(id: Int) {
        changeTimer(id, null, true)
    }

    override fun stop(id: Int, currentMs: Long) {
        changeTimer(id, currentMs, false)
    }

    override fun delete(id: Int) {
        timers.remove(timers.find { it.id == id })
        timerAdapter.submitList(timers.toList())
    }

    private fun changeTimer(id: Int, currentMs: Long?, isStarted: Boolean) {

        val newTimers = mutableListOf<Timer>()

        timers.forEach {
            if (it.id == id) {
                newTimers.add(Timer(it.id, currentMs ?: it.currentMs, isStarted))
            } else {
                newTimers.add(it)
            }
        }

        timerAdapter.submitList(newTimers)
        timers.clear()
        timers.addAll(newTimers)
    }

}