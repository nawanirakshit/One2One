package com.test.papers.utils.extension

import android.content.Context
import android.os.Handler
import android.os.Looper
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Created by Akash Saggu(R4X) on 11/9/18 at 17:14.
 * akashsaggu@protonmail.com
 * @Version 1 (11/9/18)
 * @Updated on 11/9/18
 */

val Context.executor: ExecutorService get() = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1)

inline fun <T> T.onMainThread(crossinline body: () -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    handler.post { body.invoke() }
}

inline fun <T> Context.onWorkerSynchronously(crossinline body: () -> T): T {
    val future: Future<T> = executor.submit(Callable<T> {
        body()
    })
    return future.get()
}

inline fun Context.onWorkerThread(crossinline body: () -> Unit) {
    executor.submit(Callable {
        body()
    })
}

inline fun Context.delay(timeMS: Long, crossinline body: () -> Unit): Handler {
    return Handler().apply {
        postDelayed({
            body()
        }, timeMS)
    }
}

inline fun repeatAfter(timeMS: Long, crossinline body: () -> Unit): Handler {
    val handler = Handler()
    var runnable: Runnable? = null
    runnable = Runnable {
        body()
        handler.postDelayed(runnable!!, timeMS)
    }
    handler.postDelayed(runnable, timeMS)
    return handler
}


inline fun Context.workerRE(crossinline body: () -> Unit): ExecutorService {
    executor.submit(Callable {
        body()
    })
    return executor
}
