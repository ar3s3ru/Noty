package com.danilocianfrone.noty.views.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.support.annotation.ColorRes
import android.util.AttributeSet
import android.widget.LinearLayout
import com.danilocianfrone.noty.R


class NoteLayout : LinearLayout {

    @ColorRes var color: Int = Color.LTGRAY
            set(value) {
                mPaint.color = value
            }

    private var mPadding: Int   = 0
    //private var mPath:    Path  = Path()
    private var mPaint:   Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.color = color
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {

        val ta = context.obtainStyledAttributes(attrs, R.styleable.NoteLayout)

        try {
            color = ta.getColor(R.styleable.NoteLayout_color, Color.LTGRAY)
            mPadding = ta.getDimensionPixelSize(R.styleable.NoteLayout_padding, 0)
        } finally {
            ta.recycle()
        }

    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.drawRect(
                mPadding.toFloat(),
                mPadding.toFloat(),
                (measuredWidth - mPadding).toFloat(),
                (measuredHeight - mPadding).toFloat(),
                mPaint
        )

        super.dispatchDraw(canvas)
    }
}