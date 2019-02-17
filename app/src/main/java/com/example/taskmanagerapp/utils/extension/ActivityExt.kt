package com.example.taskmanagerapp.utils.extension

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.taskmanagerapp.R

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.addGradientBackground() {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
    window.setBackgroundDrawableResource(R.drawable.gradient_green_blue)
}
