package com.serelik.todoapp.list

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.serelik.todoapp.R
import kotlin.math.roundToInt

class SwipeHelperCallback(val context: Context, val onSwipeTodo: OnSwipeTodo) :
    ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
    private val resources = context.resources
    private val displayMetrics: DisplayMetrics = resources.displayMetrics
    private val width = (displayMetrics.widthPixels / displayMetrics.density).toInt().dp

    private val swipeDeleteIcon =
        ResourcesCompat.getDrawable(resources, R.drawable.ic_swipe_delete, null)
    private val swipeCheckedIcon =
        ResourcesCompat.getDrawable(resources, R.drawable.ic_swipe_checked, null)

    private val deleteColor = ContextCompat.getColor(context, R.color.red)
    private val checkedDoneColor = ContextCompat.getColor(context, R.color.green)

    private val iconMargin = resources.getDimensionPixelOffset(R.dimen.DeleteDrawableMargin)


    private val rect = Rect().apply {
        left = 0
        right = width
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder !is TodoItemViewHolder) {
            return
        }

        val position = viewHolder.adapterPosition

        if (direction == ItemTouchHelper.LEFT) {
            onSwipeTodo.onSwipeDelete(position)
        } else {
            onSwipeTodo.onSwipeDone(position)
        }
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (viewHolder !is TodoItemViewHolder) {
            return
        }

        drawBackground(dX, viewHolder, canvas)

        if (dX < 0) {
            setupBoundsForDeleteIcon(viewHolder)
            swipeDeleteIcon?.draw(canvas)
        } else {
            setupBoundsForDoneIcon(viewHolder)
            swipeCheckedIcon?.draw(canvas)
        }

        super.onChildDraw(
            canvas,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    private fun setupBoundsForDoneIcon(viewHolder: RecyclerView.ViewHolder) {
        val drawableHeight = swipeDeleteIcon?.intrinsicHeight ?: 0
        val verticalPadding = (viewHolder.itemView.height - drawableHeight) / 2
        if (swipeCheckedIcon == null) return
        swipeCheckedIcon.bounds = Rect(
            iconMargin,
            viewHolder.itemView.top + verticalPadding,
            iconMargin + swipeCheckedIcon.intrinsicWidth,
            viewHolder.itemView.top + swipeCheckedIcon.intrinsicHeight + verticalPadding
        )
    }

    private fun setupBoundsForDeleteIcon(viewHolder: RecyclerView.ViewHolder) {
        val drawableHeight = swipeDeleteIcon?.intrinsicHeight ?: 0
        val verticalPadding = (viewHolder.itemView.height - drawableHeight) / 2
        if (swipeDeleteIcon == null) return
        swipeDeleteIcon.bounds = Rect(
            width - iconMargin * 2 - swipeDeleteIcon.intrinsicWidth,
            viewHolder.itemView.top + verticalPadding,
            width - iconMargin * 2,
            viewHolder.itemView.top + swipeDeleteIcon.intrinsicHeight +
                    verticalPadding
        )
    }

    fun drawBackground(dX: Float, viewHolder: RecyclerView.ViewHolder, canvas: Canvas) {
        paint.color = when {
            dX < 0 -> deleteColor
            else -> checkedDoneColor
        }

        rect.apply {
            top = viewHolder.itemView.top
            bottom = viewHolder.itemView.bottom
        }

        canvas.drawRect(rect, paint)
    }

    private val Int.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(),
            resources.displayMetrics
        ).roundToInt()
}

interface OnSwipeTodo {
    fun onSwipeDone(position: Int)
    fun onSwipeDelete(position: Int)

}