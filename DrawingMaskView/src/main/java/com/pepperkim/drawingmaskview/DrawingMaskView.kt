package com.pepperkim.drawingmaskview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Paint.Align
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.properties.Delegates

@SuppressLint("Recycle")
class DrawingMaskView constructor(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var shapeType by Delegates.notNull<Int>()
    var shadowColor by Delegates.notNull<Int>()
    var shapeRadius by Delegates.notNull<Int>()
    var shapeWidth by Delegates.notNull<Int>()
    var shapeHeight by Delegates.notNull<Int>()
    var shapeAlign by Delegates.notNull<Int>()
    var rectRound by Delegates.notNull<Int>()
    var shadowMode by Delegates.notNull<Int>()

    enum class Shapes(val data:Int){
        circle(0),
        rect(1),
        rect_round(2);

        companion object {
            fun dataOf(data: Int, defaultValue: Shapes = circle) = Aligns.values().firstOrNull { it.data == data } ?: defaultValue
        }
    }

    enum class Aligns(val data:Int) {
        center(0),
        top(1),
        bottom(2),
        left(3),
        right(4),
        top_left(5),
        top_right(6),
        bottom_left(7),
        bottom_right(8);

        companion object {
            fun dataOf(data: Int, defaultValue: Aligns = center) = values().firstOrNull { it.data == data } ?: defaultValue
        }
    }

    enum class ShadowModes(val data:Int){
        Clear(0),
        DstIn(1),
        DstOut(2);

        companion object {
            fun dataOf(data: Int, defaultValue: ShadowModes = DstOut) = Aligns.values().firstOrNull { it.data == data } ?: defaultValue
        }
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DrawingMaskView)
        shapeType = a.getInt(R.styleable.DrawingMaskView_shape_type, 0)
        shadowColor = a.getColor(R.styleable.DrawingMaskView_shadow_color, Color.BLACK)
        shapeRadius = a.getInt(R.styleable.DrawingMaskView_radius_size, 100)
        shapeWidth = a.getInt(R.styleable.DrawingMaskView_shape_width, 100)
        shapeHeight = a.getInt(R.styleable.DrawingMaskView_shape_height, 100)
        shapeAlign = a.getInt(R.styleable.DrawingMaskView_align, 0)
        rectRound = a.getInt(R.styleable.DrawingMaskView_rect_round, 10)
        shadowMode = a.getInt(R.styleable.DrawingMaskView_shadow_mode, 8)
    }

    fun changeAlign(align:Int){
        shapeAlign = align
        this.invalidate()
    }

    fun changeShapeType(type:Int){
        shapeType = type
        this.invalidate()
    }

    fun changeShadowColor(color:Int){
        shadowColor = color
        this.invalidate()
    }
    fun changeCircleRadius(radius:Int){
       shapeRadius = radius
       this.invalidate()
    }

    fun changeShapeWidth(width:Int){
       shapeWidth = width
       this.invalidate()
    }

    fun changeShapeHeight(height:Int){
       shapeHeight = height
       this.invalidate()
    }

    fun changeRectRound(round:Int){
       rectRound = round
       this.invalidate()
    }

    fun changePorterDuffXfermode(mode:Int){
        shadowMode = mode
        this.invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val resultCanvas = Canvas()

        val result = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888 )
        val mask = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val canvasForMask = Canvas(mask)
        val paint1 = Paint()
        paint1.isAntiAlias = true
        paint1.setARGB(255, 255, 255, 255)

        when(shapeType){
            Shapes.rect.data ->{
                canvasForMask.drawRect(getRectAlignPoint(), paint1)
            }
            Shapes.rect_round.data ->{
                canvasForMask.drawRoundRect(getRectAlignPoint(),rectRound.toFloat(), rectRound.toFloat(), paint1)
            }
            Shapes.circle.data->{
                var circleX = 0f
                var circleY = 0f
                when(shapeAlign){
                    Aligns.center.data ->{
                        circleX = (this.width/2).toFloat()
                        circleY = (this.height/2).toFloat()
                    }
                    Aligns.top.data ->{
                        circleX = (this.width/2).toFloat()
                        circleY = (shapeRadius).toFloat()
                    }
                    Aligns.top_left.data ->{
                        circleX = (shapeRadius).toFloat()
                        circleY = (shapeRadius).toFloat()
                    }
                    Aligns.top_right.data ->{
                        circleX = (this.width).toFloat() - (shapeRadius).toFloat()
                        circleY = (shapeRadius).toFloat()
                    }
                    Aligns.left.data ->{
                        circleX = (shapeRadius).toFloat()
                        circleY = (this.height/2).toFloat()
                    }
                    Aligns.right.data ->{
                        circleX = (this.width).toFloat() - (shapeRadius).toFloat()
                        circleY = (this.height/2).toFloat()
                    }
                    Aligns.bottom.data ->{
                        circleX = (this.width/2).toFloat()
                        circleY = (this.height).toFloat() - (shapeRadius).toFloat()
                    }
                    Aligns.bottom_left.data ->{
                        circleX = (shapeRadius).toFloat()
                        circleY = (this.height).toFloat() - (shapeRadius).toFloat()
                    }
                    Aligns.bottom_right.data ->{
                        circleX = (this.width).toFloat() - (shapeRadius).toFloat()
                        circleY = (this.height).toFloat() - (shapeRadius).toFloat()
                    }
                }
                canvasForMask.drawCircle(circleX, circleY, shapeRadius.toFloat(), paint1)
            }
            else ->{
                canvasForMask.drawCircle((this.width/2).toFloat(), (this.height/2).toFloat(), shapeRadius.toFloat(), paint1)
            }
        }

        resultCanvas.setBitmap(result)
        resultCanvas.drawColor(shadowColor)
        val paint2 = Paint()
        paint2.isFilterBitmap = false

        when(shadowMode){
            ShadowModes.Clear.data ->{ paint2.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
            ShadowModes.DstIn.data ->{ paint2.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN) }
            ShadowModes.DstOut.data ->{ paint2.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT) }
        }

        resultCanvas.drawBitmap(mask, 0f, 0f, paint2)
        paint2.xfermode = null

        canvas.drawBitmap(result, 0f, 0f, null)

        super.onDraw(canvas)
    }

    private fun getRectAlignPoint():RectF{
        var top = 0f
        var left = 0f
        var right = 0f
        var bottom = 0f
        when(shapeAlign){
            Aligns.center.data ->{
                top = ((this.height/2) - (shapeHeight/2)).toFloat()
                left = (this.width/2).toFloat() - (shapeWidth/2)
                right = left + shapeWidth
                bottom = ((this.height/2) + (shapeHeight/2)).toFloat()
            }
            Aligns.top.data ->{
                left = (this.width/2).toFloat() - (shapeWidth/2)
                right = left + shapeWidth
                bottom = shapeHeight.toFloat()
            }
            Aligns.top_left.data ->{
                right = shapeWidth.toFloat()
                bottom = shapeHeight.toFloat()
            }
            Aligns.top_right.data ->{
                left = (this.width - shapeWidth).toFloat()
                right = this.width.toFloat()
                bottom = shapeHeight.toFloat()
            }
            Aligns.left.data ->{
                top = ((this.height/2) - (shapeHeight/2)).toFloat()
                right = shapeWidth.toFloat()
                bottom = ((this.height/2) + (shapeHeight/2)).toFloat()
            }
            Aligns.right.data ->{
                top = ((this.height/2) - (shapeHeight/2)).toFloat()
                left = (this.width - shapeWidth).toFloat()
                right = this.width.toFloat()
                bottom = ((this.height/2) + (shapeHeight/2)).toFloat()
            }
            Aligns.bottom.data ->{
                top = (this.height - shapeHeight).toFloat()
                left = (this.width/2).toFloat() - (shapeWidth/2)
                right = left + shapeWidth
                bottom = this.height.toFloat()
            }
            Aligns.bottom_left.data ->{
                top = (this.height - shapeHeight).toFloat()
                right = left + shapeWidth
                bottom = this.height.toFloat()
            }
            Aligns.bottom_right.data ->{
                top = (this.height - shapeHeight).toFloat()
                left = (this.width - shapeWidth).toFloat()
                right = this.width.toFloat()
                bottom = this.height.toFloat()
            }
        }

        return RectF(left, top,  right, bottom)
    }
}