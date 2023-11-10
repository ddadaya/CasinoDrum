package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.os.Handler
import android.os.Looper
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception
import kotlin.random.Random

class Spinner(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var isSpinning = false
    private var drawText: Pair<String,String>?=null
    private var selectedColor: Int? = null
    private var startTime = 0L
    private var currentAngle = 0f
    private var rotationDuration = 0L

    private val colors = arrayOf(
        Color.RED,
        Color.rgb(255, 165, 0),
        Color.YELLOW,
        Color.GREEN,
        Color.CYAN,
        Color.BLUE,
        Color.MAGENTA
    )
    private val rotationHandler = Handler(Looper.getMainLooper())
    private val paint = Paint()
    private val rotationSpeed = 0.5f
    private val rotationInterval = 1


    init {
        paint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        val radius = Math.min(width, height) / 2.0f
        val centerX = width / 2.0f
        val centerY = (height * 0.4).toFloat()

        val angle = 360f / colors.size

        for (i in colors.indices) {
            paint.color = colors[i]

            val startAngle = i * angle + currentAngle
            val sweepAngle = angle

            canvas.drawArc(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius,
                startAngle,
                sweepAngle,
                true,
                paint
            )

        }

        val grayStripeHeight = height / 6
        val grayStripeWidth = 20f

        paint.color = Color.BLACK
        canvas.drawRect(
            centerX - grayStripeWidth / 2,
            0f,
            centerX + grayStripeWidth / 2,
            grayStripeHeight,
            paint
        )

        if (drawText?.second.equals("Текст")) {

            val paint = Paint().apply {
                color = Color.WHITE
                textSize = 50f
            }

            drawText?.let {
                canvas.drawText(it.first, 50f, height-100, paint)
            }
        }

        if (drawText?.second.equals("Картинка")) {

            var url = if(drawText?.first.equals("Оранжевый")){
                "https://dummyimage.com/200x100/ff8000/000000"
            } else{
                "https://dummyimage.com/200x100/${selectedColor}/000000"
            }

            Picasso.get().load(url).into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    bitmap?.let { canvas.drawBitmap(it, 50f, height-200, paint) }
                    invalidate()
                }
                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
        }
    }

    fun startSpinning() {
        if (!isSpinning) {
            isSpinning = true

            rotationDuration = (Random.nextInt(1, 12) * 800).toLong()

            startTime = System.currentTimeMillis()
            rotationHandler.post(rotationRunnable)
        }
    }

    private fun stopSpinning() {
        if (isSpinning) {
            isSpinning = false
            rotationHandler.removeCallbacks(rotationRunnable)
            val pixelColor = getPixelColor(width / 2, height/4+5)
            drawText = getColorName(pixelColor)

            selectedColor=pixelColor

            invalidate()

        }
    }
    fun reset(){
        drawText=null
        invalidate()
    }

    private val rotationRunnable = object : Runnable {
        override fun run() {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - startTime
            if (elapsedTime >= rotationDuration) {
                stopSpinning()
            } else {
                currentAngle = (elapsedTime * rotationSpeed) % 360
                invalidate()
                rotationHandler.postDelayed(this, rotationInterval.toLong())
            }
        }
    }

    private fun getPixelColor(x: Int, y: Int): Int {
        val bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap.getPixel(x, y)
    }

    private fun getColorName(color: Int?): Pair<String,String> {
        return when (color) {
            Color.RED -> "Красный" to "Текст"
            Color.rgb(255, 165, 0) -> "Оранжевый" to "Картинка"
            Color.YELLOW -> "Жёлтый" to "Текст"
            Color.GREEN -> "Зелёный" to "Картинка"
            Color.CYAN -> "Голубой" to "Текст"
            Color.BLUE -> "Синий" to "Картинка"
            Color.MAGENTA -> "Фиолетовый" to "Текст"
            else -> "Неизвестный цвет" to " "
        }
    }
}
