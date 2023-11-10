package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var rainbowSpinnerView: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rainbowSpinnerView = findViewById(R.id.rainbow_spinner_view)

        val spinButton = findViewById<Button>(R.id.Spin)
        val resetButton = findViewById<Button>(R.id.Reset)

        spinButton.setOnClickListener {
            rainbowSpinnerView.startSpinning()
        }
        resetButton.setOnClickListener {
            rainbowSpinnerView.reset()
        }
    }
}



