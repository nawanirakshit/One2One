package android.sleek.construction.config

import android.content.Context
import android.sleek.construction.utils.preference.SharedPreferenceUtils

const val IS_LOGGED_IN = "isLogin"
const val UID = "UID"
const val NAME = "NAME"
const val EMAIL = "EMAIL"

object Config {
    private lateinit var preferences: SharedPreferenceUtils

    fun init(context: Context) {
        preferences = SharedPreferenceUtils(context)
    }

    var isLoggedIn: Boolean
        get() = preferences.get(IS_LOGGED_IN, false)
        set(isLogin) = preferences.set(IS_LOGGED_IN, isLogin)

    var uid: String
        get() = preferences.get(UID, "0")
        set(userType) = preferences.set(UID, userType)

    var name: String
        get() = preferences.get(NAME, "")
        set(userType) = preferences.set(NAME, userType)

    var email: String
        get() = preferences.get(EMAIL, "")
        set(userType) = preferences.set(EMAIL, userType)

    fun clearPreferences() {
        preferences.clearAll()
    }
}
