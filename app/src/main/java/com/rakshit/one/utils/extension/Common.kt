package com.test.papers.utils.extension

//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.ClipboardManager
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.sleek.construction.config.Config
import android.text.*
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.view.animation.CycleInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rakshit.one.ApplicationClass
import com.rakshit.one.R
import com.rakshit.one.ui.dashboard.main.MainActivity
import com.rakshit.one.ui.prelogin.login.LoginActivity
import com.rakshit.one.ui.prelogin.SplashActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Debounce: To tackle multiple click on any events
 */
fun View.clickWithDebounce(debounceTime: Long = 600L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

/*fun Activity.openWebPageIntent(url: String) {
    val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder();
    val customTabsIntent: CustomTabsIntent = builder.build();
    customTabsIntent.launchUrl(this, Uri.parse(url));
}*/

/*fun Fragment.openWebPageIntent(url: String) {
    activity?.openWebPageIntent(url)
}*/

fun Activity.navigateDashboard() {
    val intent = Intent(this, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
}

fun Fragment.navigateDashboard() {
    activity?.navigateDashboard()
}

fun Activity.navigateLogin() {
    val intent = Intent(this, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
}

fun Fragment.navigateLogin() {
    activity?.navigateLogin()
}

fun Activity.logoutAll(action: Intent.() -> Unit = {}) {

    Config.clearPreferences()

    val intent = Intent(this, SplashActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent.action()
    startActivity(intent)
    finish()
}

fun Fragment.logoutAll(action: Intent.() -> Unit = {}) {
    activity?.logoutAll(action)
}


///////////////////////////////////////// CONTEXT ////////////////////////////////////

/**
 * Util Function for startActivity
 *    open<BooksDetailActivity> {
 *      putExtra("IntentKey","DATA")
 *      putExtra("IntenKey@2", "DATA@2")
 *    }
 *  or
 * open<BooksDetailActivity>()
 * */
inline fun <reified T> Activity.openActivity(extras: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.extras()
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
    startActivity(intent, options.toBundle())
}

inline fun <reified T> Fragment.openActivity(extras: Intent.() -> Unit = {}) {
    val intent = Intent(this.requireActivity(), T::class.java)
    intent.extras()
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this.requireActivity())
    startActivity(intent, options.toBundle())
}

inline fun <reified T> Activity.openActivityForResult(
    requestCode: Int,
    extras: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    intent.extras()
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
    startActivityForResult(intent, requestCode, options.toBundle())
}

inline fun <reified T> Fragment.openActivityForResult(
    requestCode: Int,
    extras: Intent.() -> Unit = {}
) {
    val intent = Intent(this.requireActivity(), T::class.java)
    intent.extras()
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this.requireActivity())
    startActivityForResult(intent, requestCode, options.toBundle())
}

@Throws(IllegalAccessException::class, InstantiationException::class)
inline fun <reified T> newFragmentInstance(extras: Bundle.() -> Unit = {}): T? {

    return (T::class.java.newInstance() as Fragment).apply {
        arguments = Bundle().apply { extras() }
    } as T

}

@Throws(IllegalAccessException::class, InstantiationException::class)
inline fun <reified T> AppCompatActivity.showDialogFragment(extras: Bundle.() -> Unit = {}): T? {

    val instance = newFragmentInstance<T>(extras)
    (instance as DialogFragment).show(
        supportFragmentManager,
        T::class.java.simpleName
    )
    return instance
}

@Throws(IllegalAccessException::class, InstantiationException::class)
inline fun <reified T> Fragment.showDialogFragment(
    fromActivity: Boolean = true,
    extras: Bundle.() -> Unit = {}
): T? {
    val instance = newFragmentInstance<T>(extras)
    (instance as DialogFragment).show(
        if (fromActivity) {
            activity?.supportFragmentManager!!
        } else {
            childFragmentManager
        },
        T::class.java.simpleName
    )
    return instance
}

/*fun Fragment.vibratePhone() {
    activity?.vibratePhone()
}*/
/*

fun Activity.vibratePhone() {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(200)
    }
}
*/

fun Bundle.putStrings(vararg values: Pair<String, String>) {
    values.forEach { value ->
        this.putString(value.first, value.second)
    }
}


fun Context.getColorCompat(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Activity.getColorCompat(@ColorRes color: Int): Int {
    return baseContext.getColorCompat(color)
}

fun Fragment.getColorCompat(@ColorRes color: Int): Int {
    return activity!!.getColorCompat(color)
}

fun Context.showToast(message: String?, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, length).show()
}

fun Drawable.setColorFilter(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    } else {
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}

fun ImageView.setColorFilter(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    } else {
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}

fun Fragment.showToast(msg: String) {
    activity?.showToast(msg)
}

fun Context.parseResColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard(view ?: View(activity))
}

fun Activity.hideKeyboard() {
    if (currentFocus == null) View(this) else currentFocus?.let { hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.openKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}


fun Fragment.openKeyboard() {
    activity?.openKeyboard()
}

fun Activity.openKeyboard() {
    applicationContext?.openKeyboard()
}

fun Context.showAlert(
    message: String?,
    cancelable: Boolean = true,
    showPositiveButton: Boolean = true,
    work: () -> Unit = { }
) {

    if (message.isNullOrEmpty()) return

    val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert)
    } else {
        AlertDialog.Builder(this)
    }

    builder.setMessage(message)
        .setCancelable(cancelable)

    if (showPositiveButton) {
        builder.setPositiveButton("Close") { dialog, id ->
            work.invoke()
            dialog.dismiss()
        }
    }

    val alert = builder.create()
//    alert.getButton(Dialog.BUTTON_NEGATIVE).isAllCaps = false
//    alert.getButton(Dialog.BUTTON_POSITIVE).isAllCaps = false
    alert.show()
}

