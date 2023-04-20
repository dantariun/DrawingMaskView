package com.pepperkim.drawingmaskview

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.pepperkim.drawingmaskview.databinding.ActivityMainBinding
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mask:DrawingMaskView = findViewById(R.id.maskView)
        val metrics = mask.resources.displayMetrics

        binding.seekBarRadius.max = metrics.widthPixels /2
        binding.seekBarRadius.progress = mask.shapeRadius

        binding.seekBarRadius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) { mask.changeCircleRadius(progress) }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.seekBarRectW.max = metrics.widthPixels
        binding.seekBarRectW.progress = mask.shapeWidth

        binding.seekBarRectW.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) { mask.changeShapeWidth(progress) }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.seekBarRectH.max = metrics.heightPixels
        binding.seekBarRectH.progress = mask.shapeHeight

        binding.seekBarRectH.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) { mask.changeShapeHeight(progress) }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        //seekBarRectCorner
        binding.seekBarRectCorner.max = 100
        binding.seekBarRectCorner.progress = mask.rectRound

        binding.seekBarRectCorner.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) { mask.changeRectRound(progress) }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val shapes = resources.getStringArray(R.array.shapes)
        val shapesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, shapes)
        binding.spinnerShapes.adapter = shapesAdapter
        binding.spinnerShapes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when(shapes[position]){
                    "circle" -> {
                        binding.seekBarRadius.isEnabled = true

                        binding.seekBarRectW.isEnabled = false
                        binding.seekBarRectH.isEnabled = false
                        binding.seekBarRectCorner.isEnabled = false
                    }
                    "rect" -> {
                        binding.seekBarRectW.isEnabled = true
                        binding.seekBarRectH.isEnabled = true

                        binding.seekBarRadius.isEnabled = false
                        binding.seekBarRectCorner.isEnabled = false
                    }
                    "rect_round" -> {
                        binding.seekBarRectCorner.isEnabled = true

                        binding.seekBarRectW.isEnabled = true
                        binding.seekBarRectH.isEnabled = true
                        binding.seekBarRadius.isEnabled = false
                    }
                }
                mask.changeShapeType(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val aligns = resources.getStringArray(R.array.align)
        val alignsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, aligns)
        binding.spinnerAlign.adapter = alignsAdapter
        binding.spinnerAlign.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mask.changeAlign(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        //spinnerDST
        val modes = resources.getStringArray(R.array.modes)
        val modesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, modes)
        binding.spinnerDST.adapter = modesAdapter
        binding.spinnerDST.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mask.changePorterDuffXfermode(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        binding.spinnerDST.setSelection(modes.size-1)

        binding.selectedColor.setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.confirm),
                    ColorEnvelopeListener { envelope, fromUser ->
//                        setLayoutColor(envelope)
                        binding.selectedColor.setBackgroundColor(envelope.color)
                        mask.changeShadowColor(envelope.color)
                    })
                .setNegativeButton(
                    getString(R.string.cancel)
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true) // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()
        }
    }
}