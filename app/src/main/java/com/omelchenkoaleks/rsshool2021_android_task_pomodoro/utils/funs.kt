package com.omelchenkoaleks.rsshool2021_android_task_pomodoro.utils

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
