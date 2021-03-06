package com.omelchenkoaleks.rsshool2021_android_task_pomodoro.rv

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.omelchenkoaleks.rsshool2021_android_task_pomodoro.R
import com.omelchenkoaleks.rsshool2021_android_task_pomodoro.Timer
import com.omelchenkoaleks.rsshool2021_android_task_pomodoro.TimerListener
import com.omelchenkoaleks.rsshool2021_android_task_pomodoro.databinding.TimerItemBinding
import com.omelchenkoaleks.rsshool2021_android_task_pomodoro.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewHolder(

    private val context: Context,
    private val binding: TimerItemBinding,
    private val listener: TimerListener,
    private val resources: Resources

) : RecyclerView.ViewHolder(binding.root) {

    private var countDownTimer: CountDownTimer? = null
    private var current = 0L

    fun bind(timer: Timer) {
        binding.timerTv.text = timer.currentMs.displayTime()

        if (timer.isStarted) {
            startTimer(timer)
            startCustomView(timer)
        } else {
            stopTimer(timer)
        }

        initButtonsListeners(timer)
    }

    private fun Long.displayTime(): String {
        if (this <= 0L) {
            return START_TIME
        }
        val h = this / 1000 / 3600
        val m = this / 1000 % 3600 / 60
        val s = this / 1000 % 60

        return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}"
    }

    private fun displaySlot(count: Long): String {
        return if (count / 10L > 0) {
            "$count"
        } else {
            "0$count"
        }
    }

    private fun initButtonsListeners(timer: Timer) {

        binding.startPauseButton.setOnClickListener {
            if (timer.isStarted) {
                listener.stop(timer.id, timer.currentMs)
            } else {
                listener.start(timer.id)
            }
        }

        binding.deleteButton.setOnClickListener { listener.delete(timer.id) }

    }

    private fun startTimer(timer: Timer) {
        binding.startPauseButton.text = resources.getString(R.string.stop)

        countDownTimer?.cancel()
        countDownTimer = getCountDownTimer(timer)
        countDownTimer?.start()

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
    }

    private fun startCustomView(timer: Timer) {

        binding.customProgressView.setPeriod(timer.currentMs)

        GlobalScope.launch {
            while (current < timer.currentMs * REPEAT) {
                current += INTERVAL
                binding.customProgressView.setCurrent(current)
                delay(INTERVAL)
            }
        }
    }

    private fun stopCustomView(saveCurrent: Long) {
        binding.customProgressView.setPeriod(saveCurrent)
    }

    private fun stopTimer(timer: Timer) {
        binding.startPauseButton.text = resources.getString(R.string.start)
        val saveCurrent = current // ???
        stopCustomView(saveCurrent) // ???

        countDownTimer?.cancel()

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun getCountDownTimer(timer: Timer): CountDownTimer {

        return object : CountDownTimer(timer.currentMs, UNIT_TEN_MS) {
            val interval = UNIT_TEN_MS

            override fun onTick(millisUntilFinished: Long) {
                timer.currentMs -= interval
                binding.timerTv.text = timer.currentMs.displayTime()
            }

            override fun onFinish() {
                binding.timerTv.text = timer.currentMs.displayTime()
                showToast(context, "timer ???${timer.id + 1} finished")
                binding.blinkingIndicator.isInvisible = true
                (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
            }
        }
    }

    private companion object {

        private const val START_TIME = "00:00:00"
        private const val UNIT_TEN_MS = 1000L
        private const val REPEAT = 100 // 10 times
        private const val INTERVAL = 100L
    }
}