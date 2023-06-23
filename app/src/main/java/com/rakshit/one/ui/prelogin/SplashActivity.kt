package com.rakshit.one.ui.prelogin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.sleek.construction.config.Config
import com.rakshit.one.R
import com.test.papers.kotlin.KotlinBaseActivity
import com.test.papers.utils.extension.navigateDashboard
import com.test.papers.utils.extension.navigateLogin

class SplashActivity : KotlinBaseActivity(android.R.id.content) {

    private var TIME_OUT: Long = 2000
    private val mHandler by lazy { Handler(Looper.getMainLooper()) }
    private lateinit var mRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_splash)

        loadSplashScreen()
    }

    private fun loadSplashScreen() {
        mRunnable = Runnable {
            if (Config.isLoggedIn) {
                navigateDashboard()
            } else navigateLogin()
        }

        mHandler.postDelayed(
            mRunnable, TIME_OUT
        )


    }

}