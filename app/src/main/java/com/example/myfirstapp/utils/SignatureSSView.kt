package com.example.myfirstapp.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.View.OnTouchListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs


class SignatureSSView : SurfaceView, OnTouchListener, SurfaceHolder.Callback {

    private lateinit var _Bitmap: Bitmap
    private lateinit var _Canvas: Canvas
    private val _Path: Path = Path()
    private val BITMAPPAINT: Paint = Paint(Paint.DITHER_FLAG)
    private val PAINT: Paint = Paint()
    private var _mX: Float = 0.toFloat()
    private var isBlank = true
    private var _mY: Float = 0.toFloat()
    private val TOUCHTOLERANCE = 4f
    private val LINETHICKNESS = 4f

    suspend fun getSign(): Bitmap{

        return withContext(Dispatchers.IO) {
            bitmap
        }

    }

    private val bitmap: Bitmap
        get() {
            val v = this as View
            val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.layout(v.left, v.top, v.right, v.bottom)
            v.draw(c)

            return b
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    override fun surfaceChanged(arg0: SurfaceHolder, arg1: Int, arg2: Int, arg3: Int) {

    }

    override fun surfaceCreated(arg0: SurfaceHolder) {

    }

    override fun surfaceDestroyed(arg0: SurfaceHolder) {

    }

    override fun onTouch(p0: View, e: MotionEvent): Boolean {
        super.onTouchEvent(e)
        val x = e.x
        val y = e.y
        isBlank = false
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }

        return true
    }

    private fun init() {
        this.setBackgroundColor(Color.TRANSPARENT)
        PAINT.color = Color.BLACK
        PAINT.isAntiAlias = true
        PAINT.isDither = true
        PAINT.style = Paint.Style.STROKE
        PAINT.strokeJoin = Paint.Join.ROUND
        PAINT.strokeCap = Paint.Cap.ROUND
        PAINT.strokeWidth = LINETHICKNESS
        this.setOnTouchListener(this)
        this.holder.addCallback(this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        _Bitmap = Bitmap.createBitmap(
            w,
            if (h > 0) h else (this.parent as View).height,
            Bitmap.Config.ARGB_8888
        )
        _Canvas = Canvas(_Bitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(_Bitmap, 0f, 0f, BITMAPPAINT)
        canvas.drawPath(_Path, PAINT)
    }

    private fun touchStart(x: Float, y: Float) {
        _Path.reset()
        _Path.moveTo(x, y)
        _mX = x
        _mY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = abs(x - _mX)
        val dy = abs(y - _mY)

        if (dx >= TOUCHTOLERANCE || dy >= TOUCHTOLERANCE) {
            _Path.quadTo(_mX, _mY, (x + _mX) / 2, (y + _mY) / 2)
            _mX = x
            _mY = y
        }
    }

    private fun touchUp() {
        if (!_Path.isEmpty) {
            _Path.lineTo(_mX, _mY)
            _Canvas.drawPath(_Path, PAINT)
        } else {
            _Canvas.drawPoint(_mX, _mY, PAINT)
        }

        _Path.reset()
    }

    fun clearCanvas() {
        isBlank = true
        _Canvas.drawColor(Color.WHITE)
        invalidate()
    }

    fun isCanvasBlank():Boolean {
        return isBlank
    }
}