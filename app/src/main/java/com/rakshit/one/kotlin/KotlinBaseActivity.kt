package com.test.papers.kotlin

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.rakshit.one.R
import com.test.papers.kotlin.listeners.KotlinBaseListener
import com.test.papers.kotlin.navigator.Navigator
import com.test.papers.kotlin.viewmodel.KotlinBaseViewModel
import com.test.papers.kotlin.viewmodel.common.ErrorType
import com.test.papers.utils.extension.executeSafe
import com.test.papers.utils.extension.hideKeyboard
import com.test.papers.utils.extension.showToast
import kotlin.reflect.KClass

@Keep
abstract class KotlinBaseActivity(@IdRes private val container: Int = android.R.id.content) :
    AppCompatActivity(), KotlinBaseListener {

    private var progress: AlertDialog? = null

    private var baseViewModel: KotlinBaseViewModel? = null

    fun setViewModel(vm: KotlinBaseViewModel) {
        baseViewModel = vm
        lifecycle.addObserver(baseViewModel!!)
        observeProgress()
        listenForError()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBackStackListener()
        showLogs("onCreate", javaClass.simpleName)

        onBackPressed(true) {
            if (manager.backStackEntryCount == 0) {
                finish()
            } else {
                supportFragmentManager.popBackStack()
            }
        }

    }

    private fun initBackStackListener() {

        with(supportFragmentManager) {
            addOnBackStackChangedListener {
                val fragment = findFragmentById(container)
                if (fragment != null) executeSafe {
                    navigator.lastFragmentChanged(fragment = fragment as KotlinBaseFragment)
                }

            }
        }
    }

    fun setBottomUpAnimation(@TransitionRes enterAnim: Int, @TransitionRes exitAnim: Int) {
        (window.decorView as ViewGroup).isTransitionGroup = false
        val enter = TransitionInflater.from(this).inflateTransition(enterAnim)
        val exit = TransitionInflater.from(this).inflateTransition(exitAnim)
        exit.excludeTarget(android.R.id.statusBarBackground, true)
        exit.excludeTarget(android.R.id.navigationBarBackground, true)
        window.enterTransition = enter
        window.exitTransition = exit
        window.returnTransition = exit
    }

    private val manager: FragmentManager by lazy {
        supportFragmentManager
    }

    private val navigator: Navigator by lazy { Navigator(this, container) }


    //Default Animation slideInLeft and slideOutRight
    fun setCustomAnimation(
        @AnimatorRes @AnimRes enterAnim: Int, @AnimatorRes @AnimRes exitAnim: Int
    ) {
        navigator.setCustomeAnimation(enterAnim, exitAnim)
    }

    override fun getFragment(kClass: KClass<out Fragment>): Fragment? {
        return supportFragmentManager.findFragmentByTag(kClass.java.simpleName)
    }

    override fun replaceFragment(
        java: KClass<out Fragment>,
        extras: Bundle?,
        userTag: String,
        addToBackStack: Boolean,
        animation: Boolean,
        container: Int
    ) {
        navigator.replaceFragment(
            java.java,
            extras,
            transitionView = null,
            userTag = userTag,
            addToBackStack = addToBackStack,
            animation = animation,
            containerId = container
        )
    }

    override fun replaceFragment(
        fragment: Fragment,
        extras: Bundle?,
        userTag: String,
        addToBackStack: Boolean,
        animation: Boolean,
        container: Int
    ) {
        navigator.replaceFragment(
            fragment,
            extras,
            transitionView = null,
            userTag = userTag,
            addToBackStack = addToBackStack,
            animation = animation,
            containerId = container
        )
    }

    override fun addFragment(
        fragment: KClass<out Fragment>,
        extras: Bundle?,
        tag: String,
        addToBackStack: Boolean,
        animation: Boolean,
        container: Int
    ) {
        navigator.addFragment(
            fragment.java,
            tag = tag,
            addToBackStack = addToBackStack,
            bundle = extras,
            animation = animation,
            containerId = container
        )
    }

    override fun addFragment(
        fragment: Fragment,
        extras: Bundle?,
        tag: String,
        addToBackStack: Boolean,
        animation: Boolean,
        container: Int
    ) {
        navigator.addFragment(
            fragment,
            tag = tag,
            addToBackStack = addToBackStack,
            bundle = extras,
            animation = animation,
            containerId = container
        )
    }


    override fun forceHideKeyboard() {
        val view: View? = findViewById(android.R.id.content)
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


/*
    override fun onBackPressed() {
        if (containsOnlyFragment()) {
            if (manager.backStackEntryCount == 1) {
                finish()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
*/

    open fun containsOnlyFragment(): Boolean {
        return true
    }

    inline fun <reified T : Fragment> getFragment(): T? {
        val fragment = supportFragmentManager.findFragmentByTag(T::class.java.simpleName)
        return if (fragment != null) fragment as T else null
    }


    override fun addChildFragment(
        childFragmentManager: FragmentManager,
        container: Int,
        kClass: KClass<out Fragment>,
        addToBackStack: Boolean,
        bundle: Bundle,
        animation: Boolean
    ) {
        navigator.addChildFragment(
            childFragmentManager, container, kClass, addToBackStack, bundle, animation
        )
    }

    override fun addChildFragment(
        childFragmentManager: FragmentManager,
        container: Int,
        fragment: Fragment,
        addToBackStack: Boolean,
        bundle: Bundle,
        animation: Boolean
    ) {
        navigator.addChildFragment(
            childFragmentManager, container, fragment, addToBackStack, bundle, animation
        )
    }

    override fun getCurrentFragment(): Fragment? = navigator.getCurrentFragment()
    override fun getCurrentFragment(container: Int): Fragment? =
        navigator.getCurrentFragment(container)

    override fun getLastAddedFragment(): Fragment? = supportFragmentManager.fragments.last()

    override fun replaceChildFragment(
        childFragmentManager: FragmentManager,
        container: Int,
        kClass: KClass<out Fragment>,
        addToBackStack: Boolean,
        bundle: Bundle,
        animation: Boolean
    ) {
        navigator.replaceChildFragment(
            childFragmentManager, container, kClass, addToBackStack, bundle, animation
        )
    }

    override fun replaceChildFragment(
        childFragmentManager: FragmentManager,
        container: Int,
        fragment: Fragment,
        addToBackStack: Boolean,
        bundle: Bundle,
        animation: Boolean
    ) {
        navigator.replaceChildFragment(
            childFragmentManager, container, fragment, addToBackStack, bundle, animation
        )
    }

    override fun showLoading() {
        try {
            hideKeyboard()
            if (progress == null) {
                val factory: LayoutInflater = LayoutInflater.from(this)
                val deleteDialogView: View = factory.inflate(R.layout.loading_screen, null)
                progress = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle).create()
                progress!!.setView(deleteDialogView)
                progress!!.setCanceledOnTouchOutside(false)
                progress!!.setCancelable(false)

                forceHideKeyboard()
            }

            progress!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * For hiding the loading
     */
    override fun hideLoading() {
        try {
            progress?.dismiss()
            forceHideKeyboard()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun observeProgress() {
        baseViewModel?.progressObserver?.observe(this, androidx.lifecycle.Observer {
            when (it) {
                true -> showLoading()
                false -> hideLoading()
            }
        })
    }


    private fun listenForError() {
        baseViewModel?.errorObserver?.observe(this, androidx.lifecycle.Observer {
            when (it.errorType) {
                ErrorType.TOAST -> showToast(it.msg ?: "")
                ErrorType.CENTER_TOAST -> showToast(it.msg)
            }
        })
    }

    fun showLogs(tag: String, message: String) {
        Log.d(tag, message)
    }
}

inline fun <reified T : Fragment> KotlinBaseActivity.replaceFragment(
    animation: Boolean = false,
    userTag: String = "",
    addToBackStack: Boolean = true,
    container: Int = 0,
    extras: Bundle.() -> Unit = {}
) {
    replaceFragment(
        T::class,
        Bundle().apply { extras() },
        userTag,
        addToBackStack,
        animation = animation,
        container = container
    )
}

inline fun <reified T : Fragment> KotlinBaseActivity.addFragment(
    animation: Boolean = true,
    userTag: String = "",
    addToBackStack: Boolean = true,
    container: Int = 0,
    extras: Bundle.() -> Unit = {}
) {
    addFragment(
        T::class,
        Bundle().apply { extras() },
        userTag,
        addToBackStack,
        animation = animation,
        container = container
    )
}

fun KotlinBaseActivity.replaceFragment(
    fragment: Fragment,
    animation: Boolean = true,
    userTag: String = "",
    addToBackStack: Boolean = true,
    container: Int = 0,
    extras: Bundle.() -> Unit = {}
) {
    replaceFragment(
        fragment,
        Bundle().apply { extras() },
        userTag,
        addToBackStack,
        animation = animation,
        container = container
    )
}

fun KotlinBaseActivity.addFragment(
    fragment: Fragment,
    animation: Boolean = true,
    userTag: String = "",
    addToBackStack: Boolean = true,
    container: Int = 0,
    extras: Bundle.() -> Unit = {}
) {
    addFragment(
        fragment,
        Bundle().apply { extras() },
        userTag,
        addToBackStack,
        animation = animation,
        container = container
    )
}


inline fun <reified T : Fragment> KotlinBaseFragment.replaceFragment(
    animation: Boolean = true,
    userTag: String = "",
    addToBackStack: Boolean = true,
    container: Int = 0,
    extras: Bundle.() -> Unit = {}
) {
    baseListener.replaceFragment(
        T::class,
        Bundle().apply { extras() },
        userTag,
        addToBackStack,
        animation = animation,
        container = container
    )
}

inline fun <reified T : Fragment> KotlinBaseFragment.addFragment(
    animation: Boolean = true,
    userTag: String = "",
    addToBackStack: Boolean = true,
    container: Int = 0,
    extras: Bundle.() -> Unit = {}
) {
    baseListener.addFragment(
        T::class,
        Bundle().apply { extras() },
        userTag,
        addToBackStack,
        animation = animation,
        container = container
    )
}


fun KotlinBaseFragment.replaceFragment(
    fragment: Fragment,
    animation: Boolean = true,
    userTag: String = "",
    addToBackStack: Boolean = true,
    container: Int = 0,
    extras: Bundle.() -> Unit = {}
) {
    baseListener.replaceFragment(
        fragment,
        Bundle().apply { extras() },
        userTag,
        addToBackStack,
        animation = animation,
        container = container
    )
}

fun KotlinBaseFragment.addFragment(
    fragment: Fragment,
    animation: Boolean = true,
    userTag: String = "",
    addToBackStack: Boolean = true,
    container: Int = 0,
    extras: Bundle.() -> Unit = {}
) {
    baseListener.addFragment(
        fragment,
        Bundle().apply { extras() },
        userTag,
        addToBackStack,
        animation = animation,
        container = container
    )
}

fun setFullScreen(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        activity.window.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}


fun enableFullScreen(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        activity.window.insetsController?.let {
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            it.hide(WindowInsets.Type.systemBars())
        }
    } else {
        @Suppress("DEPRECATION") activity.window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}

fun disableFullScreen(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        activity.window.setDecorFitsSystemWindows(false)
        activity.window.insetsController?.show(WindowInsets.Type.systemBars())
    } else {
        @Suppress("DEPRECATION") activity.window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}

fun AppCompatActivity.onBackPressed(isEnabled: Boolean, callback: () -> Unit) {
    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(isEnabled) {
        override fun handleOnBackPressed() {
            println("onBackPressed >>>>>>>>>")
            callback()
        }
    })
}

fun AppCompatActivity.checkBackPressEvent() {
    println("checkBackPressEvent >>>>>>>>>")
    if (supportFragmentManager.backStackEntryCount == 0) {
        finish()
    } else {
        supportFragmentManager.popBackStack()
    }
}

fun AppCompatActivity.clearBackStack() {
    val fm: FragmentManager = supportFragmentManager
    for (i in 0 until fm.backStackEntryCount) {
        fm.popBackStack()
    }
}

