package android.sleek.construction.di

import android.sleek.construction.io.RequestAPIs
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single(createdAtStart = false) { get<Retrofit>().create(RequestAPIs::class.java) }
}