package com.lebedaliv2601.instacat.utils.di

import com.lebedaliv2601.instacat.CatsApp
import okhttp3.OkHttpClient
import org.koin.android.ext.android.getKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

val dataModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("x-api-key", "71667ced-3d38-485f-96cf-3351fa1cc5c4")
                chain.proceed(builder.build())
            }
            .build()
    }
}

fun <T : Any> createApi(
    apiClass: KClass<T>,
    client: OkHttpClient = CatsApp.get().getKoin().get()
): T {
    return Retrofit.Builder()
        .baseUrl("https://api.thecatapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(client)
        .build()
        .create(apiClass.java)
}