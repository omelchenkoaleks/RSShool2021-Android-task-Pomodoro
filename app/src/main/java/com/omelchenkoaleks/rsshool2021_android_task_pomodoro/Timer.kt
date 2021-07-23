package com.omelchenkoaleks.rsshool2021_android_task_pomodoro

data class Timer(
    val id: Int,
    var currentMs: Long,
    var isStarted: Boolean
)