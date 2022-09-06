package com.lebedaliv2601.instacat.utils.customViews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lebedaliv2601.instacat.R
import com.lebedaliv2601.instacat.databinding.DialogInfoBinding

class BottomInfoDialog : BottomSheetDialogFragment() {

    private var binding: DialogInfoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogInfoBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.tvClose?.setOnClickListener { dismiss() }
    }

    private fun setTitle(text: String) {
        binding?.tvDialogTitle?.text = text
    }

    private fun setBodyText(text: String) {
        binding?.tvInfoText?.text = text
    }

    companion object {
        fun createDialog(
            fragmentManager: FragmentManager,
            title: String, bodyText: String
        ): BottomInfoDialog {
            return BottomInfoDialog().apply {
                setTitle(title)
                setBodyText(bodyText)
                show(fragmentManager, null)
            }
        }
    }
}