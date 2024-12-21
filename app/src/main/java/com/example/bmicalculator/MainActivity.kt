package com.example.bmicalculator

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find views
        val weightSlider = findViewById<Slider>(R.id.weightSlider)
        val heightSlider = findViewById<Slider>(R.id.heightSlider)
        val calcButton = findViewById<AppCompatButton>(R.id.calcButton)
        val answer = findViewById<TextView>(R.id.answer)
        val bmiCategory = findViewById<TextView>(R.id.bmiCategory)
        val bmiProgressBar = findViewById<ProgressBar>(R.id.bmiProgressBar)
        val weightUnitSwitch = findViewById<SwitchMaterial>(R.id.weightUnitSwitch)
        val heightUnitSwitch = findViewById<SwitchMaterial>(R.id.heightUnitSwitch)

        calcButton.setOnClickListener {
            // Get user input
            var weight = weightSlider.value
            var height = heightSlider.value

            // Convert to kilograms and meters regardless of the units chosen by the user
            if (weightUnitSwitch.isChecked) {
                // Convert weight from lbs to kg if the switch is on
                weight = weight * 0.453592f
            }

            if (heightUnitSwitch.isChecked) {
                // Convert height from inches to meters if the switch is on
                height = height * 0.0254f
            } else {
                // Convert height from cm to meters if the switch is off
                height = height / 100f
            }

            // BMI Calculation
            val bmi = weight / (height * height)

            // Display BMI result
            answer.text = String.format("%.2f", bmi)

            // Determine BMI category and update progress bar
            val category = when {
                bmi < 18.5 -> {
                    // Underweight
                    bmiProgressBar.progressTintList = ContextCompat.getColorStateList(this, R.color.colorBlueDark)
                    bmiProgressBar.progress = (bmi / 18.5 * 100).toInt().coerceIn(0, 100)
                    "Underweight"
                }
                bmi in 18.5..24.9 -> {
                    // Normal weight
                    bmiProgressBar.progressTintList = ContextCompat.getColorStateList(this, R.color.colorGreenDark)
                    bmiProgressBar.progress = ((bmi - 18.5) / 6.4 * 100).toInt().coerceIn(0, 100)
                    "Normal weight"
                }
                bmi in 25.0..29.9 -> {
                    // Overweight
                    bmiProgressBar.progressTintList = ContextCompat.getColorStateList(this, R.color.colorYellow)
                    bmiProgressBar.progress = ((bmi - 25.0) / 5.0 * 100).toInt().coerceIn(0, 100)
                    "Overweight"
                }
                bmi in 30.0..39.9 -> {
                    // Obesity
                    bmiProgressBar.progressTintList = ContextCompat.getColorStateList(this, R.color.colorRed)
                    bmiProgressBar.progress = ((bmi - 30.0) / 10.0 * 100).toInt().coerceIn(0, 100)
                    "Obesity"
                }
                else -> {
                    // Extreme obesity
                    bmiProgressBar.progressTintList = ContextCompat.getColorStateList(this, R.color.colorPurple)
                    bmiProgressBar.progress = 100 // Max progress for extreme obesity
                    "Extreme obesity"
                }
            }
            bmiCategory.text = "Category: $category"

            // Update progress bar (normalize BMI display)
            bmiProgressBar.progress = bmi.toInt().coerceIn(0, 40)
        }
    }
}