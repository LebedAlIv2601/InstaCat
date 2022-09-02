package com.lebedaliv2601.instacat.main.data.api

import com.lebedaliv2601.instacat.main.data.api.model.CatMainDataModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface MainApi {

    @GET("images/search?limit=$CATS_PER_QUERY")
    fun getRandomCats(): Observable<List<CatMainDataModel>>

    companion object {
        const val CATS_PER_QUERY = 30
    }
}