package com.omelchenkoaleks.rsshool2021_android_task_pomodoro

interface TimerListener {

    fun start(id: Int)

    fun stop(id: Int, currentMs: Long)

    fun delete(id: Int)

}