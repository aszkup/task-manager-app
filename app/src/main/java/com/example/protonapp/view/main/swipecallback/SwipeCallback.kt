package com.example.protonapp.view.main.swipecallback

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeCallback(swipeDirection: Int)
    : ItemTouchHelper.SimpleCallback(DRAG_DIRECTION, swipeDirection) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    companion object {
        const val DRAG_DIRECTION = 0
    }
}