fun Context.showConfirmAlert(
    message: String?,
    positiveText: String?,
    negativeText: String?,
    onConfirmed: () -> Unit = {},
    onCancel: () -> Unit = { }
) {

    if (message.isNullOrEmpty()) return

    val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
    } else {
        AlertDialog.Builder(this)
    }

    builder.setMessage(message)
        .setIcon(R.mipmap.ic_launcher_round)
        .setTitle(getString(R.string.app_name))
        .setCancelable(false)
        .setPositiveButton(positiveText) { dialog, _ ->
            onConfirmed.invoke()
            dialog.dismiss()
        }
        .setNegativeButton(negativeText) { dialog, _ ->
            onCancel.invoke()
            dialog.dismiss()
        }
        .setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }

    val alert = builder.create()
//    alert.getButton(Dialog.BUTTON_NEGATIVE).isAllCaps = false
//    alert.getButton(Dialog.BUTTON_POSITIVE).isAllCaps = false
    alert.show()
}


///////////////////////////////////////// VIEW ////////////////////////////////////

fun Activity.getDecorView(): View {
    return window.decorView
}

fun Fragment.getDecorView(): View {
    return activity?.window?.decorView!!
}

inline fun EditText.observeTextChange(crossinline body: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            body(p0.toString())
        }
    })
}

inline fun EditText.removeTextChange() {
    removeTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}

inline fun TextView.observeTextChange(crossinline body: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            body(p0.toString())
        }
    })
}

fun View.animateX(value: Float) {
    with(ObjectAnimator.ofFloat(this, View.TRANSLATION_X, value)) {
        duration = 3500
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        start()
    }
}

fun View.animateY(value: Float) {
    with(ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, value)) {
        duration = 3500
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        start()
    }
}

fun View.shake() {
    val shake = TranslateAnimation(0f, 10f, 0f, 0f)
    shake.duration = 500
    shake.interpolator = CycleInterpolator(4f)
    startAnimation(shake)
}

infix fun ViewGroup.inflate(@LayoutRes view: Int): View {
    return LayoutInflater.from(context).inflate(view, this, false)
}

