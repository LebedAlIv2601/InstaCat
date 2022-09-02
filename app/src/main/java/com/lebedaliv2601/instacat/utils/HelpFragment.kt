package com.lebedaliv2601.instacat.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

abstract class HelpFragment : Fragment() {

    protected inline fun <T> LiveData<T>.observeState(crossinline observeFun: (T) -> Unit) {
        observe(viewLifecycleOwner) { observeFun.invoke(it) }
    }

}