package com.example.stepcountertest

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var running = false
    private var sensorManager: SensorManager? = null
    private lateinit var stepsValue: TextView
    private lateinit var startFab: FloatingActionButton
    private lateinit var stopFab: FloatingActionButton
    private var currentSteps = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stepsValue = findViewById(R.id.stepsValue)
        startFab = findViewById(R.id.startFab)
        stopFab = findViewById(R.id.stopFab)

        startFab.setOnClickListener {
            if (!running) {
                sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
                onResume()
            }
            else {
                onPause()
            }
        }

        stopFab.setOnClickListener {
            sensorManager?.unregisterListener(this)
            stepsValue.text = "0"
            currentSteps = 0
            startFab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    override fun onResume() {
        super.onResume()
        var stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepsSensor == null) {
            if (running) {
                Toast.makeText(this, "No Step Counter Sensor!", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            running = true
            if (currentSteps != 0) {
                currentSteps--
            }
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
            startFab.setImageResource(R.drawable.ic_baseline_pause_24)
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
        startFab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        // TODO("Not yet implemented")
        if (running) {
            stepsValue.text = currentSteps.toString()
            currentSteps++
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // TODO("Not yet implemented")
    }
}