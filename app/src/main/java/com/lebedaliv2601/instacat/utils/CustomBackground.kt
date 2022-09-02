package com.lebedaliv2601.instacat.utils

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.lebedaliv2601.instacat.R

data class CustomBackground(
    @DrawableRes val backgroundResId: Int = R.drawable.background_universal,
    val cornerRadius: Float? = null,
    @ColorRes val backgroundTint: Int? = R.color.transparent,
    val strokeWidth: Float? = null,
    @ColorRes val strokeColor: Int? = null,
    val width: Int? = null,
    val height: Int? = null
) {

    companion object {
        const val CORNER_RADIUS_16_DP = 16f
    }

}