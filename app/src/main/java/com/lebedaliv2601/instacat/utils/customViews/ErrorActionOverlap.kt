package com.lebedaliv2601.instacat.utils.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.lebedaliv2601.instacat.R
import com.lebedaliv2601.instacat.databinding.ViewErrorActionOverlapBinding
import com.lebedaliv2601.instacat.utils.CustomBackground
import com.lebedaliv2601.instacat.utils.setCustomBackground

class ErrorActionOverlap(
    context: Context,
    attributeSet: AttributeSet
) : ConstraintLayout(context, attributeSet) {

    private val vb: ViewErrorActionOverlapBinding =
        ViewErrorActionOverlapBinding.inflate(LayoutInflater.from(context), this)

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.ErrorActionOverlap,
            0,
            0
        ).apply {
            vb.tvActionButton.text = getString(R.styleable.ErrorActionOverlap_buttonText) ?: ""
            vb.tvMessageText.text = getString(R.styleable.ErrorActionOverlap_messageText) ?: ""
            recycle()
        }
        vb.tvActionButton.setCustomBackground(
            CustomBackground(
                backgroundResId = R.drawable.background_universal,
                backgroundTint = R.color.blue,
                cornerRadius = CustomBackground.CORNER_RADIUS_16_DP
            )
        )
    }

    fun setAction(action: () -> Unit) {
        vb.tvActionButton.setOnClickListener {
            action.invoke()
        }
    }

    fun setMessageText(text: String) {
        vb.tvMessageText.text = text
        invalidate()
        requestLayout()
    }

    fun setButtonText(text: String) {
        vb.tvActionButton.text = text
        invalidate()
        requestLayout()
    }
}