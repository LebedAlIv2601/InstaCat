package com.lebedaliv2601.instacat.main.data

import com.lebedaliv2601.instacat.main.data.api.model.CatMainDataModel
import com.lebedaliv2601.instacat.main.ui.model.CatMainModel

fun CatMainDataModel.toCatMainModel(): CatMainModel = CatMainModel(id, url)