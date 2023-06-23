package com.test.papers.utils.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

/**
 * Caching image functionality
 * Saving all the image in cache and retrieving via Key
 */
class MyImageCache private constructor() {

    private object HOLDER {
        val INSTANCE = MyImageCache()
    }

    companion object {
        val instance: MyImageCache by lazy { HOLDER.INSTANCE }
    }

    val lru: LruCache<Any, Any> = LruCache(1024)

    /**
     * Saving server image in the cache via URL provided from backend
     * Tested with 5 MB of image
     */
    fun saveBitmapToCache(serverURL: String) {

        try {
            GlobalScope.launch(Dispatchers.IO) {

                val key = serverURL.substring(serverURL.lastIndexOf('/') + 1);

                val url = URL(serverURL)
                val image: Bitmap =
                    BitmapFactory.decodeStream(url.openConnection().getInputStream())
                instance.lru.put(key, image)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Fetching cached image using key value
     * if image is not available in cache then we need to use old method of display image from URL
     *
     * @return bitmap image
     */
    fun retrieveBitmapFromCache(key: String): Bitmap? {
        try {
            return instance.lru.get(key) as Bitmap?
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}