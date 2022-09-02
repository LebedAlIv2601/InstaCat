package com.lebedaliv2601.instacat

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.lebedaliv2601.instacat.main.di.mainModule
import com.lebedaliv2601.instacat.utils.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CatsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        val config = ImagePipelineConfig.newBuilder(this)
            .setDownsampleEnabled(true)
            .build();
        Fresco.initialize(this, config)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@CatsApp)
            modules(
                listOf(
                    dataModule,
                    mainModule
                )
            )
        }
    }

    companion object {
        private lateinit var instance: CatsApp
        fun get() = instance
    }
}