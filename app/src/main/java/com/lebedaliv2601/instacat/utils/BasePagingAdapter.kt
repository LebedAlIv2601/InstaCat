package com.lebedaliv2601.instacat.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lebedaliv2601.instacat.R

class BasePagingAdapter<T>(
    private val dataLayoutRes: Int,
    private val dataLayoutBind: (T, View) -> Unit,
    private val errorLayoutRes: Int? = null,
    private val errorLayoutBind: ((View) -> Unit)? = null,
    private val loadingLayoutRes: Int? = null,
    private val loadingLayoutBind: ((View) -> Unit)? = null,
    private val defaultErrorAction: (() -> Unit)? = null
) : ListAdapter<BasePagingAdapter.BasePagingDataModel, BasePagingAdapter.BasePagingListViewHolder<T>>(
    DiffCallback()
) {

    private var hasLoadingItem = false
    private var hasErrorItem = false

    @Suppress("UNCHECKED_CAST")
    class BasePagingListViewHolder<T>(
        itemView: View,
        private val dataLayoutBind: (T, View) -> Unit,
        private val errorLayoutBind: ((View) -> Unit)? = null,
        private val loadingLayoutBind: ((View) -> Unit)? = null,
        private val defaultErrorAction: (() -> Unit)? = null
    ) :
        RecyclerView.ViewHolder(itemView) {

        private fun bindDataModel(item: BasePagingDataModel.DataModel<T>) {
            dataLayoutBind.invoke(item.data, itemView)
        }

        private fun bindLoadingModel(item: BasePagingDataModel.LoadingModel) {
            loadingLayoutBind?.invoke(itemView)
        }

        private fun bindErrorModel(item: BasePagingDataModel.ErrorModel) {
            if (errorLayoutBind == null) {
                val actionButton = itemView.findViewById<TextView>(R.id.tvActionButton)
                actionButton.text = getString(R.string.main_try_again)
                actionButton.setCustomBackground(
                    CustomBackground(
                        backgroundResId = R.drawable.background_universal,
                        backgroundTint = R.color.blue,
                        cornerRadius = CustomBackground.CORNER_RADIUS_16_DP
                    )
                )

                defaultErrorAction?.let { action ->
                    actionButton.setOnClickListener {
                        action.invoke()
                    }
                }

                val messageText = itemView.findViewById<TextView>(R.id.tvMessageText)
                messageText.text = getString(R.string.main_something_wrong)
            } else {
                errorLayoutBind.invoke(itemView)
            }
        }

        fun bind(item: BasePagingDataModel) {
            when (item) {
                is BasePagingDataModel.DataModel<*> -> bindDataModel(item as BasePagingDataModel.DataModel<T>)
                is BasePagingDataModel.ErrorModel -> bindErrorModel(item)
                is BasePagingDataModel.LoadingModel -> bindLoadingModel(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasePagingListViewHolder<T> {
        val layout = when (viewType) {
            TYPE_LOADING -> loadingLayoutRes ?: R.layout.view_loading_item
            TYPE_ERROR -> errorLayoutRes ?: R.layout.view_error_item
            TYPE_DATA -> dataLayoutRes
            else -> throw IllegalArgumentException("Invalid type")
        }
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)

        return BasePagingListViewHolder(
            view,
            dataLayoutBind,
            errorLayoutBind,
            loadingLayoutBind,
            defaultErrorAction
        )
    }

    override fun onBindViewHolder(holder: BasePagingListViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BasePagingDataModel.ErrorModel -> TYPE_ERROR
            is BasePagingDataModel.LoadingModel -> TYPE_LOADING
            is BasePagingDataModel.DataModel<*> -> TYPE_DATA
        }
    }

    fun addLoadingItem() {
        val list = currentList.toMutableList()
        if (hasErrorItem) {
            list.removeAt(list.lastIndex)
            hasErrorItem = false
        }
        if (!hasLoadingItem) {
            list.add(BasePagingDataModel.LoadingModel)
            hasLoadingItem = true
        }
        submitList(list)
    }

    fun addErrorItem() {
        val list = currentList.toMutableList()
        if (hasLoadingItem) {
            list.removeAt(list.lastIndex)
            hasLoadingItem = false
        }
        if (!hasErrorItem) {
            list.add(BasePagingDataModel.ErrorModel)
            hasErrorItem = true
        }
        submitList(list)
    }

    fun removeNotDataItem() {
        if (hasLoadingItem || hasErrorItem) {
            val list = currentList.toMutableList()
            list.removeAt(list.lastIndex)
            submitList(list)
            hasLoadingItem = false
            hasErrorItem = false
        }
    }

    fun updateList(list: List<T>) {
        submitList(list.map { BasePagingDataModel.DataModel(it) })
    }

    fun getDataItemsCount(): Int {
        return currentList.count { it is BasePagingDataModel.DataModel<*> }
    }

    class DiffCallback : DiffUtil.ItemCallback<BasePagingDataModel>() {
        override fun areItemsTheSame(
            oldItem: BasePagingDataModel,
            newItem: BasePagingDataModel
        ): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: BasePagingDataModel,
            newItem: BasePagingDataModel
        ): Boolean =
            oldItem == newItem

    }

    sealed class BasePagingDataModel {
        data class DataModel<T>(val data: T) : BasePagingDataModel()
        object LoadingModel : BasePagingDataModel()
        object ErrorModel : BasePagingDataModel()
    }

    companion object {
        private const val TYPE_DATA = 0
        private const val TYPE_LOADING = 1
        private const val TYPE_ERROR = 2
    }
}

