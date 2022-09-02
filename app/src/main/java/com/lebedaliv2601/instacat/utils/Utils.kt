package com.lebedaliv2601.instacat.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lebedaliv2601.instacat.CatsApp
import kotlin.math.ceil

//Context

fun Context.getColorCompat(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun Context.dpToPixSize(value: Float): Int {
    return ceil(value * resources.displayMetrics.density).toInt()
}

//end Context


//Functions

fun getApplicationColor(resId: Int) = CatsApp.get().getColorCompat(resId)

fun getString(resId: Int) = CatsApp.get().getString(resId)

//end Functions

//View

fun View.setCustomBackground(background: CustomBackground) {
    val backgroundDrawable = AppCompatResources.getDrawable(
        context,
        background.backgroundResId
    ) as GradientDrawable
    backgroundDrawable.mutate()

    background.backgroundTint?.let {
        backgroundDrawable.setColor(getApplicationColor(it))
    }

    if (background.width != null && background.height != null) {
        backgroundDrawable.setSize(
            context.dpToPixSize(background.width.toFloat()),
            context.dpToPixSize(background.height.toFloat())
        )
    }

    background.cornerRadius?.let {
        backgroundDrawable.cornerRadius = context.dpToPixSize(it).toFloat()
    }

    if (background.strokeColor != null && background.strokeWidth != null) {
        backgroundDrawable.setStroke(
            context.dpToPixSize(background.strokeWidth.toFloat()),
            getApplicationColor(background.strokeColor)
        )
    }
    setBackground(backgroundDrawable)
}

//end View

//RecyclerView

fun RecyclerView.setPagingAction(
    itemsBeforeEnd: Int = 5,
    startPageNumber: Int = 1,
    action: (Int) -> Unit
) {

    addOnScrollListener(object : RecyclerView.OnScrollListener() {

        private var dataLastIndex = 0
        private var currentItemCount = 0
        private var page = startPageNumber

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            val adapterItemsCount = if (adapter is BasePagingAdapter<*>) {
                (adapter as BasePagingAdapter<*>).getDataItemsCount()
            } else {
                adapter?.itemCount ?: 0
            }
            if (currentItemCount > adapterItemsCount) {
                dataLastIndex = 0
                page = startPageNumber
            }
            currentItemCount = adapterItemsCount

            val layoutManager = layoutManager
            if (layoutManager is LinearLayoutManager) {

                //Если индекс последнего видимого элемента равен индексу последнего
                //элемента в списке в адаптере, и при этом до этого с этого места не были
                //загружены данные, то загрузить новый данные
                if (layoutManager.findLastVisibleItemPosition() ==
                    adapterItemsCount - itemsBeforeEnd &&
                    dataLastIndex != layoutManager.findLastVisibleItemPosition()
                ) {
                    dataLastIndex = layoutManager.findLastVisibleItemPosition()
                    page += 1
                    action.invoke(page)
                }
            }

            super.onScrolled(recyclerView, dx, dy)
        }

    })
}

//end RecyclerView
