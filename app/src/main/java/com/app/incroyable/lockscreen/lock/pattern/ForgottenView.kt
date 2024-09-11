package com.app.incroyable.lockscreen.lock.pattern

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import com.app.incroyable.lockscreen.R
import kotlin.math.atan2
import kotlin.math.sqrt


class ForgottenView : GridLayout {

    companion object {
        const val DEFAULT_RADIUS_RATIO = 0.3f
        const val DEFAULT_LINE_WIDTH = 2f // unit: dp
        const val DEFAULT_SPACING = 24f // unit: dp
        const val DEFAULT_ROW_COUNT = 3
        const val DEFAULT_COLUMN_COUNT = 3
        const val DEFAULT_ERROR_DURATION = 400 // unit: ms
        const val DEFAULT_HIT_AREA_PADDING_RATIO = 0.2f
        const val DEFAULT_INDICATOR_SIZE_RATIO = 0.2f
    }

    private var regularCellBackground: Drawable? = null
    private var regularDotColor: Int = 0
    private var regularDotRadiusRatio: Float = 0f

    private var selectedCellBackground: Drawable? = null
    private var selectedDotColor: Int = 0
    private var selectedDotRadiusRatio: Float = 0f

    private var errorCellBackground: Drawable? = null
    private var errorDotColor: Int = 0
    private var errorDotRadiusRatio: Float = 0f

    private var lineStyle: Int = 0

    private var lineWidth: Int = 0
    private var regularLineColor: Int = 0
    private var errorLineColor: Int = 0

    private var spacing: Int = 0

    private var plvRowCount: Int = 0
    private var plvColumnCount: Int = 0

    private var errorDuration: Int = 0
    private var hitAreaPaddingRatio: Float = 0f
    private var indicatorSizeRatio: Float = 0f

    private var cells: ArrayList<Cell> = ArrayList()
    private var selectedCells: ArrayList<Cell> = ArrayList()

    private var linePaint: Paint = Paint()
    private var linePath: Path = Path()

    private var lastX: Float = 0f
    private var lastY: Float = 0f

    private var onPatternListener: OnPatternListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        var ta = context.obtainStyledAttributes(attributeSet, R.styleable.PatternLockView)
        regularCellBackground =
            ta.getDrawable(R.styleable.PatternLockView_plv_regularCellBackground)
        regularDotColor = ta.getColor(
            R.styleable.PatternLockView_plv_regularDotColor,
            ContextCompat.getColor(context, R.color.regularColor)
        )
        regularDotRadiusRatio =
            ta.getFloat(R.styleable.PatternLockView_plv_regularDotRadiusRatio, DEFAULT_RADIUS_RATIO)

        selectedCellBackground =
            ta.getDrawable(R.styleable.PatternLockView_plv_selectedCellBackground)
        selectedDotColor = ta.getColor(
            R.styleable.PatternLockView_plv_selectedDotColor,
            ContextCompat.getColor(context, R.color.selectedColor)
        )
        selectedDotRadiusRatio = ta.getFloat(
            R.styleable.PatternLockView_plv_selectedDotRadiusRatio,
            DEFAULT_RADIUS_RATIO
        )

        errorCellBackground = ta.getDrawable(R.styleable.PatternLockView_plv_errorCellBackground)
        errorDotColor = ta.getColor(
            R.styleable.PatternLockView_plv_errorDotColor,
            ContextCompat.getColor(context, R.color.errorColor)
        )
        errorDotRadiusRatio =
            ta.getFloat(R.styleable.PatternLockView_plv_errorDotRadiusRatio, DEFAULT_RADIUS_RATIO)

