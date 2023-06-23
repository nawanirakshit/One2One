package android.sleek.construction.utils.cache

import android.graphics.drawable.Drawable

import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.rakshit.one.ApplicationClass
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

fun preCache(imageURL: String): Target<Drawable> {
    return Glide.with(ApplicationClass.getApp())
        .load(imageURL)
        .apply(
            RequestOptions()
                .useUnlimitedSourceGeneratorsPool(true)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        ).preload(SIZE_ORIGINAL, SIZE_ORIGINAL)
}

fun preCache(imageURLs: List<String>) {
    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e(
            "GlideCache",
            "Glide Cache Job error ${exception.localizedMessage ?: "error message is empty"}"
        )
    }
    GlobalScope.launch(context = handler + IO) {
        supervisorScope {
            imageURLs.forEach {
                launch {
                    preCache(it)
                }
            }
        }
    }
}



