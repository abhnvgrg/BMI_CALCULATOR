package com.example.bmicalculator

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Build
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorBrown)
        }

        // Find views
        val weightSlider = findViewById<Slider>(R.id.weightSlider)
        val heightSlider = findViewById<Slider>(R.id.heightSlider)
        val calcButton = findViewById<AppCompatButton>(R.id.calcButton)
        val answer = findViewById<TextView>(R.id.answer)
        val bmiCategory = findViewById<TextView>(R.id.bmiCategory)
        val bmiProgressBar = findViewById<ProgressBar>(R.id.bmiProgressBar)
        val weightUnitSwitch = findViewById<SwitchMaterial>(R.id.weightUnitSwitch)
        val heightUnitSwitch = findViewById<SwitchMaterial>(R.id.heightUnitSwitch)
        val shareButton = findViewById<AppCompatButton>(R.id.shareButton)


        val heightslider = findViewById<Slider>(R.id.heightSlider)
        val HeightsliderValueText = findViewById<TextView>(R.id.HeightsliderValueText)

        val HeightUnitSwitch: SwitchMaterial = findViewById(R.id.heightUnitSwitch)
        heightUnitSwitch.thumbTintList = ContextCompat.getColorStateList(this, R.color.colorBrownLight)
        heightUnitSwitch.trackTintList = ContextCompat.getColorStateList(this, R.color.switch_track_color)

        val WeightUnitSwitch: SwitchMaterial = findViewById(R.id.weightUnitSwitch)
        weightUnitSwitch.thumbTintList = ContextCompat.getColorStateList(this, R.color.colorBrownLight)
        weightUnitSwitch.trackTintList = ContextCompat.getColorStateList(this, R.color.switch_track_color)
// Update TextView dynamically
        heightslider.addOnChangeListener { _, value, _ ->
            HeightsliderValueText.text = "${value.toFloat()}"
        }

        // Animate slider and update TextView
        fun animateheightSliderWithValueDisplay(targetValue: Float) {
            val animator = ValueAnimator.ofFloat(heightslider.value, targetValue)
            animator.duration = 500
            animator.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                heightslider.value = animatedValue
                HeightsliderValueText.text = "${animatedValue.toFloat()}"
            }
            animator.start()
        }

        val weightslider = findViewById<Slider>(R.id.weightSlider)
        val WeightsliderValueText = findViewById<TextView>(R.id.WeightsliderValueText)

// Update TextView dynamically
        weightslider.addOnChangeListener { _, value, _ ->
            WeightsliderValueText.text = "${value.toFloat()}"
        }

        // Animate slider and update TextView
        fun animateweightSliderWithValueDisplay(targetValue: Float) {
            val animator = ValueAnimator.ofFloat(weightslider.value, targetValue)
            animator.duration = 500
            animator.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                weightslider.value = animatedValue
                WeightsliderValueText.text = "${animatedValue.toFloat()}"
            }
            animator.start()
        }





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

            // Add click listener for the share button
            shareButton.setOnClickListener {
                // Get BMI details to share
                val bmiValue = answer.text.toString()
                val bmiCategoryText = bmiCategory.text.toString()

                // Create the share message
                val shareMessage = """
        Check out my BMI result!
        BMI: $bmiValue
        $bmiCategoryText
        Calculated using the BMI Calculator app.
        LET'S GO FIT!!
    """.trimIndent()

                // Create an Intent to share the message
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                }

                // Start the sharing activity
                startActivity(Intent.createChooser(shareIntent, "Share your BMI result via"))
            }
        }
    }
}