        lineStyle = ta.getInt(R.styleable.PatternLockView_plv_lineStyle, 1)
        lineWidth = ta.getDimensionPixelSize(
            R.styleable.PatternLockView_plv_lineWidth,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_LINE_WIDTH,
                context.resources.displayMetrics
            ).toInt()
        )
        regularLineColor = ta.getColor(
            R.styleable.PatternLockView_plv_regularLineColor,
            ContextCompat.getColor(context, R.color.selectedColor)
        )
        errorLineColor = ta.getColor(
            R.styleable.PatternLockView_plv_errorLineColor,
            ContextCompat.getColor(context, R.color.errorColor)
        )

        spacing = ta.getDimensionPixelSize(
            R.styleable.PatternLockView_plv_spacing,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_SPACING,
                context.resources.displayMetrics
            ).toInt()
        )

        plvRowCount = ta.getInteger(R.styleable.PatternLockView_plv_rowCount, DEFAULT_ROW_COUNT)
        plvColumnCount =
            ta.getInteger(R.styleable.PatternLockView_plv_columnCount, DEFAULT_COLUMN_COUNT)

        errorDuration =
            ta.getInteger(R.styleable.PatternLockView_plv_errorDuration, DEFAULT_ERROR_DURATION)
        hitAreaPaddingRatio = ta.getFloat(
            R.styleable.PatternLockView_plv_hitAreaPaddingRatio,
            DEFAULT_HIT_AREA_PADDING_RATIO
        )
        indicatorSizeRatio = ta.getFloat(
            R.styleable.PatternLockView_plv_indicatorSizeRatio,
            DEFAULT_INDICATOR_SIZE_RATIO
        )

        ta.recycle()

        rowCount = plvRowCount
        columnCount = plvColumnCount

        setupCells()
        initPathPaint()
    }

    private fun notifyCellSelected(cell: Cell) {
        selectedCells.add(cell)
        onPatternListener?.onProgress(generateSelectedIds())
        cell.setState(State.SELECTED)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        for (i in 0 until selectedCells.size - 1) {
            val currentCell = selectedCells[i]
            val nextCell = selectedCells[i + 1]
            val startX = currentCell.getCenter().x.toFloat()
            val startY = currentCell.getCenter().y.toFloat()
            val endX = nextCell.getCenter().x.toFloat()
            val endY = nextCell.getCenter().y.toFloat()

            // Draw line
            canvas.drawLine(startX, startY, endX, endY, linePaint)

            // Update indicators
            val previousCell = selectedCells[i]
            val cell = selectedCells[i + 1]

            val previousCellCenter = previousCell.getCenter()
            val center = cell.getCenter()
            val diffX = center.x - previousCellCenter.x
            val diffY = center.y - previousCellCenter.y
            val radius = cell.getRadius()
            val length = sqrt((diffX * diffX + diffY * diffY).toDouble())

            linePath.moveTo(
                (previousCellCenter.x + radius * diffX / length).toFloat(),
                (previousCellCenter.y + radius * diffY / length).toFloat()
            )
            linePath.lineTo(
                (center.x - radius * diffX / length).toFloat(),
                (center.y - radius * diffY / length).toFloat()
            )

            val degree = Math.toDegrees(atan2(diffY.toDouble(), diffX.toDouble())) + 90
            previousCell.setDegree(degree.toFloat())
            previousCell.invalidate()
        }
    }

    private fun setupCells() {
        for (i in 0..(plvRowCount - 1)) {
            for (j in 0..(plvColumnCount - 1)) {
                var cell = Cell(
                    context, i * plvColumnCount + j,
                    regularCellBackground, regularDotColor, regularDotRadiusRatio,
                    selectedCellBackground, selectedDotColor, selectedDotRadiusRatio,
                    errorCellBackground, errorDotColor, errorDotRadiusRatio,
                    lineStyle, regularLineColor, errorLineColor, plvColumnCount, indicatorSizeRatio
                )
                var cellPadding = spacing / 2
                cell.setPadding(cellPadding, cellPadding, cellPadding, cellPadding)
                addView(cell)

                cells.add(cell)
            }
        }
    }

    private fun initPathPaint() {
        linePaint.isAntiAlias = true
        linePaint.isDither = true
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeJoin = Paint.Join.ROUND
        linePaint.strokeCap = Paint.Cap.ROUND

        linePaint.strokeWidth = lineWidth.toFloat()
        linePaint.color = regularLineColor
    }

    private fun reset() {
        for (cell in selectedCells) {
            cell.reset()
        }
        selectedCells.clear()
        linePaint.color = regularLineColor
        linePath.reset()

        lastX = 0f
        lastY = 0f

        invalidate()
    }

    private fun generateSelectedIds(): ArrayList<Int> {
        var result = ArrayList<Int>()
        for (cell in selectedCells) {
            result.add(cell.index)
        }
        return result
    }

    interface OnPatternListener {
        fun onStarted() {}
        fun onProgress(ids: ArrayList<Int>) {}
        fun onComplete(ids: ArrayList<Int>): Boolean
    }

    fun showForgottenPattern(patternIds: List<Int>) {
        reset() // Reset the view before showing the forgotten pattern

        for (id in patternIds) {
            val cell = cells.find { it.index == id }
            cell?.let {
                notifyCellSelected(it)
            }
        }

        invalidate()
    }

}