package com.lebedaliv2601.instacat.main.data.repository

import com.lebedaliv2601.instacat.main.data.api.MainApi
import com.lebedaliv2601.instacat.main.data.toCatMainModel
import com.lebedaliv2601.instacat.main.ui.model.CatMainModel
import io.reactivex.rxjava3.core.Observable

class MainRepository(
    private val api: MainApi
) {

    fun getCats(): Observable<List<CatMainModel>> =
        api.getRandomCats()
            .map { list ->
                list.map {
                    it.toCatMainModel()
                }
            }
}