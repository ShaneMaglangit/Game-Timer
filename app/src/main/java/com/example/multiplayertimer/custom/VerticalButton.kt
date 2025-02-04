package com.example.multiplayertimer.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Button


@SuppressLint("AppCompatCustomView")
class VerticalButton(context: Context, attrs: AttributeSet) : Button(context, attrs) {
    var topDown: Boolean = false

    init {
        val gravity = gravity
        topDown = if (Gravity.isVertical(gravity) && gravity and Gravity.VERTICAL_GRAVITY_MASK == Gravity.BOTTOM) {
            setGravity(gravity and Gravity.HORIZONTAL_GRAVITY_MASK or Gravity.TOP)
            false
        } else {
            true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure( heightMeasureSpec, widthMeasureSpec );
        setMeasuredDimension( measuredHeight, measuredWidth );
    }

    override fun onDraw(canvas: Canvas?) {
        val textPaint = paint
        textPaint.color = currentTextColor
        textPaint.drawableState = drawableState

        canvas!!.save()

        if (topDown) {
            canvas.translate(width.toFloat(), 0F)
            canvas.rotate(90F)
        } else {
            canvas.translate(0F, height.toFloat())
            canvas.rotate((-90).toFloat())
        }

        canvas.translate(
            compoundPaddingLeft.toFloat(),
            extendedPaddingTop.toFloat()
        )

        layout.draw(canvas)
        canvas.restore()
    }
}