fun Int.inflate(viewGroup: ViewGroup): View {
    return LayoutInflater.from(viewGroup.context).inflate(this, viewGroup, false)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.toggleVisibility() {
    when (this.visibility) {
        View.VISIBLE -> this.gone()
        View.INVISIBLE -> this.visible()
        View.GONE -> this.visible()
    }
}

fun Activity.snack(arrayList: String) {
    getDecorView().snack(arrayList)
}

fun Fragment.snack(arrayList: String) {
    getDecorView().snack(arrayList)
}

inline fun View.snack(
    arrayList: String,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit = {}
) {
    val snack = Snackbar.make(this, arrayList, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}


///////////////////////////////////////// COMMON ////////////////////////////////////

inline fun <T> T.executeSafe(body: () -> Unit) {
    try {
        body.invoke()
    } catch (e: Exception) {

    }
}

fun <T> T.isNull(): Boolean {
    return this == null
}

fun <T> T.isNotNull(): Boolean {
    return this != null
}

inline infix operator fun Int.times(action: (Int) -> Unit) {
    var i = 0
    while (i < this) {
        action(i)
        i++
    }
}

fun String.remove(vararg value: String): String {
    var removeString = this
    value.forEach {
        removeString = removeString.replace(it, "")
    }
    return removeString
}

//*
//  android:textColorHighlight="#f00" // background color when pressed
//    android:textColorLink="#0f0" link backgroudnd color**/
fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        val startIndexOfLink = this.text.toString().indexOf(link.first)
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun dpToPx(dp: Float): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun pxToDp(px: Int): Int {
    return (px / Resources.getSystem().displayMetrics.density).toInt()
}

fun spToPx(sp: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        Resources.getSystem().displayMetrics
    )
}

fun View.setTopMargin(top: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.topMargin = dpToPx(top)
        requestLayout()
    }
    /*(view.layoutParams as RelativeLayout.LayoutParams).setMargins(
        position.left.convertDpToPx(context),
        position.top.convertDpToPx(context),
        position.right.convertDpToPx(context),
        position.bottom.convertDpToPx(context)
    )*/

}

fun View.setLeftMargin(left: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.leftMargin = dpToPx(top)
        requestLayout()
    }
}

fun View.setRightMargin(right: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.rightMargin = dpToPx(top)
        requestLayout()
    }
}

fun View.setBottomMargin(bottom: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.bottomMargin = dpToPx(top)
        requestLayout()
    }
}

fun View.getMarginLayoutParams(): ViewGroup.MarginLayoutParams? {
    return if (layoutParams is ViewGroup.MarginLayoutParams) {
        layoutParams as ViewGroup.MarginLayoutParams
    } else {
        null
    }

}
/*

fun Activity.playSound(@RawRes sound: Int = R.raw.bell) {
    MediaPlayer.create(this, sound).start()
}
*/

fun String.underline(): SpannableString {
    val content = SpannableString(this)
    content.setSpan(UnderlineSpan(), 0, this.length, 0)
    return content
}

fun TextView.underline() {
    text = text.toString().underline()
}
/*

fun Context.openTab(url: String) {
    val builder = CustomTabsIntent.Builder()
// modify toolbar color
    builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
// add share button to overflow men
    builder.addDefaultShareMenuItem()
// add menu item to oveflow
    //builder.addMenuItem("MENU_ITEM_NAME", pendingIntent)
// show website title
    builder.setShowTitle(true)
// modify back button icon
    //builder.setCloseButtonIcon(bitmap)
// menu item icon
    //builder.setActionButton(bitmap, "Android", pendingIntent, true)
// animation for enter and exit of tab            builder.setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
    builder.setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
    val customTabsIntent = builder.build()
    val packageName = CustomTabHelper().getPackageNameToUse(this, url)

    if (packageName == null) {
        // if chrome not available open in web view
        openActivity<WebViewActivity> {
            putExtra(WebViewActivity.EXTRA_URL, url)
        }
    } else {
        customTabsIntent.intent.setPackage(packageName)
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}*/

fun String.copyToClipboard(context: Context) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(this, this))
}

fun String.maintainTwoLength(): String {
    executeSafe {
        if (this.length < 2) {
            return "0$this"
        }
    }
    return this
}

