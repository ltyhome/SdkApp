package com.android.sdk.wedgit

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.Nullable
import com.android.sdk.R


class LoadingView @JvmOverloads constructor(
    context: Context, @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mCenterX: Float = 0f
    private var mCenterY: Float = 0f
    private var mPaint: Paint? = null
    private val mDefaultColor = -0x666667
    private val mDefaultSegmentWidth = 10
    private val mDefaultSegmentLength = 20
    private var mSegmentWidth = mDefaultSegmentWidth
    private var mSegmentColor = mDefaultColor
    private var mSegmentLength = mDefaultSegmentLength

    private var control = 1

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs,
            R.styleable.LoadingView
        )
        val indexCount = typedArray.indexCount
        for (i in 0 until indexCount) {
            when (val attr = typedArray.getIndex(i)) {
                R.styleable.LoadingView_pathColor -> mSegmentColor =
                    typedArray.getColor(attr, mDefaultColor)
                R.styleable.LoadingView_segmentLength -> mSegmentLength =
                    typedArray.getDimensionPixelSize(attr, mDefaultSegmentLength)
                R.styleable.LoadingView_segmentWidth -> mSegmentWidth =
                    typedArray.getDimensionPixelSize(attr, mDefaultSegmentWidth)
            }
        }
        typedArray.recycle()

        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.strokeCap = Paint.Cap.ROUND
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.color = mSegmentColor
        mPaint!!.strokeWidth = mSegmentWidth.toFloat()

    }

     override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        mCenterX = mWidth / 2f
        mCenterY = mHeight / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0..11) {
            mPaint!!.alpha = (i + 1 + control) % 12 * 255 / 12
            canvas.drawLine(
                mCenterX,
                mCenterY - mSegmentLength * 1.3f,
                mCenterX,
                mCenterY - mSegmentLength * 2f,
                mPaint!!
            )
            canvas.rotate(30f, mCenterX, mCenterY)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val valueAnimator = ValueAnimator.ofInt(12, 1)
        valueAnimator.duration = 1000
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.RESTART
        valueAnimator.addUpdateListener { animation ->
            control = animation.animatedValue.toString().toInt()
            invalidate()
        }
        valueAnimator.start()
    }
}