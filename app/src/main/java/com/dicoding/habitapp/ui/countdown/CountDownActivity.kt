package com.dicoding.habitapp.ui.countdown

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.HABIT
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.NOTIFICATION_CHANNEL_ID
import com.dicoding.habitapp.utils.NOTIF_UNIQUE_WORK

class CountDownActivity : AppCompatActivity() {

    private lateinit var txtCountdown: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: TextView
    val wm = WorkManager.getInstance(this)
    private lateinit var oneTimeWork: OneTimeWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"

        val habit = intent.getParcelableExtra<Habit>(HABIT) as Habit
        txtCountdown = findViewById(R.id.tv_count_down)
        btnStart = findViewById(R.id.btn_start)
        btnStop = findViewById(R.id.btn_stop)

        findViewById<TextView>(R.id.tv_count_down_title).text = habit.title


        //TODO 10 : Set initial time and observe current time. Update button state when countdown is finished
        val viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)
        viewModel.setInitialTime(habit.minutesFocus)
        viewModel.currentTimeString.observe(this) {
            txtCountdown.text = it
        }
        viewModel.currentTimeString.observe(this){
            txtCountdown.text = it
        }
        val data = Data.Builder()
            .putInt(HABIT_ID, habit.id)
            .putString(NOTIFICATION_CHANNEL_ID, habit.title)
            .build()
        oneTimeWork = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInputData(data)
            .build()
        viewModel.setInitialTime(habit.minutesFocus)
        viewModel.eventCountDownFinish.observe(this) {
            updateButtonState(false)
            wm.enqueue(oneTimeWork)
        }

        //TODO 13 : Start and cancel One Time Request WorkManager to notify when time is up.
        findViewById<Button>(R.id.btn_start).setOnClickListener {
            viewModel.startTimer()
            updateButtonState(true)
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            viewModel.resetTimer()
            wm.cancelWorkById(oneTimeWork.id)
            updateButtonState(false)
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }
}