inline fun <reified T : Activity> RecyclerView.ViewHolder.getActivity(): T? {
    val contextWrapperBaseContext = ((itemView.context as ContextWrapper).baseContext)
    val fieldOuterContext = contextWrapperBaseContext.javaClass.getDeclaredField("mOuterContext")
    fieldOuterContext.isAccessible = true
    val activity = fieldOuterContext.get(contextWrapperBaseContext) as? T
    fieldOuterContext.isAccessible = false
    return activity
}

fun Long.getDateInformat(): String? {
    val sd = SimpleDateFormat("EEE,dd MMM-hh:mm")
    val dateformatdate = sd.format(this)
    return dateformatdate
}


fun EditText.value(): String = this.text.toString()

fun TextView.value(): String = this.text.toString()

fun Bitmap.toFile(): File {
    // Get the context wrapper
    val wrapper = ContextWrapper(ApplicationClass.getApp())

    // Initialize a new file instance to save bitmap object
    var file = wrapper.cacheDir
    file = File(file, "${UUID.randomUUID()}.jpg")

    try {
        // Compress the bitmap and save in jpg format
        val stream: OutputStream = FileOutputStream(file)
        compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }

    // Return the saved bitmap uri
    return file
}

fun Spinner.onItemSelectListener(listener: (View?, Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            listener.invoke(view, position)
        }
    }
}

fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean = !isNullOrEmpty()

fun Activity.makeStatusBarTransparent(@IdRes viewToMarginTop: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val controller = window.insetsController
        controller?.hide(WindowInsets.Type.statusBars())
    } else
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN// or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }

    findViewById<View>(viewToMarginTop).fitsSystemWindows = true
    /*  ViewCompat.setOnApplyWindowInsetsListener(
          findViewById(viewToMarginTop)
      ) { _, insets ->
          Log.d("topInset", insets.systemWindowInsetTop.toString())
          Log.d("bottomInset", insets.systemWindowInsetBottom.toString())
          findViewById<ViewGroup>(R.id.toolBarDetail).setTopMargin(insets.systemWindowInsetTop)
          //and so on for left and right insets
          insets.consumeSystemWindowInsets()
      }*/
}

suspend fun onMain(func: () -> Unit) {
    withContext(Dispatchers.Main.immediate) {
        func()
    }
}

/*inline fun <reified T : ViewModel> Fragment.sharedParentFragmentViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = lazy {
    getKoin().getViewModel(
        requireParentFragment(),
        qualifier,
        parameters
    )
}*/


fun Activity.shareText(text: String, title: String) {
    val share = Intent.createChooser(Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)

        // (Optional) Here we're setting the title of the content
        putExtra(Intent.EXTRA_TITLE, title)

        type = "text/plain"
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }, null)
    startActivity(share)
}

fun ViewGroup.disableClick() {
    for (i in 0 until childCount) {
        val childAtView = getChildAt(i)
        if (childAtView is ViewGroup) {
            childAtView.disableClick()
        } else {
            childAtView.isClickable = false
            childAtView.isEnabled = false
        }
    }
}

fun ViewGroup.enableClick() {
    for (i in 0 until childCount) {
        val childAtView = getChildAt(i)
        if (childAtView is ViewGroup) {
            childAtView.enableClick()
        } else {
            childAtView.isClickable = true
            childAtView.isEnabled = true
        }
    }
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun ViewGroup.iterateChilds(func: (View) -> Unit) {
    for (i in 0 until childCount) {
        func(getChildAt(i))
    }
}

fun Date.isToday(): Boolean = DateUtils.isToday(this.time)

fun getDateStamp(): String {
    // Will return -> WED, 17 JUN 2020
    return SimpleDateFormat("E, dd MMM yyyy", Locale.ENGLISH).format(Calendar.getInstance().time)

    //Future Refrence
    // E, dd MMM yyyy HH:mm:ss z     ->     Tue, 02 Jan 2018 18:07:59 IST
}

fun getDateFormatted(date: Date?): String? {
    var time: Date = Calendar.getInstance().time
    if (date != null) {
        time = date
    }
    return SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(time)
}

fun getDateFormattedEventDetail(date: Date?): String? {
    var time: Date = Calendar.getInstance().time
    if (date != null) {
        time = date
    }
    return SimpleDateFormat("dd/MM/yy", Locale.ENGLISH).format(time)
}

fun getDateFormattedEventReplies(date: Date?): String? {
    var time: Date = Calendar.getInstance().time
    if (date != null) {
        time = date
    }
    return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(time)
}

fun getDateFormattedServer(date: Date?): String? {
    var time: Date = Calendar.getInstance().time
    if (date != null) {
        time = date
    }
    return SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(time)
}

fun getDateFormattedStyled(date: Date?): String? {
    var time: Date = Calendar.getInstance().time
    if (date != null) {
        time = date
    }
    return SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH).format(time)
}

