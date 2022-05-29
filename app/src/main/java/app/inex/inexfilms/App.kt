package app.inex.inexfilms

import android.app.Application
import android.content.Context
import androidx.appcompat.widget.DrawableUtils
import app.inex.inexfilms.core.common.utils.uiLazy
import app.inex.inexfilms.core.di.AppComponent
import app.inex.inexfilms.core.di.DaggerAppComponent
import coil.ImageLoader
import coil.ImageLoaderFactory
import timber.log.Timber

class App: Application(), ImageLoaderFactory {

    val component: AppComponent by uiLazy {
        DaggerAppComponent.create()
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        app = this
    }

    companion object {
        val component: AppComponent get() = app.component

        val context: Context get() = app

        lateinit var app: App
            private set
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .error(R.color.grey)
            .placeholder(R.color.grey)
            .okHttpClient(App.component.okHttpClient())
            .build()
    }
}