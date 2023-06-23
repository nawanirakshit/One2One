package android.sleek.construction.di

import com.google.gson.GsonBuilder
import android.sleek.construction.io.networking.coroutineRequestAdapter.NetworkResponseAdapterFactory
import androidx.multidex.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECT_TIMEOUT = 1L
private const val WRITE_TIMEOUT = 30L
private const val READ_TIMEOUT = 30L

val networkModule = module {
    single { Cache(androidApplication().cacheDir, 10L * 1024 * 1024) }

    single { GsonBuilder().setLenient().create() }

    single {
        OkHttpClient.Builder().apply {
            cache(get())
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.MINUTES)
            readTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

            retryOnConnectionFailure(true)
            addInterceptor(HttpLoggingInterceptor().apply {

                //Display Logs only in debug mode
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }

            })
        }.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://sleek.gstb.in/api/")
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(get())
            .build()
    }
}