fun getDateAsRequested(date: Date): String {

    val duration: Long = System.currentTimeMillis() - date.time
    val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(duration)
    val diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration)
    val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration)

    return when {
        diffInSeconds < 5 -> {
            //XX seconds ago
            "just now"
        }
        diffInSeconds < 60 -> {
            //XX seconds ago
            "$diffInSeconds seconds ago"
        }
        diffInMinutes < 2 -> {
            //XX minutes ago
            "$diffInMinutes minute ago"
        }
        diffInMinutes < 60 -> {
            //XX minutes ago
            "$diffInMinutes minutes ago"
        }
        diffInHours < 24 -> {
            //22 hours Ago
            "$diffInHours hours ago"
        }
        else -> {
            //November 23
            SimpleDateFormat("MMMM d", Locale.ENGLISH).format(date.time)
        }
    }
}

fun getDateAsRequestedNoJustNow(date: Date): String {

    val duration: Long = System.currentTimeMillis() - date.time
    val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(duration)
    val diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration)
    val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration)

    return when {
        diffInSeconds < 60 -> {
            //XX seconds ago
            "$diffInSeconds seconds ago"
        }
        diffInMinutes < 60 -> {
            //XX minutes ago
            "$diffInMinutes minutes ago"
        }
        diffInHours < 24 -> {
            //22 hours Ago
            "$diffInHours hours ago"
        }
        else -> {
            //November 23
            SimpleDateFormat("MMMM d", Locale.ENGLISH).format(date.time)
        }
    }
}

fun getDateFormattedNoYear(date: Date?): String? {
    var time: Date = Calendar.getInstance().time

    if (date != null) {
        time = date
    }

    return DateUtils.getRelativeTimeSpanString(
        time.time,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_RELATIVE
    ).toString()
}

fun <T> Gson.convertToJsonString(t: T): String {
    return toJson(t).toString()
}

fun <T> Gson.convertToModel(jsonString: String, cls: Class<T>): T? {
    return try {
        fromJson(jsonString, cls)
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T> Gson.fromJson(json: String) =
    this.fromJson<T>(json, object : TypeToken<T>() {}.type)

/*
fun Fragment.shareDynamicLink(type: String, id: String) {
    val dynamicLink =
        Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) { // or Firebase.dynamicLinks.shortLinkAsync
            link =
                Uri.parse("https://racematesios.page.link?linkType=$type&linkID=$id")
            domainUriPrefix = "https://racematesios.page.link"

            androidParameters("com.racemates") {
                fallbackUrl = Uri.parse("https://racemates-web.netlify.app/home")
            }
            iosParameters("com.racemates.ios-app") {
                appStoreId = "com.racemates.ios-app"
                setFallbackUrl(Uri.parse("https://racemates-web.netlify.app/home"))
            }
        }

    dynamicLink.addOnCompleteListener {
        if (it.isSuccessful) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, it.result.shortLink.toString());
            startActivity(Intent.createChooser(shareIntent, "Share with"))

        }
    }.addOnCanceledListener {
        showToast("Something went wrong, try again")
    }

}
*/

fun Activity.displayMetrics(): DisplayMetrics {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}


fun openChromeTab(context: Context, url: String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}

fun Context.checkPermissions(vararg permissions: String): Boolean {
    var allPermissionGranted = true
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
            PackageManager.PERMISSION_GRANTED
        )
            allPermissionGranted = false; break
    }
    return allPermissionGranted;
}
