package com.sprint.gina.timerfuns2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// TIMERS
// multiple ways to set up "timers" in android
// using java.util.Timer (not recommended in Android)
// using android.os.Handler (recommended for non-precised timing tasks)
// using an AlarmManager or similar (for more precise timing tasks)

// we will use a Handler to schedule a Runnable
// the Runnable interface has one method run()
// we will put our "tick" logic in run()
// we will schedule it to run with a 1000ms delay

// use a Handler to run code "later"
// 1. "later" could be immediately handler.post(Runnable)
// 2. "later" could be after a delay handler.postDelayed(Runnable, int milliSeconds)

public class MainActivity extends AppCompatActivity {
    Handler handler = null; // null represents timer is not running (e.g. running = false)
    int seconds = 0; // number of seconds that have elapsed

    Button startButton;
    Button pauseButton;
    Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up our Runnable first
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // set up "tick" logic
                // 1. update the seconds textview
                updateSeconds(seconds + 1);
                // 2. schedule the Runnable to run again
                handler.postDelayed(this, 1000); // causes drift over time
            }
        };

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler == null) {
                    // the "timer" is not running
                    handler = new Handler(Looper.getMainLooper()); // parameterless constructor is deprecated
                    handler.postDelayed(runnable, 1000);
                    startButton.setEnabled(false);
                    pauseButton.setEnabled(true);
                }
            }
        });

        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setEnabled(false);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer(runnable);
            }
        });

        // task: finish the app
        // 1. add the reset logic (set seconds to 0 and if the "timer" is running, stop it)
        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. set seconds 0
                updateSeconds(0);
                // 2. stop the timer
                stopTimer(runnable);
            }
        });
        // 2. set the buttons up to appropriately enabled/disabled
    }

    private void stopTimer(Runnable runnable) {
        // to stop the timer, we need to remove the Runnable from the
        // handler... effectively taking it out of the handler's schedule queue
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler = null; // e.g. running = false
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
        }
    }

    private void updateSeconds(int newSeconds) {
        seconds = newSeconds;
        TextView textView = findViewById(R.id.secondsTextView);
        textView.setText("" + seconds);
    }
}