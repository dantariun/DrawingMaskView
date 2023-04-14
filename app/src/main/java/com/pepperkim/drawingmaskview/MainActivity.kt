package com.pepperkim.drawingmaskview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mask:DrawingMaskView = findViewById<DrawingMaskView>(R.id.maskView)

        val btn = findViewById<Button>(R.id.buttonTest)
        btn.setOnClickListener {
            mask.changePorterDuffXfermode(DrawingMaskView.ShadowModes.DestinationIn)
        }
    }
}