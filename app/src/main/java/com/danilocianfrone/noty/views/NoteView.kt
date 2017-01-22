package com.danilocianfrone.noty.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.models.Priority

/**
 * Custom ViewGroup that displays a Note.
 */
class NoteView : LinearLayout {

    private var priority: Priority = Priority.MEDIUM

    private val mTopPath:  Path = Path()
    private val mBodyPath: Path = Path()

    private val mPaintBody: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintTop:  Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val points: Array<Point> = arrayOf(
            Point(), Point(), Point(), Point(), Point()
    )

    //private val foldingSize: Int

    init {
        mPaintTop.style  = Paint.Style.FILL_AND_STROKE
        mPaintBody.style = Paint.Style.FILL_AND_STROKE

        mPaintTop.color  = Color.YELLOW
        mPaintBody.color = Color.RED

        mTopPath.fillType  = Path.FillType.EVEN_ODD
        mBodyPath.fillType = Path.FillType.EVEN_ODD
    }

    constructor(context: Context) :
            this(context, null)
    constructor(context: Context, attrs: AttributeSet?) :
            this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) {
        // Get TypedArray for custom options
        //
    }

    fun of(note: Note): NoteView {
        // Set up all the necessary fields
        priority = note.priority

        mPaintTop.color  = context.resources.getColor(priority.ColorTop())
        mPaintBody.color = context.resources.getColor(priority.ColorBody())

        (getChildAt(0) as TextView).text = note.content
        (getChildAt(1) as TextView).text = note.creation.toString()

        return this
    }

    fun pointMeasuring(width: Int, height: Int) {
        points[0].x = paddingLeft //+ foldingSize
        points[0].y = paddingTop

        points[1].x = paddingLeft
        points[1].y = paddingTop //+ foldingSize

        points[2].x = paddingLeft
        points[2].y = height - paddingBottom

        points[3].x = width - paddingRight
        points[3].y = height - paddingBottom

        points[4].x = width - paddingRight
        points[4].y = paddingTop
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (i in 0..1) {
            val view = getChildAt(i) as? TextView ?:
                    throw IllegalStateException("First two NoteLayout childs must be TextViews")
        }

        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        pointMeasuring(measuredWidth, measuredHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pointMeasuring(w, h)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        // Top path
        mTopPath.moveTo(points[0].x.toFloat(),  points[0].y.toFloat())
        mTopPath.lineTo(points[1].x.toFloat(),  points[1].y.toFloat())
        mTopPath.lineTo(points[0].x.toFloat() + points[1].x.toFloat(),
                points[0].y.toFloat() + points[1].y.toFloat())
        mTopPath.close()

        // Body path
        mBodyPath.moveTo(points[0].x.toFloat(),  points[0].y.toFloat())
        mBodyPath.lineTo(points[0].x.toFloat() + points[1].x.toFloat(),
                points[0].y.toFloat() + points[1].y.toFloat())
        mBodyPath.lineTo(points[1].x.toFloat(),  points[1].y.toFloat())
        mBodyPath.lineTo(points[2].x.toFloat(),  points[2].y.toFloat())
        mBodyPath.lineTo(points[3].x.toFloat(),  points[3].y.toFloat())
        mBodyPath.lineTo(points[4].x.toFloat(),  points[4].y.toFloat())
        mBodyPath.close()

        // Draw note
        canvas?.drawPath(mBodyPath, mPaintBody)
        canvas?.drawPath(mTopPath, mPaintTop)

        // Draw everything else...
        super.dispatchDraw(canvas)
    }

    override fun toString(): String =
            "NoteLayout{points=[${points[0]}, ${points[1]}, ${points[2]}, ${points[3]}, ${points[4]}}"

    companion object {
        const val TAG = "NoteLayout"
    }
}