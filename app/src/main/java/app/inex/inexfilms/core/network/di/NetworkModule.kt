package app.inex.inexfilms.core.network.di

import app.inex.inexfilms.BuildConfig
import app.inex.inexfilms.core.network.utils.DefaultHeadersInterceptor
import app.inex.inexfilms.core.network.utils.HostCookieJar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class RefreshUriOkHttpClient

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().run {
            if (BuildConfig.DEBUG) addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            addInterceptor(DefaultHeadersInterceptor())
            cookieJar(HostCookieJar())
            build()
        }
    }

    @Provides
    @RefreshUriOkHttpClient
    fun provideCheckBaseUrlOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(DefaultHeadersInterceptor())
            .cookieJar(HostCookieJar())
            .followSslRedirects(false)
            .followRedirects(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }
}