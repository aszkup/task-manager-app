package com.example.protonapp.view.main.swipecallback

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class TaskSwipeCallback(
        swipeDirection: Int,
        private val onItemSwiped: (Int) -> Unit
) : ItemTouchHelper.SimpleCallback(DRAG_DIRECTION, swipeDirection) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onItemSwiped(viewHolder.adapterPosition)
    }

    companion object {
        const val DRAG_DIRECTION = 0
    }
}
