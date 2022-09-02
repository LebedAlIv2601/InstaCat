package com.lebedaliv2601.instacat.main.di

import com.lebedaliv2601.instacat.main.data.api.MainApi
import com.lebedaliv2601.instacat.main.data.repository.MainRepository
import com.lebedaliv2601.instacat.main.ui.MainViewModel
import com.lebedaliv2601.instacat.utils.di.createApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single {
        createApi(MainApi::class)
    }

    single {
        MainRepository(get())
    }

    viewModel {
        MainViewModel(get(), get())
    }
}